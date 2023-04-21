    public static byte[] readContentsAsBytes(InputStream input) throws IOException {
        BufferedInputStream bufferedInputStream = null;
        try {
            final int BUF_SIZE = 8192;
            byte[] buf = new byte[BUF_SIZE];
            int read;
            int totalRead = 0;
            bufferedInputStream = new BufferedInputStream(input);
            while (totalRead < BUF_SIZE && (read = bufferedInputStream.read(buf, totalRead, BUF_SIZE - totalRead)) != -1) {
                totalRead += read;
            }
            if (totalRead < BUF_SIZE) {
                byte[] result = new byte[totalRead];
                System.arraycopy(buf, 0, result, 0, totalRead);
                return result;
            }
            ByteArrayOutputStream out = new ByteArrayOutputStream(BUF_SIZE * 2);
            out.write(buf);
            while ((read = bufferedInputStream.read(buf, 0, BUF_SIZE)) != -1) {
                out.write(buf, 0, read);
            }
            return out.toByteArray();
        } finally {
            try {
                if (bufferedInputStream != null) {
                    bufferedInputStream.close();
                }
            } catch (IOException e) {
                CodeGenPlugin.write(e);
            }
        }
    }
