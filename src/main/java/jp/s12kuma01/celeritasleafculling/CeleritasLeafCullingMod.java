package jp.s12kuma01.celeritasleafculling;

import jp.s12kuma01.celeritasleafculling.util.BlockConstantRandom;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLConstructionEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.taumc.celeritas.api.OptionGUIConstructionEvent;

import java.util.function.Predicate;

@Mod(modid = CeleritasLeafCullingMod.MOD_ID,
     name = CeleritasLeafCullingMod.MOD_NAME,
     version = CeleritasLeafCullingMod.VERSION,
     clientSideOnly = true)
public class CeleritasLeafCullingMod {

    public static final String MOD_ID = "celeritasleafculling";
    public static final String MOD_NAME = "Celeritas Leaf Culling";
    public static final String VERSION = "1.0.0";

    public static final Logger LOGGER = LogManager.getLogger(MOD_NAME);

    private static LeafCullingConfig CONFIG;

    @Mod.EventHandler
    public void construct(FMLConstructionEvent event) {
        if (Loader.isModLoaded("celeritas")) {
            try {
                Class.forName("org.taumc.celeritas.api.OptionGUIConstructionEvent");
                // Register option GUI construction listener
                OptionGUIConstructionEvent.BUS.addListener(jp.s12kuma01.celeritasleafculling.gui.LeafCullingOptionsListener::onCeleritasOptionsConstruct);
                LOGGER.info("Successfully registered Leaf Culling options with Celeritas GUI");
            } catch (Throwable t) {
                if (t instanceof NoClassDefFoundError) {
                    LOGGER.error("Celeritas version is too old, use 2.4.0 or newer");
                } else {
                    LOGGER.error("Unable to check if Celeritas is up-to-date", t);
                }
            }
        } else {
            LOGGER.warn("Celeritas not found! This mod requires Celeritas to function.");
        }
    }

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        LOGGER.info("Initializing {}...", MOD_NAME);
        CONFIG = LeafCullingConfig.load(event.getSuggestedConfigurationFile().toPath().getParent().toFile());
    }

    public static LeafCullingConfig config() {
        return CONFIG;
    }

    public static boolean shouldCullSide(int depth, BlockPos pos, IBlockAccess access, EnumFacing facing, Predicate<Block> blockCheck) {
        boolean cull = true;
        float rejectionChance = (float) CONFIG.getEffectiveRandomRejection();

        boolean outerBlock = false;
        for (int i = 1; i <= depth; i++) {
            IBlockState state = access.getBlockState(pos.offset(facing, i));
            cull &= blockCheck.test(state.getBlock());

            if (!cull && i == 1)
                outerBlock = true;
        }

        if (!outerBlock && !cull && BlockConstantRandom.getConstantRandomSeeded(pos) <= rejectionChance)
            cull = true;

        return cull;
    }
}
