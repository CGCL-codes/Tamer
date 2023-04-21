    private static byte[] loadBytes(String name) {
        FileInputStream in = null;
        try {
            in = new FileInputStream(name);
            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            int bytesread = 0;
            byte[] tbuff = new byte[512];
            while ((bytesread = in.read(tbuff)) != -1) {
                buffer.write(tbuff, 0, bytesread);
            }
            return buffer.toByteArray();
        } catch (IOException e) {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e2) {
                }
            }
            return null;
        }
    }
