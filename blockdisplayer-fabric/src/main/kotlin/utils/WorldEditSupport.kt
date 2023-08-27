package utils

import com.sk89q.worldedit.WorldEdit
import com.sk89q.worldedit.fabric.FabricAdapter
import com.sk89q.worldedit.regions.Region
import net.minecraft.server.network.ServerPlayerEntity

val ServerPlayerEntity.selection: Region?
    get() {
        return try {
            WorldEdit.getInstance().sessionManager[FabricAdapter.adaptPlayer(this)]?.run {
                getSelection(selectionWorld)
            }
        } catch (e: Exception) {
            null
        }
    }