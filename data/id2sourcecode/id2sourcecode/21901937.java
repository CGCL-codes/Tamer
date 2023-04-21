    public static FileIO open(String path, int flag, int mode) {
        ensurePath(path);
        boolean reading = (flag & O_RDONLY) != 0;
        boolean writing = (flag & O_WRONLY) != 0;
        boolean updating = (flag & O_RDWR) != 0;
        boolean creating = (flag & O_CREAT) != 0;
        boolean appending = (flag & O_APPEND) != 0;
        boolean truncating = (flag & O_TRUNC) != 0;
        boolean exclusive = (flag & O_EXCL) != 0;
        boolean sync = (flag & O_SYNC) != 0;
        File file = new RelativeFile(path);
        if (updating && writing) {
            throw Py.OSError(Errno.EINVAL, path);
        }
        if (!creating && !file.exists()) {
            throw Py.OSError(Errno.ENOENT, path);
        }
        if (!writing) {
            if (updating) {
                writing = true;
            } else {
                reading = true;
            }
        }
        if (truncating && !writing) {
            new FileIO(path, "w").close();
        }
        if (exclusive && creating) {
            try {
                if (!file.createNewFile()) {
                    throw Py.OSError(Errno.EEXIST, path);
                }
            } catch (IOException ioe) {
                throw Py.OSError(ioe);
            }
        }
        String fileIOMode = (reading ? "r" : "") + (!appending && writing ? "w" : "") + (appending && (writing || updating) ? "a" : "") + (updating ? "+" : "");
        if (sync && (writing || updating)) {
            try {
                return new FileIO(new RandomAccessFile(file, "rws").getChannel(), fileIOMode);
            } catch (FileNotFoundException fnfe) {
                throw Py.OSError(file.isDirectory() ? Errno.EISDIR : Errno.ENOENT, path);
            }
        }
        return new FileIO(path, fileIOMode);
    }
