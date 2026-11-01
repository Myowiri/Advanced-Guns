package meow.cats;

import meow.cats.advancedguns.config.Config;
import meow.cats.advancedguns.config.ModCommands;
import meow.cats.advancedguns.event.EventManager;
import meow.cats.advancedguns.items.GunItem;
import meow.cats.advancedguns.items.MagazineItem;
import meow.cats.advancedguns.items.ModItems;
import meow.cats.advancedguns.misc.Zoom;
import meow.cats.advancedguns.misc.sounds;
import meow.cats.advancedguns.network.ReloadPayload;
import meow.cats.advancedguns.projectile.ModEntities;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraft.resources.Identifier;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Weapons implements ModInitializer, ClientModInitializer {
	public static final String MOD_ID = "weapons";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	public static Zoom zoom;

	public static EventManager eventManager;

	@Override
	public void onInitialize() {

		PayloadTypeRegistry.serverboundPlay().register(ReloadPayload.TYPE, ReloadPayload.CODEC);

		ServerPlayNetworking.registerGlobalReceiver(ReloadPayload.TYPE, (payload, context) -> {
			context.server().execute(() -> {
				net.minecraft.server.level.ServerPlayer player = context.player();
				ItemStack stack = player.getItemInHand(InteractionHand.MAIN_HAND);
				if (stack.getItem() instanceof GunItem gun) {
					gun.reload(player, stack);
				}
				if (stack.getItem() instanceof MagazineItem mag) {
					mag.addAmmo(player, stack);
				}
			});
		});
		ModCommands.register();
		Config.load();
		sounds.load();
		ModItems.initialize();



	}

	public static Identifier id(String path) {
		return Identifier.fromNamespaceAndPath(MOD_ID, path);
	}

	@Override
	public void onInitializeClient() {
		eventManager = new EventManager();
		zoom = new Zoom();
		eventManager.init();
		EntityRendererRegistry.register(ModEntities.BULLET, ThrownItemRenderer::new);
	}
}
