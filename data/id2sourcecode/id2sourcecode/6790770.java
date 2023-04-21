    public static void writeContents(File file, byte[] contents) throws IOException {
        if ((file != null) && (contents != null)) {
            if (file.createNewFile()) {
                FileOutputStream aFileStream = new FileOutputStream(file);
                BufferedOutputStream aBufferedStream = new BufferedOutputStream(aFileStream);
                for (int i = 0; i < contents.length; i++) {
                    aBufferedStream.write(contents[i]);
                }
                aBufferedStream.flush();
                aBufferedStream.close();
            } else {
                throw new IOException("Can't writeContents '" + file + "' a file already exists at that path");
            }
        }
    }
