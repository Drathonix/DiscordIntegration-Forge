package de.erdbeerbaerlp.dcintegration.forge.mixin;

import dcshadow.com.moandjiezana.toml.TomlComment;
import de.erdbeerbaerlp.dcintegration.common.storage.Configuration;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(value = Configuration.Localization.Linking.class, priority = 1001)
public class MixinConfigurationLocalizationLinking {
    @TomlComment({"Sent to the user when they don't have the required discord role"})
    public String notWhitelistedNoRoleCode = "\u00a7cYou are not whitelisted.\nYou must have the whitelisted role on discord to join!";
}

