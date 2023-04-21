    public static void writeToStream(final File file, final OutputStream out) throws IOException {
        final FileInputStream fis = new FileInputStream(file);
        try {
            final BufferedInputStream bis = new BufferedInputStream(fis);
            try {
                final byte[] chars = new byte[BUFFER_SIZE];
                int readBytes = 0;
                while (readBytes >= 0) {
                    readBytes = bis.read(chars, 0, BUFFER_SIZE);
                    if (readBytes <= 0) {
                        break;
                    }
                    out.write(chars, 0, readBytes);
                }
            } finally {
                bis.close();
            }
        } finally {
            fis.close();
        }
    }
