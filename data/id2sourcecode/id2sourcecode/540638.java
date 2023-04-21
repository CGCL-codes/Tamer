    public static String getBinaryString(String hexChars) {
        int binaryToken = Integer.parseInt(hexChars, 16);
        String binaryValue = Integer.toBinaryString(binaryToken);
        int leading0s = 8 - binaryValue.length();
        while (leading0s != 0) {
            binaryValue = "0" + binaryValue;
            leading0s = leading0s - 1;
        }
        Log.level3("Binary To String: " + binaryValue);
        return binaryValue;
    }
