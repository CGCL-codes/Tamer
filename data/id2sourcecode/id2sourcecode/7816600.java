    private File downloadSoundFile(String text) {
        File soundFile = null;
        InputStream reader = null;
        OutputStream writer = null;
        URLConnection connection = establishConnection(text);
        try {
            reader = new BufferedInputStream(connection.getInputStream(), FileUtil.DEFAULT_BUFFER_SIZE_IN_BYTES);
            writer = context.openFileOutput(text, Context.MODE_WORLD_READABLE);
            int readByte = 0;
            synchronized (buffer) {
                while ((readByte = reader.read(buffer)) != -1) {
                    writer.write(buffer, 0, readByte);
                }
                Arrays.fill(buffer, (byte) 0);
            }
        } catch (IOException ioe) {
            throw new OperationFailedException("downloading data from connection", ioe);
        } finally {
            FileUtil.closeInputStream(reader);
            FileUtil.closeOutputStream(writer);
        }
        String path = context.getFilesDir().getPath() + File.separator + text;
        soundFile = new File(path);
        return soundFile;
    }
