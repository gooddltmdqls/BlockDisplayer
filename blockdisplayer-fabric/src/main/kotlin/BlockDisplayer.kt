import net.fabricmc.api.ModInitializer

class BlockDisplayer : ModInitializer {
    override fun onInitialize() {
        BlockDisplayerCommand().register()
    }
}