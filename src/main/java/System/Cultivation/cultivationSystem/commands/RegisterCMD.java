package System.Cultivation.cultivationSystem.commands;

import System.Cultivation.cultivationSystem.DatabaseManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class RegisterCMD implements CommandExecutor{

    private final DatabaseManager databaseManager;

    public RegisterCMD(DatabaseManager databaseManager) {
        this.databaseManager = databaseManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Bạn phải là một người chơi để sử dụng lệnh này.");
            return true;
        }

        Player player = (Player) sender; // Lấy đối tượng Player
        String username = player.getName(); // Lấy tên người dùng từ Player
        if (args.length < 2) {
            sender.sendMessage("Sử dụng: /register <password> <password again>");
            return true;
        }

        String password = args[0];
        String email = args[1];

        if (databaseManager.registerPlayer(username, password)) {
            sender.sendMessage("Đăng ký thành công!");
        } else {
            sender.sendMessage("Đăng ký thất bại. Có thể tên người dùng đã tồn tại.");
        }
        return true;
    }
}
