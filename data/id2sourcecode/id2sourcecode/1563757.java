    public static void main(String[] args) {
        try {
            InputStream inStream = new FileInputStream("C:\\Temp\\Test03.txt");
            System.out.println(new String(convertInputStreamToByteArray(inStream)));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
