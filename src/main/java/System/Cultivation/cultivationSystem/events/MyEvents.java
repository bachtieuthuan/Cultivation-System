package System.Cultivation.cultivationSystem.events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class MyEvents implements Listener{

    @EventHandler // Đánh dấu phương thức xử lý sự kiện
    public void onPlayerJoin(PlayerJoinEvent event) {
        event.getPlayer().sendMessage("Chào mừng bạn đến với thế giới!"); // Gửi tin nhắn đến người chơi khi họ vào server
    }

}
