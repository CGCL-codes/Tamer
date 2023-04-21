    public boolean savePartOfFile(byte[] data, int partNumber) {
        if (!(fileInfo instanceof model.ModelDownloadFile)) return false;
        ModelDownloadFile dFile = (ModelDownloadFile) fileInfo;
        java.io.File file = new java.io.File(dFile.getPath());
        if (!file.exists() || !file.canWrite()) {
            System.err.println("Error in FileHandler.saveData(): file does not exist or you don't have rights to write!");
            return false;
        }
        long offset = partNumber * Model.partSize;
        int partLength = Model.partSize;
        if (partNumber == (int) (fileInfo.getSize() / Model.partSize)) {
            partLength = (int) (fileInfo.getSize() - partNumber * Model.partSize);
        }
        ByteBuffer dataBuffer = ByteBuffer.allocate(partLength);
        dataBuffer.put(data, 0, partLength);
        dataBuffer.position(0);
        try {
            RandomAccessFile raf = new RandomAccessFile(file, "rw");
            FileChannel fc = raf.getChannel();
            fc.lock();
            fc.write(dataBuffer, offset);
            fc.close();
            raf.close();
        } catch (IOException e) {
            System.err.println("Error in FileHandler.saveData(): IOException occured!");
            return false;
        }
        return true;
    }
