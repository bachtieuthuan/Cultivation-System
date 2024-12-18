package System.Cultivation.cultivationSystem;

import System.Cultivation.cultivationSystem.commands.*;
import System.Cultivation.cultivationSystem.events.*;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import java.io.File;

public final class CultivationSystem extends JavaPlugin {
    private LevelSystem levelSystem;
    private DatabaseManager databaseManager;

    @Override
    public void onEnable() {
        // Plugin startup logic
        databaseManager = new DatabaseManager();
        databaseManager.connect(); // Kết nối đến cơ sở dữ liệu
        levelSystem = new LevelSystem(this);
        // Tạo file cấu hình nếu chưa tồn tại
        saveDefaultConfig();

        // Tạo thư mục cho dữ liệu nếu chưa tồn tại
        File dataFolder = new File(getDataFolder(), "messages");
        if (!dataFolder.exists()) {
            dataFolder.mkdirs();
        }


        // Đăng ký lớp commands
        this.getCommand("hoiphuc").setExecutor(new Heal()); // Đăng ký lệnh /hoiphuc
        this.getCommand("thongtin").setExecutor(new Info(this.levelSystem));
        this.getCommand("register").setExecutor(new RegisterCMD(this.databaseManager));
        this.getCommand("login").setExecutor(new LoginCMD(this.databaseManager));

        // Đăng ký lớp lắng nghe sự kiện
        this.getServer().getPluginManager().registerEvents(new PlayerActionListener(databaseManager), this);
        this.getServer().getPluginManager().registerEvents(new PlayerJoinListener(databaseManager), this);
        this.getServer().getPluginManager().registerEvents(new MyEvents(), this);



        // Bắt đầu nhiệm vụ cộng EXP mỗi 15 giây
        new BukkitRunnable() {
            @Override
            public void run() {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    levelSystem.addExperience(player, calculateExperienceGain(player)); // Cộng EXP dựa trên cấp độ.
                }
            }
        }.runTaskTimer(this, 0, 300); // Mỗi 15 giây

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    private int calculateExperienceGain(Player player) {
        int level = levelSystem.getLevel(player);

        // Tăng lượng EXP nhận được dựa trên cấp độ.
        return (level + 1) * 10; // Ví dụ: Cấp độ càng cao thì EXP nhận càng nhiều.
    }

}
