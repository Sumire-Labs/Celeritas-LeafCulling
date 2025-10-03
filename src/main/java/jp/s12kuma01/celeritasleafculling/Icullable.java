package jp.s12kuma01.celeritasleafculling;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

public interface Icullable {
    boolean cll$shouldCullSide(IBlockState state, IBlockAccess access, BlockPos pos, EnumFacing facing);
}
