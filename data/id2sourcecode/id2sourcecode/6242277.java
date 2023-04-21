    public void writeFile(File file) {
        try {
            int ints = 3;
            int doubles = _a.length;
            int size = 4 * ints + 8 * doubles;
            ByteBuffer bb = ByteBuffer.allocateDirect(size);
            IntBuffer ib = bb.asIntBuffer();
            ib.put(_nx);
            ib.put(_ny);
            ib.put(_nz);
            bb.position(4 * ib.position());
            DoubleBuffer db = bb.asDoubleBuffer();
            db.put(_a);
            bb.rewind();
            FileChannel channel = new FileOutputStream(file).getChannel();
            channel.write(bb);
            channel.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
