import java.util.Base64;

public class GiaiMaHoa {

    public static void main(String[] args) {
        // Chuỗi đã được mã hóa
        String encodedString1 = "UGhhbnRoYW5odGFu"; // Chuỗi 1 cần giải mã
        String encodedString2 = "dGhhbmh0YW4="; // Chuỗi 2 cần giải mã

        // Giải mã hai chuỗi
        String decodedString1 = decodeBase64(encodedString1);
        String decodedString2 = decodeBase64(encodedString2);

        // Hiển thị chuỗi giải mã
        System.out.println("username : " + decodedString1);
        System.out.println("password : " + decodedString2);
    }

    // Phương thức giải mã base64
    private static String decodeBase64(String encodedString) {
        byte[] decodedBytes = Base64.getDecoder().decode(encodedString);
        return new String(decodedBytes);
    }
}
