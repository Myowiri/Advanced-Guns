package meow.cats;

import meow.cats.advancedguns.config.ClientConfig;
import meow.cats.advancedguns.config.ServerConfig;
import meow.cats.advancedguns.config.ModCommands;
import meow.cats.advancedguns.event.EventManager;
import meow.cats.advancedguns.items.GunItem;
import meow.cats.advancedguns.items.MagazineItem;
import meow.cats.advancedguns.items.ModItems;
import meow.cats.advancedguns.misc.Zoom;
import meow.cats.advancedguns.misc.sounds;
import meow.cats.advancedguns.misc.HasMagazineProperty;
import meow.cats.advancedguns.network.*;
import meow.cats.advancedguns.projectile.ModEntities;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraft.client.renderer.item.properties.conditional.ConditionalItemModelProperties;
import net.minecraft.resources.Identifier;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Weapons implements ModInitializer, ClientModInitializer {
	public static final String MOD_ID = "weapons";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	public static Zoom zoom;
	public static boolean isCrosshairHidden = false;
	public static boolean areNametagsHidden = false;
	public static boolean canReloadFromInventory = false;
	public static EventManager eventManager;


	@Override
	public void onInitialize() {
		ServerConfig.load();
		PayloadTypeRegistry.serverboundPlay().register(ReloadPayload.TYPE, ReloadPayload.CODEC);
		PayloadTypeRegistry.serverboundPlay().register(ShootPayload.TYPE, ShootPayload.CODEC);
		PayloadTypeRegistry.clientboundPlay().register(CrosshairStatePayload.TYPE, CrosshairStatePayload.STREAM_CODEC);
		PayloadTypeRegistry.clientboundPlay().register(NameTagsStatePayload.TYPE, NameTagsStatePayload.STREAM_CODEC);
		PayloadTypeRegistry.clientboundPlay().register(ReloadFromInventoryStatePayload.TYPE, ReloadFromInventoryStatePayload.STREAM_CODEC);

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

		ServerPlayNetworking.registerGlobalReceiver(ShootPayload.TYPE, (payload, context) -> {
			context.server().execute(() -> {
				ServerPlayer player = context.player();
				ItemStack stack = player.getItemInHand(InteractionHand.MAIN_HAND);
				if (stack.getItem() instanceof GunItem gun) {
					gun.shoot(player.level(), player, InteractionHand.MAIN_HAND);
				}
			});
		});

		ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
			ServerPlayNetworking.send(handler.player, new CrosshairStatePayload(ServerConfig.disableCrosshair));
		});

		ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
			ServerPlayNetworking.send(handler.player, new NameTagsStatePayload(ServerConfig.disableNameTags));
		});

		ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
			ServerPlayNetworking.send(handler.player, new ReloadFromInventoryStatePayload(ServerConfig.reloadFromInventory));
		});

		ModCommands.register();
		sounds.load();
		ModItems.initialize();
	}

	public static void sync(MinecraftServer server) {
		CrosshairStatePayload payload = new CrosshairStatePayload(ServerConfig.disableCrosshair);
		for (ServerPlayer player : server.getPlayerList().getPlayers()) {
			ServerPlayNetworking.send(player, payload);
		}
		NameTagsStatePayload ng = new NameTagsStatePayload(ServerConfig.disableNameTags);
		for (ServerPlayer player : server.getPlayerList().getPlayers()) {
			ServerPlayNetworking.send(player, ng);
		}
		ReloadFromInventoryStatePayload rfi = new ReloadFromInventoryStatePayload(ServerConfig.reloadFromInventory);
		for (ServerPlayer player : server.getPlayerList().getPlayers()) {
			ServerPlayNetworking.send(player, rfi);
		}
	}


	public static Identifier id(String path) {
		return Identifier.fromNamespaceAndPath(MOD_ID, path);
	}

	@Override
	public void onInitializeClient() {
		ClientConfig.load();
		ConditionalItemModelProperties.ID_MAPPER.put(Identifier.fromNamespaceAndPath(Weapons.MOD_ID, "has_magazine"), HasMagazineProperty.CODEC);
		ClientPlayNetworking.registerGlobalReceiver(CrosshairStatePayload.TYPE, (payload, context) -> {context.client().execute(() -> {Weapons.isCrosshairHidden = payload.hideCrosshair();});});
		ClientPlayConnectionEvents.DISCONNECT.register((handler, client) -> {Weapons.isCrosshairHidden = false;});
		ClientPlayNetworking.registerGlobalReceiver(NameTagsStatePayload.TYPE, (payload, context) -> {context.client().execute(() -> {Weapons.areNametagsHidden = payload.hideNameTags();});});
		ClientPlayConnectionEvents.DISCONNECT.register((handler, client) -> {Weapons.areNametagsHidden = false;});
		ClientPlayNetworking.registerGlobalReceiver(ReloadFromInventoryStatePayload.TYPE, (payload, context) -> {context.client().execute(() -> {Weapons.canReloadFromInventory = payload.rfi();});});
		ClientPlayConnectionEvents.DISCONNECT.register((handler, client) -> {Weapons.canReloadFromInventory = false;});
		eventManager = new EventManager();
		eventManager.init();
		zoom = new Zoom();
		EntityRendererRegistry.register(ModEntities.GRENADE, ThrownItemRenderer::new);
		EntityRendererRegistry.register(ModEntities.BULLET, ThrownItemRenderer::new);
	}
}
