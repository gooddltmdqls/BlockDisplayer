import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.arguments.ArgumentType
import com.mojang.brigadier.arguments.StringArgumentType
import com.mojang.brigadier.builder.ArgumentBuilder
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import com.mojang.brigadier.builder.RequiredArgumentBuilder
import com.sk89q.jnbt.CompoundTag
import com.sk89q.jnbt.ListTagBuilder
import com.sk89q.jnbt.StringTag
import com.sk89q.jnbt.Tag
import com.sk89q.worldedit.WorldEdit
import com.sk89q.worldedit.entity.BaseEntity
import com.sk89q.worldedit.extension.input.ParserContext
import com.sk89q.worldedit.fabric.FabricAdapter
import com.sk89q.worldedit.util.Location
import com.sk89q.worldedit.world.block.BlockTypes
import com.sk89q.worldedit.world.entity.EntityTypes
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback
import net.minecraft.server.command.ServerCommandSource
import net.minecraft.text.Text
import utils.selection

class BlockDisplayerCommand {
    fun register() {
        CommandRegistrationCallback.EVENT.register { dispatcher, _, _ ->
            dispatcher.register(
                LiteralArgumentBuilder.literal<ServerCommandSource>("/display")
                    .requires {
                        if (it.player == null) return@requires false
                        return@requires it.player!!.hasPermissionLevel(4)
                    }
                    .then(
                        RequiredArgumentBuilder.argument<ServerCommandSource, String>("tag", StringArgumentType.word())
                            .requires {
                                if (it.player == null) return@requires false
                                return@requires it.player!!.hasPermissionLevel(4)
                            }
                            .executes { ctx ->
                                println("this is working111")
                                val tag = StringArgumentType.getString(ctx, "tag")
                                val player = ctx.source.playerOrThrow
                                val selection = player.selection

                                if (selection == null) {
                                    player.sendMessage(Text.literal("§cPlease select region with selwand"))

                                    return@executes 0
                                }

                                val session = WorldEdit.getInstance().sessionManager.findByName(player.entityName)

                                if (session == null) {
                                    player.sendMessage(Text.literal("§cPlease select region with selwand"))

                                    return@executes 0
                                }

                                player.sendMessage(Text.literal("§dConverting blocks to block display with the command tag $tag"))

                                val actor = FabricAdapter.adaptPlayer(player)
                                val editSession = session.createEditSession(actor)

                                selection.forEach { vector ->
                                    val block = editSession.getBlock(vector)
                                    val entity = BaseEntity(EntityTypes.BLOCK_DISPLAY)
                                    val context = ParserContext()

                                    context.actor = actor
                                    context.world = actor.world
                                    context.session = session
                                    context.extent = actor.world

                                    editSession.setBlock(
                                        vector,
                                        WorldEdit.getInstance().patternFactory.parseFromInput("air", context)
                                    )

                                    val props = mutableMapOf<String, Tag>()

                                    block.states.forEach {
                                        props[it.key.name] = StringTag(it.value.toString().lowercase())
                                    }

                                    val nbtData = CompoundTag(
                                        mapOf(
                                            Pair(
                                                "block_state", CompoundTag(
                                                    mapOf(
                                                        Pair("Name", StringTag(block.blockType.id)),
                                                        Pair("Properties", CompoundTag(props))
                                                    )
                                                )
                                            ),
                                            Pair("Tags", ListTagBuilder.createWith(StringTag(tag)).build())
                                        )
                                    )

                                    entity.nbtData = nbtData

                                    if (block.blockType != BlockTypes.AIR) {
                                        editSession.createEntity(
                                            Location(selection.world, vector.toVector3(), 0f, 0f),
                                            entity
                                        )
                                    }
                                }

                                editSession.close()

                                session.remember(editSession)

                                return@executes 0
                            }
                    )
            )
        }
    }
}