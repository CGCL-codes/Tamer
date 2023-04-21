    public static ReadableByteChannel getChannel(String fileName) {
        String[] directories = (String[]) directoryPaths.toArray(new String[0]);
        for (int i = 0; i < directories.length; ++i) {
            try {
                FileChannel channel = new FileInputStream(directories[i] + fileName).getChannel();
                return channel;
            } catch (IOException e) {
            }
        }
        String[] archives = (String[]) archivePaths.toArray(new String[0]);
        for (int i = 0; i < archives.length; ++i) {
            try {
                ZipFile zipFile = new ZipFile(archives[i]);
                ZipEntry entry = zipFile.getEntry(fileName);
                ReadableByteChannel channel = Channels.newChannel(zipFile.getInputStream(entry));
                return channel;
            } catch (IOException e) {
            }
        }
        return Channels.newChannel(ClassLoader.getSystemResourceAsStream(fileName));
    }
