    public void restoreFile(File destination) throws IOException {
        String nameAsStored = readUTF();
        long lengthAtStoreTime = readLong();
        File storedFile = new File(getAuxiliaryDirectory(), nameAsStored);
        FileUtils.copyFile(storedFile, destination, lengthAtStoreTime);
    }
