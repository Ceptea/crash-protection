package ceptea.crashprotection;

import net.fabricmc.api.ModInitializer;


public class Main implements ModInitializer {


    @Override
    public void onInitialize() {
        Protection.INSTANCE.init();
    }
}