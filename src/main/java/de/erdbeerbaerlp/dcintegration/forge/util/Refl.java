package de.erdbeerbaerlp.dcintegration.forge.util;

import de.erdbeerbaerlp.dcintegration.common.storage.Configuration;

import java.lang.reflect.Field;

public class Refl {
    private static Field localization;
    public static Field getLocalizationField(){
        if(localization == null) {
            try {
                localization = Configuration.Localization.Linking.class.getDeclaredField("notWhitelistedNoRoleCode");
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            }
        }
        return localization;
    }
}
