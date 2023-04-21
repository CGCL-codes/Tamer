    public void genCode(Vector<String> reader_v, Vector<String> reader_engine_v, Vector<String> writer_v, Vector<String> writer_engine_v, Vector<String> shmem_v, Vector<String> acm_v, Vector<String> mux_v) {
        this.reader_v = reader_v;
        this.reader_engine_v = reader_engine_v;
        this.writer_v = writer_v;
        this.writer_engine_v = writer_engine_v;
        this.shmem_v = shmem_v;
        this.acm_v = acm_v;
        this.mux_v = mux_v;
        this.genReader();
        this.genWriter();
        this.genShMem();
        this.genACM();
        this.genMux();
    }
