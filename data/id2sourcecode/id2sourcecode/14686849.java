    public static void nuke(File file) throws IOException {
        long fileSize = file.length();
        byte[] crap = new byte[BUFFER_SIZE];
        Random random = new Random(crap.hashCode());
        FileOutputStream output = null;
        for (int c = 0; c < 3; c++) {
            try {
                output = new FileOutputStream(file);
                for (long written = 0; written < fileSize; written += crap.length) {
                    random.nextBytes(crap);
                    output.write(crap);
                }
            } finally {
                if (output != null) output.getChannel().force(false);
                close(output);
            }
        }
        if (!file.delete()) throw new IOException("Failed to delete nuked " + file);
    }
