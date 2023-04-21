    public File(String name, int mode) throws FileException, FileNotFoundException {
        boolean read = false;
        boolean write = false;
        boolean append = false;
        switch(mode) {
            case OPEN_OVERWRITE:
                write = true;
                append = false;
                break;
            case OPEN_WRITE_APPEND:
                write = true;
                append = true;
                break;
        }
        this.init(name, read, write, append);
    }
