package jp.s12kuma01.celeritasleafculling.gui;

import jp.s12kuma01.celeritasleafculling.CeleritasLeafCullingMod;
import org.taumc.celeritas.api.OptionGUIConstructionEvent;

/**
 * Listener for Celeritas GUI construction
 * Registers Leaf Culling options page
 */
public class LeafCullingOptionsListener {

    public static void onCeleritasOptionsConstruct(OptionGUIConstructionEvent event) {
        CeleritasLeafCullingMod.LOGGER.info("Registering Leaf Culling options page with Celeritas GUI");
        event.addPage(LeafCullingOptionPages.leafCulling());
    }
}
