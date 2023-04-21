    public static InputStream openStreamRessource(String fileName) {
        byte[] data;
        DataInputStream dis = new DataInputStream(ClassLoader.getSystemResourceAsStream(fileName));
        InputStream result = null;
        try {
            dis.readUTF();
            return dis;
        } catch (Exception e) {
            URL url = null;
            try {
                url = new URL(fileName);
                result = url.openStream();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
        return result;
    }
