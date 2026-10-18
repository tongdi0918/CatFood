import java.nio.charset.StandardCharsets;

public class EncTest {
    public static void main(String[] args) throws Exception {
        String garbled = "宸插結蟬彐端觸闌十溅";
        byte[] gbkBytes = garbled.getBytes("GBK");
        System.out.print("GBK bytes: ");
        for (byte b : gbkBytes) System.out.printf("%02x ", b);
        System.out.println();
        System.out.println("UTF8读: [" + new String(gbkBytes, StandardCharsets.UTF_8) + "]");
        System.out.println("GB18030读: [" + new String(gbkBytes, "GB18030") + "]");

        String[] samples = {"已加入购物车", "加入失败", "库存不足，最多还能加入 95 件", "该商品已下架", "网络错误，请重试", "无法加入，请稍后重试"};
        for (String s : samples) {
            byte[] u8 = s.getBytes(StandardCharsets.UTF_8);
            System.out.println("[" + s + "] UTF8->GBK=" + new String(u8, "GBK"));
        }
    }
}
