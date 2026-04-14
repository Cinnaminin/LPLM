package dev.cinnaminin.mixin;

import dev.cinnaminin.lplmConfig;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.ServerMetadata;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MinecraftServer.class)
public class MixinServerMetadata9 {

    @Inject(method = "createMetadata", at = @At("RETURN"), cancellable = true)
    private void lplm$fixMetadata(CallbackInfoReturnable<ServerMetadata> cir) {

        if (lplmConfig.INSTANCE == null) return;

        ServerMetadata old = cir.getReturnValue();

        ServerMetadata fixed = new ServerMetadata(
                old.description(),
                old.players().map(p ->
                        new ServerMetadata.Players(
                                lplmConfig.INSTANCE.maxPlayers,
                                p.online(),
                                p.sample()
                        )
                ),
                old.version(),
                old.favicon(),
                old.secureChatEnforced()
        );

        cir.setReturnValue(fixed);
    }
}