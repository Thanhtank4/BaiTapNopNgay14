import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Base64;

public class Main {

    public static void main(String[] args) {
        // Thông tin kết nối
        String url = "jdbc:mysql://localhost:3306/";
        String databaseName = "mydb";
        String username = "root";
        String password = "123456789";

        // Khởi tạo giao diện đăng nhập
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new LoginFrame(url, databaseName, username, password);
            }
        });
    }
}

class LoginFrame extends JFrame {

    private JTextField usernameField;
    private JPasswordField passwordField;
    private String url;
    private String databaseName;
    private String dbUsername;
    private String dbPassword;

    public LoginFrame(String url, String databaseName, String username, String password) {
        super("Login");

        this.url = url;
        this.databaseName = databaseName;
        this.dbUsername = username;
        this.dbPassword = password;

        // Tạo các thành phần giao diện
        JLabel usernameLabel = new JLabel("Username:");
        usernameField = new JTextField(20);
        JLabel passwordLabel = new JLabel("Password:");
        passwordField = new JPasswordField(20);
        JButton loginButton = new JButton("Đăng nhập");
        JButton registerButton = new JButton("Đăng kí");

        // Thiết lập sự kiện cho nút đăng nhập
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                login();
            }
        });

        // Thiết lập sự kiện cho nút đăng ký
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                register();
            }
        });

        // Tạo panel cho các thành phần
        JPanel panel = new JPanel(new GridLayout(4, 2, 4, 5));
        JPanel usernamePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JPanel passwordPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JPanel buttonPanel1 = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JPanel buttonPanel2 = new JPanel(new FlowLayout(FlowLayout.CENTER));

        usernamePanel.add(usernameLabel);
        usernamePanel.add(usernameField);
        passwordPanel.add(passwordLabel);
        passwordPanel.add(passwordField);
        buttonPanel1.add(loginButton);
        buttonPanel2.add(registerButton);
        panel.add(usernamePanel);
        panel.add(passwordPanel);
        panel.add(buttonPanel1);
        panel.add(buttonPanel2);

        // Thiết lập layout cho frame
        setLayout(new BorderLayout());
        add(panel, BorderLayout.CENTER);

        // Cài đặt kích thước và hiển thị frame
        setSize(300, 200);
        setLocationRelativeTo(null); // Hiển thị frame ở giữa màn hình
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    // Phương thức xử lý đăng nhập
    private void login() {
        String inputUsername = usernameField.getText();
        String inputPassword = new String(passwordField.getPassword());

        // Mã hóa thông tin người dùng và mật khẩu
        String encodedUsername = encodeBase64(inputUsername);
        String encodedPassword = encodeBase64(inputPassword);

        // Kết nối và kiểm tra thông tin từ cơ sở dữ liệu
        try (Connection conn = DriverManager.getConnection(url + databaseName, dbUsername, dbPassword)) {
            // Kiểm tra thông tin người dùng từ bảng "users"
            try (PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM users WHERE username = ? AND password = ?")) {
                pstmt.setString(1, encodedUsername);
                pstmt.setString(2, encodedPassword);
                ResultSet rs = pstmt.executeQuery();
                if (rs.next()) {
                    JOptionPane.showMessageDialog(this, "Đăng nhập thành công! ❤️ I LOVE YOU ❤️", "Thông báo", JOptionPane.INFORMATION_MESSAGE);

                    // Reset các trường dữ liệu
                    usernameField.setText("");
                    passwordField.setText("");
                } else {
                    JOptionPane.showMessageDialog(this, "Tên người dùng hoặc mật khẩu không đúng.", "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Lỗi: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Phương thức xử lý đăng ký
    private void register() {
        String inputUsername = usernameField.getText();
        String inputPassword = new String(passwordField.getPassword());

        // Mã hóa thông tin người dùng và mật khẩu
        String encodedUsername = encodeBase64(inputUsername);
        String encodedPassword = encodeBase64(inputPassword);

        // Kết nối và thêm thông tin vào cơ sở dữ liệu
        try (Connection conn = DriverManager.getConnection(url + databaseName, dbUsername, dbPassword)) {
            // Thêm thông tin người dùng vào bảng "users"
            try (PreparedStatement pstmt = conn.prepareStatement("INSERT INTO users (username, password) VALUES (?, ?)")) {
                pstmt.setString(1, encodedUsername);
                pstmt.setString(2, encodedPassword);
                pstmt.executeUpdate();
                JOptionPane.showMessageDialog(this, "Đăng ký thành công! Vui lòng đăng nhập lại.");
                // Reset các trường dữ liệu
                usernameField.setText("");
                passwordField.setText("");
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Lỗi: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Phương thức mã hóa base64
    private static String encodeBase64(String input) {
        return Base64.getEncoder().encodeToString(input.getBytes());
    }
}
