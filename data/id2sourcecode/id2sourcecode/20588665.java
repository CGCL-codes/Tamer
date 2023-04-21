    public void restoreFileTo(File directory) throws IOException {
        String nameAsStored = readUTF();
        long lengthAtStoreTime = readLong();
        File storedFile = new File(getAuxiliaryDirectory(), nameAsStored);
        File destination = new File(directory, nameAsStored);
        FileUtils.copyFile(storedFile, destination, lengthAtStoreTime);
    }
