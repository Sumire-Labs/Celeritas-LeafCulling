package jp.s12kuma01.celeritasleafculling.gui;

import jp.s12kuma01.celeritasleafculling.CeleritasLeafCullingMod;
import jp.s12kuma01.celeritasleafculling.LeafCullingConfig;
import org.taumc.celeritas.api.options.structure.OptionStorage;

/**
 * Storage wrapper for Celeritas options system
 */
public class LeafCullingOptionsStorage implements OptionStorage<LeafCullingConfig> {

    @Override
    public LeafCullingConfig getData() {
        return CeleritasLeafCullingMod.config();
    }

    @Override
    public void save() {
        CeleritasLeafCullingMod.config().save();
        CeleritasLeafCullingMod.LOGGER.info("Saved Leaf Culling config from GUI");
    }
}
