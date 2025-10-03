package jp.s12kuma01.celeritasleafculling.mixin;

import jp.s12kuma01.celeritasleafculling.Icullable;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Block.class)
public class MixinBlock {

    @Inject(method = "shouldSideBeRendered", at = @At("HEAD"), cancellable = true)
    private void MixinShouldSideBeRendered(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side, CallbackInfoReturnable<Boolean> ci){
        if (blockState.getBlock() instanceof Icullable cullable) {
            if(cullable.cll$shouldCullSide(blockState, blockAccess, pos, side)) {
                ci.setReturnValue(false);
                ci.cancel();
            }
        }
    }

}
