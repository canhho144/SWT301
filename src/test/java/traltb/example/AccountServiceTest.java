package traltb.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

class AccountServiceTest {

    private AccountService service;

    @BeforeEach
    void setup() {
        // Arrange chung cho toàn bộ các ca kiểm thử
        service = new AccountService();
    }

    // =========================================================================
    // 1. KIỂM THỬ PHƯƠNG THỨC isValidEmail
    // =========================================================================

    @ParameterizedTest(name = "Email hợp lệ: {0}")
    @ValueSource(strings = {
            "john@example.com",
            "alice.b@mail.co.uk",
            "carol_99@domain.io"
    })
    @DisplayName("isValidEmail trả về true với email đúng định dạng")
    void isValidEmail_ValidEmails_ReturnsTrue(String email) {
        // Arrange: tham số email do JUnit inject từ @ValueSource

        // Act
        boolean result = service.isValidEmail(email);

        // Assert
        assertTrue(result, () -> "Email " + email + " phải hợp lệ");
    }

    @ParameterizedTest(name = "Email không hợp lệ: \"{0}\"")
    @CsvSource(value = {
            "bobmail.com",       // Thiếu ký tự @
            "missing@dot",       // Thiếu phần mở rộng tên miền (.domain)
            "@nodomain.com",     // Thiếu phần định danh tài khoản trước @
            "'   '",             // Chỉ chứa khoảng trắng
            "NULL"               // Giá trị null
    }, nullValues = "NULL")
    @DisplayName("isValidEmail trả về false với email sai định dạng hoặc null")
    void isValidEmail_InvalidEmails_ReturnsFalse(String email) {
        // Arrange: tham số email do JUnit inject từ @CsvSource

        // Act
        boolean result = service.isValidEmail(email);

        // Assert
        assertFalse(result, () -> "Email \"" + email + "\" không được phép hợp lệ");
    }

    // =========================================================================
    // 2. KIỂM THỬ THAM SỐ HÓA registerAccount TỪ FILE CSV
    // =========================================================================

    @ParameterizedTest(name = "Row {index}: [{0}, {1}, {2}] -> {3}")
    @CsvFileSource(resources = "/test-data.csv", numLinesToSkip = 1)
    @DisplayName("registerAccount kiểm thử với bộ dữ liệu từ test-data.csv")
    void registerAccount_FromCsv(String username, String password, String email, boolean expected) {
        // Arrange: bộ dữ liệu được inject tự động từ test-data.csv

        // Act
        boolean actual = service.registerAccount(username, password, email);

        // Assert
        assertEquals(expected, actual,
                () -> String.format("Bộ dữ liệu (%s, %s, %s) phải trả về %s", username, password, email, expected));
    }

    // =========================================================================
    // 3. KIỂM THỬ CÁC TRƯỜNG HỢP BIÊN (EDGE CASES) & GIÁ TRỊ NULL
    // =========================================================================

    @Test
    @DisplayName("registerAccount: password đúng 6 ký tự (biên dưới) -> trả về false")
    void registerAccount_PasswordExactly6_ReturnsFalse() {
        // Arrange
        String username = "bob";
        String password = "abcdef"; // Độ dài đúng 6 ký tự (yêu cầu > 6)
        String email = "bob@mail.com";

        // Act
        boolean actual = service.registerAccount(username, password, email);

        // Assert
        assertFalse(actual, "Mật khẩu có độ dài 6 ký tự phải không hợp lệ (cần > 6 ký tự)");
    }

    @Test
    @DisplayName("registerAccount: password đúng 7 ký tự (biên trên hợp lệ) -> trả về true")
    void registerAccount_PasswordExactly7_ReturnsTrue() {
        // Arrange
        String username = "bob";
        String password = "abcdefg"; // Độ dài đúng 7 ký tự (> 6)
        String email = "bob@mail.com";

        // Act
        boolean actual = service.registerAccount(username, password, email);

        // Assert
        assertTrue(actual, "Mật khẩu có độ dài 7 ký tự phải hợp lệ");
    }

    @Test
    @DisplayName("registerAccount: tất cả các tham số đều là null -> trả về false")
    void registerAccount_AllNull_ReturnsFalse() {
        // Arrange: tất cả giá trị đều là null

        // Act
        boolean actual = service.registerAccount(null, null, null);

        // Assert
        assertFalse(actual, "Khi tất cả tham số là null phải trả về false");
    }

    @Test
    @DisplayName("registerAccount: username chỉ chứa khoảng trắng -> trả về false")
    void registerAccount_BlankUsername_ReturnsFalse() {
        // Arrange
        String username = "    ";
        String password = "validPassword123";
        String email = "user@test.com";

        // Act
        boolean actual = service.registerAccount(username, password, email);

        // Assert
        assertFalse(actual, "Username để trắng không được phép hợp lệ");
    }
}
