    public static void copy(URL source, String destination) {
        InputStream in = null;
        FileOutputStream out = null;
        try {
            in = source.openStream();
            out = new FileOutputStream(destination);
            int c;
            while ((c = in.read()) != -1) out.write(c);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (in != null) try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (out != null) try {
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
