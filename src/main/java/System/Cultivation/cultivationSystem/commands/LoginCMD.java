package System.Cultivation.cultivationSystem.commands;

import System.Cultivation.cultivationSystem.DatabaseManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class LoginCMD implements CommandExecutor{
    private final DatabaseManager databaseManager;

    public LoginCMD(DatabaseManager databaseManager) {
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

        if (args.length < 1) {
            sender.sendMessage("Sử dụng: /login <password>");
            return true;
        }

        String password = args[0];

        if (databaseManager.loginPlayer(username, password)) {
            sender.sendMessage("Đăng nhập thành công!");
            // Có thể thêm logic khác sau khi người chơi đã đăng nhập thành công.
        } else {
            sender.sendMessage("Đăng nhập thất bại. Tên người dùng hoặc mật khẩu không đúng.");
        }

        return true;
    }

}
