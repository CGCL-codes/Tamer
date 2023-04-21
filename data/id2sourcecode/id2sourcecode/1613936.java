    public static boolean copyFile(byte[] data, File destinationFile) {
        boolean copySuccessfull = false;
        FileChannel destination = null;
        try {
            destination = new FileOutputStream(destinationFile).getChannel();
            long transferedBytes = destination.write(ByteBuffer.wrap(data));
            copySuccessfull = transferedBytes == data.length ? true : false;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (destination != null) {
                try {
                    destination.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return copySuccessfull;
    }
