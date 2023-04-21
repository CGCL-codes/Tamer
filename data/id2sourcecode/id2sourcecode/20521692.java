    public void add(WriterFormat.Factory writerFormatFactory) {
        if (this.writerFormatFactory != null) throw new RuntimeException("writerFormatFactory already set");
        this.writerFormatFactory = support(writerFormatFactory);
    }
