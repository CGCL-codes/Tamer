    public void loadState(DataInput input) throws IOException {
        reset();
        isSupervisor = input.readBoolean();
        globalPagesEnabled = input.readBoolean();
        pagingDisabled = input.readBoolean();
        pageCacheEnabled = input.readBoolean();
        writeProtectUserPages = input.readBoolean();
        pageSizeExtensions = input.readBoolean();
        baseAddress = input.readInt();
        lastAddress = input.readInt();
        int len = input.readInt();
        pageFlags = new byte[len];
        input.readFully(pageFlags, 0, len);
        nonGlobalPages.clear();
        int count = input.readInt();
        int key, value;
        for (int i = 0; i < count; i++) {
            key = input.readInt();
            value = input.readInt();
            nonGlobalPages.put(new Integer(key), new Integer(value));
        }
        len = input.readInt();
        len = input.readInt();
        len = input.readInt();
        len = input.readInt();
        setSupervisor(isSupervisor);
    }
