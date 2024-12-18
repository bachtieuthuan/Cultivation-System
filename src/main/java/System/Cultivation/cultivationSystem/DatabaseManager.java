package System.Cultivation.cultivationSystem;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class DatabaseManager {
    private final String url = "jdbc:mysql://localhost:3306/cultivationsystemdb"; // Đường dẫn đến file cơ sở dữ liệu
    private final String userDB = "root"; // Tên người dùng
    private final String passwordDB = "Itxhnv@!2023"; // Mật khẩu
    private Connection connection;
    // Lưu trạng thái đăng nhập của người chơi
    private final Map<String, Boolean> loginStatus = new HashMap<>();

    public void connect() {
        try {
            connection = DriverManager.getConnection(url, userDB, passwordDB);
            System.out.println("Kết nối đến cơ sở dữ liệu thành công.");
            createTables(); // Tạo bảng nếu chưa tồn tại
        } catch (SQLException e) {
            System.err.println("Không thể kết nối đến cơ sở dữ liệu: " + e.getMessage());
        }
    }

    public void disconnect() {
        try {
            if (connection != null) {
                connection.close();
                System.out.println("Đã ngắt kết nối đến cơ sở dữ liệu.");
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi ngắt kết nối: " + e.getMessage());
        }
    }

    private void createTables() {
        //Người chơi
        String createPlayersTable = "CREATE TABLE IF NOT EXISTS players (" +
                "id INT PRIMARY KEY AUTO_INCREMENT," +
                "username VARCHAR(50) NOT NULL UNIQUE," +
                "password VARCHAR(20) NOT NULL," +
                "email VARCHAR(50)," +
                "created_at DATETIME DEFAULT CURRENT_TIMESTAMP" +
                ");";
        String createPlayerLevelsTable = "CREATE TABLE IF NOT EXISTS player_levels (" +
                "player_id INT," +
                "level INT NOT NULL," +
                "experience INT NOT NULL," +
                "FOREIGN KEY (player_id) REFERENCES players(id)" +
                ");";
        String createPlayerMoney = "CREATE TABLE IF NOT EXISTS player_money (" +
                "player_id INT," +
                "nganphieu INT," +
                "linhthach INT," +
                "FOREIGN KEY (player_id) REFERENCES players(id)" +
                ");";
        //Tông môn
        String createSect = "CREATE TABLE IF NOT EXISTS sect (" +
                "sect_id PRIMARY KEY AUTO_INCREMENT," +
                "sect_name VARCHAR(50) NOT NULL UNIQUE," +
                "sect_leader_id INT," +
                "sect_quantity INT" +
                "sect_slots INT," +
                "sect_level INT," +
                "sect_warpoints INT," +
                "sect_status VARCHAR(50)," +
                "create_at DATETIME DEFAULT CURRENT_TIMESTAMP," +
                "FOREIGN KEY (sect_leader_id) REFERENCES players(id)" +
                ");";
        String createSectMember = "CREATE TABLE IF NOT EXISTS sect_member (" +
                "member_id INT," +
                "sect_id INT," +
                "role ENUM('Tông Chủ', 'Phó Tông Chủ', 'Trưởng Lão', 'Đệ Tử')," +
                "joined_at DATETIME DEFAULT CURRENT_TIMESTAMP," +
                "FOREIGN KEY (member_id) REFERENCES players(id)," +
                "FOREIGN KEY (sect_id) REFERENCES sect(sect_id)" +
                ");";

        try (var stmt = connection.createStatement()) {
            //User
            stmt.execute(createPlayersTable);
            stmt.execute(createPlayerLevelsTable);
            stmt.execute(createPlayerMoney);
            //Sect
            stmt.execute(createSect);
            stmt.execute(createSectMember);
        } catch (SQLException e) {
            System.err.println("Lỗi khi tạo bảng: " + e.getMessage());
        }
    }

    public boolean registerPlayer(String username, String password) {
        String sql = "INSERT INTO players(username, password, created_at) VALUES(?, ?, ?)";

        try (var pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, username);
            pstmt.setString(2, hashPassword(password)); // Mã hóa mật khẩu
            pstmt.setString(3, getCurrentDate()); // Ngày tạo tài khoản
            pstmt.executeUpdate();
            return true; // Đăng ký thành công
        } catch (SQLException e) {
            System.err.println("Lỗi khi đăng ký: " + e.getMessage());
            return false; // Đăng ký thất bại
        }
    }

    public boolean loginPlayer(String username, String password) {
        String sql = "SELECT password FROM players WHERE username = ?";

        try (var pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, username);
            try (var rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    String storedPassword = rs.getString("password");
                    if (checkPassword(password, storedPassword)) {
                        loginStatus.put(username, true); // Đánh dấu là đã đăng nhập
                        return true; // Đăng nhập thành công
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi đăng nhập: " + e.getMessage());
        }
        return false; // Đăng nhập thất bại
    }

    public boolean isPlayerRegistered(String username) {
        String sql = "SELECT COUNT(*) FROM players WHERE username = ?";

        try (var pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, username);
            try (var rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0; // Trả về true nếu có ít nhất một bản ghi
                }
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi kiểm tra đăng ký: " + e.getMessage());
        }
        return false; // Trả về false nếu không tìm thấy
    }

    public boolean isPlayerLoggedIn(String username) {
        return loginStatus.getOrDefault(username, false); // Kiểm tra trạng thái đăng nhập
    }

    private String getCurrentDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(new Date()); // Trả về ngày giờ hiện tại
    }

    private String hashPassword(String password) {
        // Sử dụng một phương thức mã hóa như BCrypt hoặc SHA-256
        return password; // Thay thế bằng mã hóa thực tế
    }

    private boolean checkPassword(String plainPassword, String hashedPassword) {
        // Kiểm tra mật khẩu đã mã hóa với mật khẩu người dùng nhập vào
        return plainPassword.equals(hashedPassword); // Thay thế bằng kiểm tra thực tế
    }

    public Connection getConnection() {
        return connection;
    }

}
