package jp.s12kuma01.celeritasleafculling.mixin;

import jp.s12kuma01.celeritasleafculling.Icullable;
import jp.s12kuma01.celeritasleafculling.CeleritasLeafCullingMod;
import jp.s12kuma01.celeritasleafculling.compat.Compat;
import net.minecraft.block.BlockLeaves;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(BlockLeaves.class)
public class MixinLeaves implements Icullable {

    @Override
    @Unique
    public boolean cll$shouldCullSide(IBlockState state, IBlockAccess access, BlockPos pos, EnumFacing facing) {
        if (!CeleritasLeafCullingMod.config().enabled) {
            return false; // Culling disabled, render all sides
        }

        int effectiveDepth = Compat.isFancyLeaves() ? CeleritasLeafCullingMod.config().getEffectiveDepth() : 1;

        return CeleritasLeafCullingMod.shouldCullSide(
            effectiveDepth,
            pos, access, facing,
            (block) -> block instanceof BlockLeaves
        );
    }
}
