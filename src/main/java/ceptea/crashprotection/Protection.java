package ceptea.crashprotection;


import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.DisconnectedScreen;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.text.Text;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public enum Protection {
    INSTANCE;
    public static final Logger LOGGER = LoggerFactory.getLogger("Client Protection");

    public static MinecraftClient mc;

    public static double sinceLastTick = 0;

    public static double pps = 0;

    public static double timer = 0;

    public static double safetoReEnable = 0;

    public static boolean panicMode = false;



    public void init() {
        mc = MinecraftClient.getInstance();
        
        log("Client Protection, is starting.");

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (!inGame()) {
                return;
            }
            if (System.currentTimeMillis() - sinceLastTick > 10000) {
                mc.world.disconnect();
                mc.disconnect(new DisconnectedScreen(new TitleScreen(), Text.of("Client Protection"), Text.of("timed out.")));

            }
            if (mc.getCurrentFps() <= 40 && !Protection.panicMode) {
                panicMode = true;

            }
            if (System.currentTimeMillis() > timer) {
                timer = System.currentTimeMillis() + 1000;
                pps = 0;
            }
            if (System.currentTimeMillis() > safetoReEnable) {
                safetoReEnable = System.currentTimeMillis() + 5000;
                if (panicMode){

                    panicMode = false;

                }


            }

        });

    }

    public static boolean inGame() {
        return (mc.player != null && mc.world != null);
    }

    public static void log(String message) {
        LOGGER.info(message);
    }

    public static void send(String message) {
        if (inGame()) {
            mc.player.sendMessage(Text.of(String.format("§7[§dCrash Protection§7]§d %s", message)));

        } else {
            log(String.format("error. [%s]", message));
        }
    }
}
