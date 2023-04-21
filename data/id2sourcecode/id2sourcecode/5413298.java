    public static boolean copy(InputStream src, File dst) {
        FileOutputStream out = null;
        boolean ok = true;
        try {
            out = new FileOutputStream(dst);
            byte[] buffer = new byte[256];
            int read = src.read(buffer);
            while (read > 0) {
                out.write(buffer, 0, read);
                read = src.read(buffer);
            }
        } catch (FileNotFoundException e) {
            ok = false;
            e.printStackTrace();
        } catch (IOException e) {
            ok = false;
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
                ok = false;
            }
        }
        return ok;
    }
