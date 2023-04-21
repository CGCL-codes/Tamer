    public static void main(String[] args) {
        try {
            InputStream inStream = new FileInputStream("C:\\TEMP\\EdifactTest\\Test03.txt");
            System.out.println(new String(convertInputStreamToByteArray(inStream)));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
