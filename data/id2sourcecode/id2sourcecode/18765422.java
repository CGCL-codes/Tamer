    private AbstractFileNode getRelativeSubFile(int i) {
        i = this.getActiveFileNumber() + i;
        i %= this.subFilesCount();
        if (i < 0) {
            i += this.subFilesCount();
        }
        AbstractLogWriter.getInstance().write(this, "read sub file: " + this.getSubFile(i), AbstractLogWriter.VERBOSE);
        return this.getSubFile(i);
    }
