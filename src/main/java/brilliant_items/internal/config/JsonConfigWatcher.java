package brilliant_items.internal.config;

import brilliant_items.BrilliantItems;
import net.minecraft.client.Minecraft;

import java.io.IOException;
import java.nio.file.*;

public class JsonConfigWatcher implements Runnable {
    /**
     * When an object implementing interface <code>Runnable</code> is used
     * to create a thread, starting the thread causes the object's
     * <code>run</code> method to be called in that separately executing
     * thread.
     * <p>
     * The general contract of the method <code>run</code> is that it may
     * take any action whatsoever.
     *
     * @see Thread#run()
     */
    @Override
    public void run() {
        try {
            WatchService watchService = FileSystems.getDefault().newWatchService();
            Path path = Paths.get(Minecraft.getMinecraft().gameDir.getPath(), "config");
            path.register(watchService, StandardWatchEventKinds.ENTRY_MODIFY);

            WatchKey key;
            while ((key = watchService.take()) != null) {
                for (WatchEvent<?> event : key.pollEvents())
                    if (event.context().toString().equals("brilliant_items_bindings.json")) {
                        BrilliantItems.LOGGER.info("Config was changed, re-importing");
                        JsonConfigManager.init();
                    }

                key.reset();
            }
        } catch (IOException | InterruptedException e) {
            BrilliantItems.LOGGER.error("Config watcher failed", e);
        }
    }
}
