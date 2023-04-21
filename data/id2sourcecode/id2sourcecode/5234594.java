    public void update() throws UpdateException {
        File file = getFile(filename);
        if (!file.exists()) {
            return;
        }
        if (Platform.OS_MACOSX.equals(Platform.getOS())) {
            write(file, read(file).replaceAll("&#xD;", "\n"));
        } else {
            write(file, read(file).replaceAll("&#xD;", ""));
        }
    }
