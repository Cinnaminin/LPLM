package dev.cinnaminin.mixin;

import net.fabricmc.loader.api.FabricLoader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import java.util.List;
import java.util.Set;

public class VersionMixinPlugin implements IMixinConfigPlugin {

    private static final Logger LOGGER = LogManager.getLogger("LPLM-Plugin");

    private boolean isNewVersion;

    // this makes sure the correct mixins get loaded for the right version because the method used for
    // 1.21.1 - 1.21.8 was changed in 1.21.9+

    @Override
    public void onLoad(String mixinPackage) {
        LOGGER.info("[LPLM] Plugin loaded");

        String version = FabricLoader.getInstance()
                .getModContainer("minecraft")
                .get()
                .getMetadata()
                .getVersion()
                .getFriendlyString();

        LOGGER.info("[LPLM] MC VERSION = {}", version);

        String[] parts = version.split("\\.");
        int minor = Integer.parseInt(parts[1]);
        int patch = parts.length > 2 ? Integer.parseInt(parts[2]) : 0;

        isNewVersion = (minor > 21) || (minor == 21 && patch >= 9);

        LOGGER.info("[LPLM] isNewVersion = {}", isNewVersion);
    }

    @Override
    public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {

        boolean result;

        if (mixinClassName.endsWith("MixinPlayerManager9")) {
            result = isNewVersion;
        }
        else if (mixinClassName.endsWith("MixinServerMetadata9")) {
            result = isNewVersion;
        }
        else if (mixinClassName.endsWith("MixinPlayerManager1")) {
            result = !isNewVersion;
        }
        else if (mixinClassName.endsWith("MixinMinecraftServer")) {
            result = !isNewVersion;
        }
        else {
            result = true;
        }

        // added loggers because my dumbahh forgot to put the mixins on the .json so they could never be managed
        // but i thought they just werent working lmao
        // still usefull to have i guess :P
        LOGGER.info("[LPLM] Evaluating mixin: {} -> {}", mixinClassName, result);

        return result;
    }

    @Override public String getRefMapperConfig() { return null; }
    @Override public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) {}
    @Override public List<String> getMixins() { return null; }

    @Override
    public void preApply(String targetClassName, org.objectweb.asm.tree.ClassNode targetClass,
                         String mixinClassName, IMixinInfo mixinInfo) {
        LOGGER.info("[LPLM] preApply: {} -> {}", mixinClassName, targetClassName);
    }

    @Override
    public void postApply(String targetClassName, org.objectweb.asm.tree.ClassNode targetClass,
                          String mixinClassName, IMixinInfo mixinInfo) {
        LOGGER.info("[LPLM] postApply: {} -> {}", mixinClassName, targetClassName);
    }
}