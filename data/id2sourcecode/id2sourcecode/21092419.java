    public byte[] getPartOfFile(int partNumber) {
        byte[] data = new byte[Model.partSize];
        java.io.File file = new java.io.File(fileInfo.getPath());
        if (!file.exists() || !file.canRead() || (fileInfo.getSize() < partNumber * Model.partSize)) return null;
        long offset = partNumber * Model.partSize;
        int partLength = Model.partSize;
        if (partNumber == (int) (fileInfo.getSize() / Model.partSize)) {
            partLength = (int) (fileInfo.getSize() - partNumber * Model.partSize);
        }
        byte[] d = new byte[partLength];
        MappedByteBuffer dataBuffer = null;
        try {
            FileInputStream fis = new FileInputStream(file);
            FileChannel fc = fis.getChannel();
            if (!fc.isOpen()) System.err.println("Error in FileHandler.getPartOfFile(): channel not opened");
            dataBuffer = fc.map(MapMode.READ_ONLY, offset, partLength);
            fc.close();
            fis.close();
        } catch (IOException e) {
            System.err.println("Error in FileHandler.getPartOfFile(): IOException occured!");
        }
        if (dataBuffer != null) {
            dataBuffer.get(d);
            for (int i = 0; i < partLength; ++i) data[i] = d[i];
        }
        return data;
    }
