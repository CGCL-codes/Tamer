    public static void copy(InputStream in, OutputStream out, boolean closeInput, boolean closeOutput) throws IOException {
        BufferedInputStream input = new BufferedInputStream(in);
        try {
            byte buffer[] = new byte[BUF_SIZE];
            int nRead;
            while ((nRead = input.read(buffer)) > 0) out.write(buffer, 0, nRead);
        } finally {
            if (closeInput) in.close();
            if (closeOutput) out.close();
        }
    }
