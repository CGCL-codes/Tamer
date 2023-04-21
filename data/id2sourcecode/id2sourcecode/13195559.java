    public RandomAccessFile readWriteFile(File file) throws IOException {
        try {
            PrivilegeManager.enablePrivilege("UniversalFileWrite");
            PrivilegeManager.enablePrivilege("UniversalFileRead");
        } catch (ForbiddenTargetException e) {
            throw new IOException("file read/write forbidden");
        }
        return super.readWriteFile(file);
    }
