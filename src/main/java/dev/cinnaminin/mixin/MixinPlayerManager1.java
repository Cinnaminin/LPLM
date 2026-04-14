package dev.cinnaminin.mixin;

import dev.cinnaminin.lplmConfig;
import net.minecraft.server.PlayerManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerManager.class)
public abstract class MixinPlayerManager1 {

	@Shadow @Mutable
	protected int maxPlayers;

	@Inject(method = "<init>", at = @At("RETURN"))
	private void lplm$overrideLimit(CallbackInfo ci) {
		if (lplmConfig.INSTANCE != null) {
			this.maxPlayers = lplmConfig.INSTANCE.maxPlayers;
		}
	}
}