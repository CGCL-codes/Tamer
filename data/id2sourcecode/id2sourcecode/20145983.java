    public final void purge(final int[] msgnums) throws IOException {
        File newFile = new File(System.getProperty("java.io.tmpdir"), file.getName() + TEMP_FILE_EXTENSION);
        FileOutputStream newOut = new FileOutputStream(newFile);
        FileChannel newChannel = newOut.getChannel();
        MessageAppender appender = new MessageAppender(newChannel);
        synchronized (file) {
            loop: for (int i = 0; i < getMessagePositions().length; i++) {
                for (int j = 0; j < msgnums.length; j++) {
                    if (msgnums[j] == i) {
                        continue loop;
                    }
                }
                appender.appendMessage(getMessage(i));
            }
            newOut.close();
            close();
            File tempFile = new File(System.getProperty("java.io.tmpdir"), file.getName() + "." + System.currentTimeMillis());
            if (!renameTo(file, tempFile)) {
                throw new IOException("Unable to rename existing file");
            }
            tempFile.deleteOnExit();
            renameTo(newFile, file);
        }
    }
