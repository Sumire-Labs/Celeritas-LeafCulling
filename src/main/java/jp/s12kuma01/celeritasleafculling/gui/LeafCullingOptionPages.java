package jp.s12kuma01.celeritasleafculling.gui;

import com.google.common.collect.ImmutableList;
import jp.s12kuma01.celeritasleafculling.LeafCullingConfig;
import net.minecraft.client.resources.I18n;
import org.embeddedt.embeddium.impl.gui.framework.TextComponent;
import org.taumc.celeritas.api.options.OptionIdentifier;
import org.taumc.celeritas.api.options.control.ControlValueFormatter;
import org.taumc.celeritas.api.options.control.CyclingControl;
import org.taumc.celeritas.api.options.control.SliderControl;
import org.taumc.celeritas.api.options.control.TickBoxControl;
import org.taumc.celeritas.api.options.structure.OptionFlag;
import org.taumc.celeritas.api.options.structure.OptionGroup;
import org.taumc.celeritas.api.options.structure.OptionImpl;
import org.taumc.celeritas.api.options.structure.OptionPage;

import java.util.ArrayList;
import java.util.List;

/**
 * Defines the Leaf Culling option page for Celeritas GUI
 */
public class LeafCullingOptionPages {

    public static final OptionIdentifier<Void> LEAF_CULLING_PAGE = OptionIdentifier.create("celeritasleafculling", "leafculling");

    private static final LeafCullingOptionsStorage storage = new LeafCullingOptionsStorage();

    public static OptionPage leafCulling() {
        List<OptionGroup> groups = new ArrayList<>();

        // Main toggle group
        groups.add(OptionGroup.createBuilder()
                .add(OptionImpl.createBuilder(boolean.class, storage)
                        .setName(TextComponent.literal(I18n.format("celeritasleafculling.option.enabled")))
                        .setTooltip(TextComponent.literal(I18n.format("celeritasleafculling.option.enabled.tooltip")))
                        .setControl(TickBoxControl::new)
                        .setBinding((opts, value) -> opts.enabled = value,
                                   opts -> opts.enabled)
                        .setFlags(OptionFlag.REQUIRES_RENDERER_RELOAD)
                        .build()
                )
                .build());

        // Preset selection group
        groups.add(OptionGroup.createBuilder()
                .add(OptionImpl.createBuilder(LeafCullingConfig.CullingPreset.class, storage)
                        .setName(TextComponent.literal(I18n.format("celeritasleafculling.option.preset")))
                        .setTooltip(TextComponent.literal(I18n.format("celeritasleafculling.option.preset.tooltip")))
                        .setControl(opts -> new CyclingControl<>(opts, LeafCullingConfig.CullingPreset.class,
                                new TextComponent[] {
                                    TextComponent.literal(I18n.format("celeritasleafculling.option.preset.fast")),
                                    TextComponent.literal(I18n.format("celeritasleafculling.option.preset.balanced")),
                                    TextComponent.literal(I18n.format("celeritasleafculling.option.preset.fancy")),
                                    TextComponent.literal(I18n.format("celeritasleafculling.option.preset.custom"))
                                }))
                        .setBinding((opts, value) -> opts.preset = value,
                                   opts -> opts.preset)
                        .setFlags(OptionFlag.REQUIRES_RENDERER_RELOAD)
                        .build()
                )
                .build());

        // Custom settings group (only visible when CUSTOM preset is selected)
        groups.add(OptionGroup.createBuilder()
                .add(OptionImpl.createBuilder(int.class, storage)
                        .setName(TextComponent.literal(I18n.format("celeritasleafculling.option.depth")))
                        .setTooltip(TextComponent.literal(I18n.format("celeritasleafculling.option.depth.tooltip")))
                        .setControl(opts -> new SliderControl(opts, 0, 5, 1, ControlValueFormatter.number()))
                        .setBinding((opts, value) -> opts.customDepth = value,
                                   opts -> opts.customDepth)
                        .setFlags(OptionFlag.REQUIRES_RENDERER_RELOAD)
                        .build()
                )
                .add(OptionImpl.createBuilder(int.class, storage)
                        .setName(TextComponent.literal(I18n.format("celeritasleafculling.option.randomRejection")))
                        .setTooltip(TextComponent.literal(I18n.format("celeritasleafculling.option.randomRejection.tooltip")))
                        .setControl(opts -> new SliderControl(opts, 0, 50, 1, ControlValueFormatter.percentage()))
                        .setBinding((opts, value) -> opts.customRandomRejection = value / 100.0,
                                   opts -> (int)(opts.customRandomRejection * 100))
                        .setFlags(OptionFlag.REQUIRES_RENDERER_RELOAD)
                        .build()
                )
                .build());

        return new OptionPage(
                LEAF_CULLING_PAGE,
                TextComponent.literal(I18n.format("celeritasleafculling.option.page.title")),
                ImmutableList.copyOf(groups)
        );
    }
}
