    public HttpFileContent(File target, @Nullable String type, @Nullable String encoding) {
        Checks.notNull("target", target);
        this.target = target;
        this.type = type;
        this.encoding = encoding;
        try {
            this.open = new RandomAccessFile(target, "r");
            this.lock = open.getChannel().tryLock();
        } catch (IOException xp) {
        }
    }
