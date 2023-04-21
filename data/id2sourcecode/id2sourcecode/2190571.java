    public static void fileRename(final File file, final String newPath, final Class clazz) {
        File newDestFile = new File(newPath);
        File lockFile = new File(newDestFile.getAbsolutePath() + ".lock");
        try {
            FileChannel channel = new RandomAccessFile(lockFile, "rw").getChannel();
            FileLock lock = channel.lock();
            try {
                synchronized (clazz) {
                    FileUtils.copyFileStreams(file, newDestFile);
                    newDestFile.setLastModified(file.lastModified());
                    String osName = System.getProperty("os.name");
                    if (!file.renameTo(newDestFile)) {
                        if (osName.toLowerCase().indexOf("windows") > -1 && newDestFile.exists()) {
                            if (!newDestFile.delete()) {
                                throw new AntdepoException("Unable to remove dest file on windows: " + newDestFile.getAbsolutePath());
                            }
                            if (!file.renameTo(newDestFile)) {
                                throw new AntdepoException("Unable to move file to dest file on windows: " + file + ", " + newDestFile.getAbsolutePath());
                            }
                        } else {
                            throw new AntdepoException("Unable to move file to dest file: " + file + ", " + newDestFile.getAbsolutePath());
                        }
                    }
                }
            } finally {
                lock.release();
                channel.close();
            }
        } catch (IOException e) {
            System.err.println("IOException: " + e.getMessage());
            e.printStackTrace(System.err);
            throw new AntdepoException("Unable to rename file: " + e.getMessage(), e);
        }
    }
