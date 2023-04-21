    public static String openRessource(String filename) {
        try {
            DataInputStream dis = new DataInputStream(ClassLoader.getSystemResourceAsStream(filename));
            String s = dis.readUTF();
            dis.close();
            return s;
        } catch (Exception ex1) {
            try {
                StringBuffer buffer = new StringBuffer();
                URL url = new URL(filename);
                InputStream is = url.openStream();
                InputStreamReader isr = new InputStreamReader(is, "UTF8");
                Reader in = new BufferedReader(isr);
                int ch;
                while ((ch = in.read()) > -1) {
                    buffer.append((char) ch);
                }
                in.close();
                return buffer.toString();
            } catch (Exception ex2) {
                ex2.printStackTrace();
            }
        }
        return "";
    }
