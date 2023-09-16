import com.sk89q.jnbt.CompoundTag
import com.sk89q.jnbt.ListTagBuilder
import com.sk89q.jnbt.StringTag
import com.sk89q.jnbt.Tag
import com.sk89q.worldedit.WorldEdit
import com.sk89q.worldedit.bukkit.BukkitAdapter
import com.sk89q.worldedit.entity.BaseEntity
import com.sk89q.worldedit.extension.input.ParserContext
import com.sk89q.worldedit.util.Location
import io.github.monun.kommand.StringType
import io.github.monun.kommand.getValue
import io.github.monun.kommand.kommand
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.entity.EntityType
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin
import utils.selection

class BlockDisplayer : JavaPlugin() {
    override fun onEnable() {
        setupKommand()

        getLogger().info("Enabled!")
    }

    override fun onDisable() {
        getLogger().info("Disabled!")
    }

    private fun setupKommand() {
        kommand {
            register("/display") {
                requires { sender is Player && player.isOp }
                executes {
                    player.sendMessage(Component.text("Usage: //display <tag>").color(NamedTextColor.RED))

                    return@executes
                }

                then("tag" to string(StringType.SINGLE_WORD)) {
                    requires { sender is Player && player.isOp }
                    executes {
                        val tag: String by it
                        val selection = player.selection

                        if (selection == null) {
                            player.sendMessage(Component.text("Please select region with selwand").color(NamedTextColor.RED))

                            return@executes
                        }

                        val session = WorldEdit.getInstance().sessionManager.findByName(player.name)

                        if (session == null) {
                            player.sendMessage(Component.text("Please select region with selwand").color(NamedTextColor.RED))

                            return@executes
                        }

                        player.sendMessage(Component.text("Converting blocks to block display with the command tag $tag").color(NamedTextColor.LIGHT_PURPLE))

                        val actor = BukkitAdapter.adapt(player)
                        val editSession = session.createEditSession(actor)

                        selection.forEach { vector ->
                            val block = editSession.getBlock(vector)
                            val entity = BaseEntity(BukkitAdapter.adapt(EntityType.BLOCK_DISPLAY))
                            val context = ParserContext()

                            context.actor = actor
                            context.world = actor.world
                            context.session = session
                            context.extent = actor.world

                            editSession.setBlock(vector, WorldEdit.getInstance().patternFactory.parseFromInput("air", context))

                            val props = mutableMapOf<String, Tag>()

                            block.states.forEach {
                                props[it.key.name] = StringTag(it.value.toString().lowercase())
                            }

                            val nbtData = CompoundTag(mapOf(
                                    Pair("block_state", CompoundTag(mapOf(
                                            Pair("Name", StringTag(block.blockType.id)),
                                            Pair("Properties", CompoundTag(props))
                                    ))),
                                    Pair("Tags", ListTagBuilder.createWith(StringTag(tag)).build())
                            ))

                            entity.nbtData = nbtData

                            if (!BukkitAdapter.adapt(block.blockType).isAir) {
                                editSession.createEntity(Location(selection.world, vector.toVector3(), 0f, 0f), entity)
                            }
                        }

                        editSession.close()

                        session.remember(editSession)
                    }
                }
            }
        }
    }
}