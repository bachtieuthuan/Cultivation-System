package System.Cultivation.cultivationSystem;

import org.bukkit.entity.Player;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class LevelSystem {
    private final Map<Player, Integer> playerLevels = new HashMap<>();
    private final Map<Player, Integer> playerExperience = new HashMap<>();
    private final Map<Integer, LevelData> levelRequirements = new HashMap<>();

    public LevelSystem(JavaPlugin plugin) {
        loadLevelRequirements(plugin);
    }

    private void loadLevelRequirements(JavaPlugin plugin) {
        File levelsFile = new File(plugin.getDataFolder(), "level.yml");
        if (!levelsFile.exists()) {
            plugin.saveResource("level.yml", false); // Tạo file nếu chưa tồn tại
        }

        FileConfiguration config = YamlConfiguration.loadConfiguration(levelsFile);
        for (String key : config.getConfigurationSection("levels").getKeys(false)) {
            int level = Integer.parseInt(key); // Chỉ phân tích cú pháp khóa là số
            String levelName = config.getString("levels." + key + ".name");
            int expNeeded = config.getInt("levels." + key + ".exp_needed");
            levelRequirements.put(level, new LevelData(levelName, expNeeded));
        }
    }

    public void addExperience(Player player, int exp) {
        int currentExp = playerExperience.getOrDefault(player, 0);
        currentExp += exp;

        int currentLevel = getLevel(player);
        if (levelRequirements.containsKey(currentLevel + 1) && currentExp >= levelRequirements.get(currentLevel + 1).expNeeded) {
            currentExp -= levelRequirements.get(currentLevel + 1).expNeeded; // Reset EXP và tăng cấp độ.
            playerLevels.put(player, currentLevel + 1); // Tăng cấp độ.
            player.sendMessage("Chúc mừng! Bạn đã lên cấp!");
        }

        playerExperience.put(player, currentExp);
        player.sendMessage("Bạn đã nhận được " + exp + " EXP! Tổng EXP: " + currentExp);
    }

    public int getLevel(Player player) {
        return playerLevels.getOrDefault(player, 0);
    }

    public int getExperience(Player player) {
        return playerExperience.getOrDefault(player, 0);
    }

    public String getLevelName(int level) {
        return levelRequirements.get(level).name;
    }

    public int getNextLevelExp(int level) {
        return levelRequirements.containsKey(level + 1) ? levelRequirements.get(level + 1).expNeeded : -1; // Trả về -1 nếu không có cấp độ tiếp theo
    }

    private static class LevelData {
        String name;
        int expNeeded;

        LevelData(String name, int expNeeded) {
            this.name = name;
            this.expNeeded = expNeeded;
        }
    }

}
