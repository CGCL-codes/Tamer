    private void processVirtualActualFile() throws IPCException {
        String suffix = Long.toString(System.currentTimeMillis());
        String actual = toActualName(suffix);
        if (null == actual) {
            actual = getVirtualName();
        } else {
            try {
                actual = Utils.readOrCreate(getVirtualName(), actual);
            } catch (IOException e) {
                String msg = "Error trying to read/write virtual file: " + getVirtualName();
                throw new IPCException(msg, e);
            }
        }
        setActualName(actual);
    }
