    public void copy(File from, File to, boolean append) throws IOException {
        if (logWriter != null) logWriter.println("copy(" + from + ", " + to + ", " + append + ")");
        OutputStream out = openOutputStream(to, append);
        try {
            InputStream in = openInputStream(from);
            try {
                if (from.length() > 65536L) {
                    byte[] buff = new byte[65536];
                    int len;
                    while ((len = in.read(buff)) > -1) if (len != 0) out.write(buff, 0, len);
                } else {
                    byte[] buff = new byte[(int) from.length()];
                    int len = in.read(buff);
                    out.write(buff, 0, len);
                }
            } finally {
                try {
                    in.close();
                } catch (Exception e) {
                }
            }
        } finally {
            try {
                out.close();
            } catch (Exception e) {
            }
        }
    }
