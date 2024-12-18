package System.Cultivation.cultivationSystem.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Heal implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Chỉ người chơi mới có thể sử dụng lệnh này.");
            return true;
        }

        Player player = (Player) sender;
        player.setHealth(20.0); // Đặt sức khỏe tối đa
        player.sendMessage("Bạn đã được hồi phục sức khỏe!");
        return true;
    }
}
