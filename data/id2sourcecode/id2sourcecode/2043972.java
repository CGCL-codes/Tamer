    public static boolean copyInputStreamIntoFile(InputStream inputStream, File copyFile) {
        if (inputStream == null) {
            return false;
        }
        try {
            int bufferSize = BUFFER_SIZE;
            byte[] byteArray = new byte[bufferSize];
            int readByteNumber = -1;
            File parentDirectory = copyFile.getParentFile();
            if (parentDirectory.isDirectory() == false) {
                if (parentDirectory.mkdirs() == true) {
                }
            }
            FileOutputStream fileOutputStream = new FileOutputStream(copyFile);
            while ((readByteNumber = inputStream.read(byteArray)) != -1) {
                fileOutputStream.write(byteArray, 0, readByteNumber);
            }
            fileOutputStream.flush();
            fileOutputStream.close();
            inputStream.close();
        } catch (IOException ioException) {
            return false;
        }
        return true;
    }
