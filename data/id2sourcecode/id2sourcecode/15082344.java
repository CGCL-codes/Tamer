    public void write(File file) throws IOException {
        file.delete();
        RandomAccessFile out = new RandomAccessFile(file, "rw");
        ByteBuffer bb = out.getChannel().map(MapMode.READ_WRITE, 0, 1024 * 64);
        bb.order(ByteOrder.nativeOrder());
        this.writeASCII(bb, "HEADER_START");
        for (HeaderID headerID : HeaderID.values()) {
            switch(headerID) {
                case rawdatafile:
                    if (this.rawdatafile != null) {
                        this.writeASCII(bb, headerID.toString());
                        this.writeASCII(bb, this.rawdatafile);
                    }
                    break;
                case source_name:
                    if (this.source_name != null) {
                        this.writeASCII(bb, headerID.toString());
                        this.writeASCII(bb, this.source_name);
                    }
                    break;
                case az_start:
                    if (!Double.isNaN(this.az_start)) {
                        this.writeASCII(bb, headerID.toString());
                        bb.putDouble(this.az_start);
                    }
                    break;
                case za_start:
                    if (!Double.isNaN(this.za_start)) {
                        this.writeASCII(bb, headerID.toString());
                        bb.putDouble(this.za_start);
                    }
                    break;
                case src_raj:
                    if (!Double.isNaN(this.src_raj)) {
                        this.writeASCII(bb, headerID.toString());
                        bb.putDouble(this.src_raj);
                    }
                    break;
                case src_dej:
                    if (!Double.isNaN(this.src_dej)) {
                        this.writeASCII(bb, headerID.toString());
                        bb.putDouble(this.src_dej);
                    }
                    break;
                case tstart:
                    if (!Double.isNaN(this.tstart)) {
                        this.writeASCII(bb, headerID.toString());
                        bb.putDouble(this.tstart);
                    }
                    break;
                case tsamp:
                    if (!Double.isNaN(this.tsamp)) {
                        this.writeASCII(bb, headerID.toString());
                        bb.putDouble(this.tsamp);
                    }
                    break;
                case period:
                    if (!Double.isNaN(this.period)) {
                        this.writeASCII(bb, headerID.toString());
                        bb.putDouble(this.period);
                    }
                    break;
                case fch1:
                    if (!Double.isNaN(this.fch1)) {
                        this.writeASCII(bb, headerID.toString());
                        bb.putDouble(this.fch1);
                    }
                    break;
                case foff:
                    if (!Double.isNaN(this.foff)) {
                        this.writeASCII(bb, headerID.toString());
                        bb.putDouble(this.foff);
                    }
                    break;
                case nchans:
                    if (this.nchans != -1) {
                        this.writeASCII(bb, headerID.toString());
                        bb.putInt(this.nchans);
                    }
                    break;
                case telescope_id:
                    if (this.telescope_id != -1) {
                        this.writeASCII(bb, headerID.toString());
                        bb.putInt(this.telescope_id);
                    }
                    break;
                case machine_id:
                    if (this.machine_id != -1) {
                        this.writeASCII(bb, headerID.toString());
                        bb.putInt(this.machine_id);
                    }
                    break;
                case data_type:
                    if (this.data_type != -1) {
                        this.writeASCII(bb, headerID.toString());
                        bb.putInt(this.data_type);
                    }
                    break;
                case nbeams:
                    if (this.nbeams != -1) {
                        this.writeASCII(bb, headerID.toString());
                        bb.putInt(this.nbeams);
                    }
                    break;
                case nbits:
                    if (this.machine_id != -1) {
                        this.writeASCII(bb, headerID.toString());
                        bb.putInt(this.nbits);
                    }
                    break;
                case barycentric:
                    if (this.barycentric != -1) {
                        this.writeASCII(bb, headerID.toString());
                        bb.putInt(this.barycentric);
                    }
                    break;
                case pulsarcentric:
                    if (this.pulsarcentric != -1) {
                        this.writeASCII(bb, headerID.toString());
                        bb.putInt(this.pulsarcentric);
                    }
                    break;
                case nbins:
                    if (this.nbins != -1) {
                        this.writeASCII(bb, headerID.toString());
                        bb.putInt(this.nbins);
                    }
                    break;
                case nsamples:
                    break;
                case nifs:
                    if (this.nifs != -1) {
                        this.writeASCII(bb, headerID.toString());
                        bb.putInt(this.nifs);
                    }
                    break;
                case refdm:
                    if (!Double.isNaN(this.refdm)) {
                        this.writeASCII(bb, headerID.toString());
                        bb.putDouble(this.refdm);
                    }
                    break;
            }
        }
        this.writeASCII(bb, "HEADER_END");
        out.getChannel().truncate(bb.position());
        this.setHeaderLength(bb.position());
        out.close();
        this.written = true;
    }
