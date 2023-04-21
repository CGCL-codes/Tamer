    public static void writeInputStreamToOutputStream(InputStream in, boolean closeInputStream, OutputStream out, boolean closeOutputStream) throws IOException {
        try {
            BufferedInputStream bis = new BufferedInputStream(in);
            try {
                byte buf[] = new byte[1024 * 50];
                int read = -1;
                while ((read = bis.read(buf)) != -1) {
                    out.write(buf, 0, read);
                }
            } finally {
                if (closeInputStream) {
                    bis.close();
                }
            }
            out.flush();
        } finally {
            if (closeOutputStream) {
                out.close();
            }
        }
    }
