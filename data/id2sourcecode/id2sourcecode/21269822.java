    private void init() {
        try {
            File d = this.file.getParentFile();
            UtilIO.mkdirs(d);
            FileOutputStream stream = new FileOutputStream(this.file, append);
            this.lock = stream.getChannel().tryLock();
            setWriter(new OutputStreamWriter(stream));
        } catch (IOException e) {
            throw new RuntimeException(RezIO.getCanonicalPath(this.file), e);
        }
    }
