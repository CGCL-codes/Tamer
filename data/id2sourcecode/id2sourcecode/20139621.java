    public void writeFile(FileDataWare dataWare) throws WriteDriverException, ReadDriverException {
        String temp = dsf.getTempFile();
        writeToTemp(dataWare, new File(temp));
        try {
            FileChannel fcout = dbf.getWriteChannel();
            FileChannel fcin = new FileInputStream(temp).getChannel();
            DriverUtilities.copy(fcin, fcout);
        } catch (IOException e) {
            throw new WriteDriverException(getName(), e);
        }
    }
