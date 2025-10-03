package jp.s12kuma01.celeritasleafculling.compat;

import net.minecraft.client.Minecraft;

public class Compat {
    public static boolean isFancyLeaves() {
        return Minecraft.isFancyGraphicsEnabled();
    }
}
