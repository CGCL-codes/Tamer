    String open(Interp interp, String fileName, int modeFlags) throws IOException, TclException {
        mode = modeFlags;
        File fileObj = FileUtil.getNewFileObj(interp, fileName);
        if (((modeFlags & TclIO.CREAT) != 0) && ((modeFlags & TclIO.EXCL) != 0) && fileObj.exists()) {
            throw new TclException(interp, "couldn't open \"" + fileName + "\": file exists");
        }
        if (((modeFlags & TclIO.CREAT) != 0) && !fileObj.exists()) {
            file = new RandomAccessFile(fileObj, "rw");
            file.close();
        }
        if ((modeFlags & TclIO.RDWR) != 0) {
            checkFileExists(interp, fileObj);
            checkReadWritePerm(interp, fileObj, 0);
            if (fileObj.isDirectory()) {
                throw new TclException(interp, "couldn't open \"" + fileName + "\": illegal operation on a directory");
            }
            file = new RandomAccessFile(fileObj, "rw");
        } else if ((modeFlags & TclIO.RDONLY) != 0) {
            checkFileExists(interp, fileObj);
            checkReadWritePerm(interp, fileObj, -1);
            if (fileObj.isDirectory()) {
                throw new TclException(interp, "couldn't open \"" + fileName + "\": illegal operation on a directory");
            }
            file = new RandomAccessFile(fileObj, "r");
        } else if ((modeFlags & TclIO.WRONLY) != 0) {
            checkFileExists(interp, fileObj);
            checkReadWritePerm(interp, fileObj, 1);
            if (fileObj.isDirectory()) {
                throw new TclException(interp, "couldn't open \"" + fileName + "\": illegal operation on a directory");
            }
            if (!fileObj.canRead()) {
                throw new TclException(interp, "Java IO limitation: Cannot open a file " + "that has only write permissions set.");
            }
            file = new RandomAccessFile(fileObj, "rw");
        } else {
            throw new TclRuntimeError("FileChannel.java: invalid mode value");
        }
        if ((modeFlags & TclIO.APPEND) != 0) {
            file.seek(file.length());
        }
        if ((modeFlags & TclIO.TRUNC) != 0) {
            java.nio.channels.FileChannel chan = file.getChannel();
            chan.truncate(0);
        }
        String fName = TclIO.getNextDescriptor(interp, "file");
        setChanName(fName);
        return fName;
    }
