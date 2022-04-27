package de.erdbeerbaerlp.dcintegration.forge.mixin;

import com.mojang.authlib.GameProfile;
import de.erdbeerbaerlp.dcintegration.common.storage.Configuration;
import de.erdbeerbaerlp.dcintegration.common.storage.PlayerLinkController;
import de.erdbeerbaerlp.dcintegration.common.util.Variables;
import de.erdbeerbaerlp.dcintegration.forge.util.Refl;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.minecraft.server.management.PlayerList;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.fml.server.ServerLifecycleHooks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.lang.reflect.Field;
import java.net.SocketAddress;


@Mixin(PlayerList.class)
public class MixinWhitelist {
    @Inject(method = "canPlayerLogin", at = @At("HEAD"), cancellable = true)
    private void canLogin(SocketAddress address, GameProfile profile, CallbackInfoReturnable<ITextComponent> cir) {
        if (Configuration.instance().linking.whitelistMode && ServerLifecycleHooks.getCurrentServer().isServerInOnlineMode()) {
            try {
                if (!PlayerLinkController.isPlayerLinked(profile.getId()) ) {
                    cir.setReturnValue(new StringTextComponent(Configuration.instance().localization.linking.notWhitelistedCode.replace("%code%",""+Variables.discord_instance.genLinkNumber(profile.getId()))));
                }
                else{
                    String discordId = PlayerLinkController.getDiscordFromPlayer(profile.getId());
                    Member m = Variables.discord_instance.getChannel().getGuild().getMemberById(discordId);
                    String[] roles = Configuration.instance().linking.requiredRoles;
                    for(int i = 0; i < roles.length; i++) {
                        String r = roles[i];
                        for (Role role : m.getRoles()){
                            if(role.getId().equals(r)) if(i == roles.length-1) return;
                        }
                    }
                    cir.setReturnValue(new StringTextComponent((String) Refl.getLocalizationField().get(Configuration.instance().localization.linking)));
                }
            } catch (IllegalStateException e) {
                cir.setReturnValue(new StringTextComponent("Please check " + Variables.discordDataDir + "LinkedPlayers.json\n\n" + e.toString()));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }
}
