package System.Cultivation.cultivationSystem.commands;

import System.Cultivation.cultivationSystem.LevelSystem;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Info implements CommandExecutor{
    private LevelSystem levelSystem;

    public Info(LevelSystem levelSystem) {
        this.levelSystem = levelSystem;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("thongtin")) {
            if (sender instanceof Player player) {
                int level = levelSystem.getLevel(player);
                String levelName = levelSystem.getLevelName(level);
                int exp = levelSystem.getExperience(player);
                int nextLevelExp = levelSystem.getNextLevelExp(level); // Lấy yêu cầu EXP cho cấp độ tiếp theo

                player.sendMessage("=== Thông Tin Cấp Độ ===");
                player.sendMessage("Cấp độ của bạn: " + levelName + " (" + level + ")");
                if (nextLevelExp != -1) { // Nếu có cấp độ tiếp theo
                    player.sendMessage("Tiến trình EXP: " + exp + "/" + nextLevelExp); // Hiển thị yêu cầu EXP cho cấp tiếp theo.
                } else {
                    player.sendMessage("Bạn đã đạt đến cấp độ tối đa!");
                }
            } else {
                sender.sendMessage("Bạn phải là người chơi để sử dụng lệnh này.");
            }
            return true;
        }
        return false;
    }
}

