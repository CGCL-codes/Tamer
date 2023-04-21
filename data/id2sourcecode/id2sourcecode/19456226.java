    public PathPermission(String pattern, int type, String readPermission, String writePermission) {
        super(pattern, type);
        mReadPermission = readPermission;
        mWritePermission = writePermission;
    }
