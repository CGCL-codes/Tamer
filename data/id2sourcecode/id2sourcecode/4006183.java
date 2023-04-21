    private void copyFileFromJarToSupportDir(File helpFile) throws IOException {
        if (!helpFile.exists()) {
            final ReadableByteChannel input = Channels.newChannel(ClassLoader.getSystemResourceAsStream(helpFile.getName()));
            final FileChannel output = new FileOutputStream(helpFile).getChannel();
            output.transferFrom(input, 0, 1000000L);
            output.close();
            input.close();
        }
    }
