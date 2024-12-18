package System.Cultivation.cultivationSystem.events;

import System.Cultivation.cultivationSystem.DatabaseManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener{
    private final DatabaseManager databaseManager;

    public PlayerJoinListener(DatabaseManager databaseManager) {
        this.databaseManager = databaseManager;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        String username = player.getName();

        // Kiểm tra xem người chơi đã đăng ký chưa
        if (!databaseManager.isPlayerRegistered(username)) {
            player.sendMessage("Bạn chưa đăng ký. Vui lòng sử dụng /register <password> <email> để đăng ký.");
            // Có thể thêm logic để ngăn chặn người chơi tiếp tục vào máy chủ cho đến khi họ đăng ký.
        } else if (!databaseManager.isPlayerLoggedIn(username)) {
            player.sendMessage("Bạn đã đăng ký nhưng chưa đăng nhập. Vui lòng sử dụng /login <password> để đăng nhập.");
            // Có thể thêm logic để ngăn chặn người chơi tiếp tục vào máy chủ cho đến khi họ đăng nhập.
        } else {
            player.sendMessage("Chào mừng trở lại, " + username + "!");
        }
    }

}
