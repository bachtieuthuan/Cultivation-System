package System.Cultivation.cultivationSystem.events;

import System.Cultivation.cultivationSystem.DatabaseManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerActionListener implements Listener {
    private final DatabaseManager databaseManager;

    public PlayerActionListener(DatabaseManager databaseManager) {
        this.databaseManager = databaseManager;
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        String username = player.getName();

        // Kiểm tra xem người chơi đã đăng ký chưa
        if (!databaseManager.isPlayerRegistered(username)) {
            event.setCancelled(true); // Ngăn chặn di chuyển
        }
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        String username = player.getName();

        // Kiểm tra xem người chơi đã đăng ký chưa
        if (!databaseManager.isPlayerRegistered(username)) {
            event.setCancelled(true); // Ngăn chặn tương tác (đập)
        }
    }

}
