    public static String loadTextFile(String filename) {
        try {
            InputStream in = new FileInputStream(filename);
            ByteArrayOutputStream buf = new ByteArrayOutputStream();
            byte[] arr = new byte[1000];
            int size;
            while ((size = in.read(arr)) != -1) buf.write(arr, 0, size);
            return buf.toString();
        } catch (IOException e) {
            return null;
        }
    }
