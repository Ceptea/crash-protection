package ceptea.crashprotection;

import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class CrashProtectionInit implements ModInitializer {


    @Override
    public void onInitialize() {
        Protection.INSTANCE.init();
        File file = new File("protection.t");
        try {
            file.createNewFile();

        } catch (IOException e) {
            Scanner scanner = new Scanner(file.getName());
            while (scanner.hasNextLine()) {
                String ln = scanner.nextLine();

            }
        }
//        KeyBinding guiBind = KeyBindingHelper.registerKeyBinding(new KeyBinding(
//                "Configure!",
//                InputUtil.Type.KEYSYM,
//                GLFW.GLFW_KEY_SEMICOLON,
//                "Client Protection."
//        ));


    }
}