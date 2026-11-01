package meow.cats.advancedguns.config;

import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.server.permissions.Permission;

public class ModCommands {

    public static void register() {
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            dispatcher.register(Commands.literal("advanced-guns")
                    .requires(source -> source.checkPermission(
                            Identifier.fromNamespaceAndPath("advancedguns", "command.reload"),
                            true
                    ))

                    .then(Commands.literal("reload")
                            .executes(ctx -> {
                                try {
                                    Config.load();

                                    ctx.getSource().sendSuccess(() -> Component.literal("§a[Advanced Guns] Configuration reloaded successfully!"), true);
                                    return 1;
                                } catch (Exception e) {
                                    ctx.getSource().sendFailure(Component.literal("§c[Advanced Guns] Failed to reload config: " + e.getMessage()));
                                    e.printStackTrace();
                                    return 0;
                                }
                            })
                    )
            );
        });
    }
}
