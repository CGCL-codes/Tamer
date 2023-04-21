    public SAWGraphicsModeClientSession(SAWClientSession session) {
        this.session = session;
        this.reader = new SAWGraphicsModeClientReader(this);
        this.writer = new SAWGraphicsModeClientWriter(this);
        this.reader.setWriter(writer);
        this.writer.setReader(reader);
        this.finished = true;
    }
