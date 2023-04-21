    public static void writeContent(final FileObject file, final OutputStream outstr) throws IOException {
        final InputStream instr = file.getContent().getInputStream();
        try {
            final byte[] buffer = new byte[1024];
            while (true) {
                final int nread = instr.read(buffer);
                if (nread < 0) {
                    break;
                }
                outstr.write(buffer, 0, nread);
            }
        } finally {
            instr.close();
        }
    }
