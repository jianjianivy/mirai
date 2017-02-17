public class DecimalToBinary{
    // Decimal⇒Binary
    public static String convert10To2(int decimal) {
        List<Integer> binaryList = new ArrayList<Integer>();

        int mod = 0;
        int divide = decimal;
        do {
            mod = divide % 2;
            binaryList.add(mod);
            divide = divide / 2;
        } while (divide != 0);

        StringBuffer sb = new StringBuffer();
        for (int i = binaryList.size() - 1; i >= 0; i--) {
            sb.append(binaryList.get(i));
        }

        return sb.toString();
    }
}
