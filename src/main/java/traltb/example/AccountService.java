package traltb.example;

import java.util.regex.Pattern;

public class AccountService {
    // Biểu thức Regex kiểm tra định dạng email theo chuẩn RFC
    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");

    /**
     * Kiểm tra tính hợp lệ của email
     */
    public boolean isValidEmail(String email) {
        if (email == null || email.isBlank()) {
            return false;
        }
        return EMAIL_PATTERN.matcher(email.trim()).matches();
    }

    /**
     * Đăng ký tài khoản theo các quy tắc nghiệp vụ:
     * - username: không null, không rỗng/chỉ chứa khoảng trắng
     * - password: không null, độ dài > 6 ký tự
     * - email: đúng định dạng RFC
     */
    public boolean registerAccount(String username, String password, String email) {
        if (username == null || username.isBlank()) {
            return false;
        }
        if (password == null || password.length() <= 6) {
            return false;
        }
        if (!isValidEmail(email)) {
            return false;
        }
        // Giả lập lưu dữ liệu vào Database thành công
        return true;
    }
}
