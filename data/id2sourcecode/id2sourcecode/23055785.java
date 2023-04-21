    public void genCode(Vector<String> reader_cpp, Vector<String> reader_h, Vector<String> writer_cpp, Vector<String> writer_h) {
        this.reader_cpp = reader_cpp;
        this.reader_h = reader_h;
        this.writer_cpp = writer_cpp;
        this.writer_h = writer_h;
        this.genReader();
        this.genWriter();
    }
