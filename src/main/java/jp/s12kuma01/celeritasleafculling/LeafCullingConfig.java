package jp.s12kuma01.celeritasleafculling;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Modifier;

/**
 * Configuration for Celeritas Leaf Culling
 * Simple JSON-based config system with preset support
 */
public class LeafCullingConfig {

    private static final Gson gson = new GsonBuilder()
            .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
            .setPrettyPrinting()
            .excludeFieldsWithModifiers(Modifier.PRIVATE)
            .create();

    /**
     * Enable/Disable leaf culling entirely
     */
    public boolean enabled = true;

    /**
     * Current preset mode
     */
    public CullingPreset preset = CullingPreset.BALANCED;

    /**
     * Custom depth (used when preset is CUSTOM)
     */
    public int customDepth = 2;

    /**
     * Custom random rejection (used when preset is CUSTOM)
     */
    public double customRandomRejection = 0.2;

    private transient File file;

    /**
     * Get effective depth based on current preset
     */
    public int getEffectiveDepth() {
        if (!enabled) return 0;
        return preset == CullingPreset.CUSTOM ? customDepth : preset.depth;
    }

    /**
     * Get effective random rejection based on current preset
     */
    public double getEffectiveRandomRejection() {
        if (!enabled) return 0.0;
        return preset == CullingPreset.CUSTOM ? customRandomRejection : preset.randomRejection;
    }

    public static LeafCullingConfig load(File configDir) {
        File configFile = new File(configDir, "celeritas-leaf-culling.json");
        LeafCullingConfig config;

        if (configFile.exists()) {
            try (FileReader reader = new FileReader(configFile)) {
                config = gson.fromJson(reader, LeafCullingConfig.class);
                CeleritasLeafCullingMod.LOGGER.info("Loaded config from {}", configFile);
            } catch (Exception e) {
                CeleritasLeafCullingMod.LOGGER.error("Failed to load config, using defaults", e);
                config = new LeafCullingConfig();
            }
        } else {
            CeleritasLeafCullingMod.LOGGER.info("Creating default config at {}", configFile);
            config = new LeafCullingConfig();
        }

        config.file = configFile;
        config.save();

        return config;
    }

    public void save() {
        File dir = this.file.getParentFile();

        if (!dir.exists()) {
            if (!dir.mkdirs()) {
                throw new RuntimeException("Could not create config directory");
            }
        }

        try (FileWriter writer = new FileWriter(this.file)) {
            gson.toJson(this, writer);
        } catch (IOException e) {
            CeleritasLeafCullingMod.LOGGER.error("Failed to save config", e);
        }
    }

    public enum CullingPreset {
        FAST(1, 0.0),
        BALANCED(2, 0.2),
        FANCY(3, 0.1),
        CUSTOM(2, 0.2);

        public final int depth;
        public final double randomRejection;

        CullingPreset(int depth, double randomRejection) {
            this.depth = depth;
            this.randomRejection = randomRejection;
        }
    }
}
