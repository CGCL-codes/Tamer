    public SAWGraphicsModeServerSession(SAWServerSession session) {
        this.session = session;
        this.reader = new SAWGraphicsModeServerReader(this);
        this.writer = new SAWGraphicsModeServerWriter(this);
        this.reader.setWriter(writer);
    }
