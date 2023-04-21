    public void readFile(File file) {
        try {
            FileChannel channel = new FileInputStream(file).getChannel();
            MappedByteBuffer bb = channel.map(FileChannel.MapMode.READ_ONLY, 0, channel.size());
            IntBuffer ib = bb.asIntBuffer();
            _nx = ib.get();
            _ny = ib.get();
            _nz = ib.get();
            bb.position(4 * ib.position());
            DoubleBuffer db = bb.asDoubleBuffer();
            if (_a == null || _a.length != _nx * _ny * _nz) _a = new double[_nx * _ny * _nz];
            db.get(_a);
            channel.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
