    public void rebuildFile(File basis, InputStream deltas, OutputStream out) throws IOException {
        File temp = File.createTempFile(".rdiff", null);
        temp.deleteOnExit();
        final RandomAccessFile f = new RandomAccessFile(temp, "w");
        RebuilderStream rs = new RebuilderStream();
        rs.setBasisFile(basis);
        rs.addListener(new RebuilderListener() {

            public void update(RebuilderEvent re) throws ListenerException {
                try {
                    f.seek(re.getOffset());
                    f.write(re.getData());
                } catch (IOException ioe) {
                    throw new ListenerException(ioe);
                }
            }
        });
        int command;
        long offset = 0;
        byte[] buf;
        boolean end = false;
        read: while ((command = deltas.read()) != -1) {
            try {
                switch(command) {
                    case OP_END:
                        end = true;
                        break read;
                    case OP_LITERAL_N1:
                        buf = new byte[(int) readInt(1, deltas)];
                        deltas.read(buf);
                        rs.update(new DataBlock(offset, buf));
                        offset += buf.length;
                        break;
                    case OP_LITERAL_N2:
                        buf = new byte[(int) readInt(2, deltas)];
                        deltas.read(buf);
                        rs.update(new DataBlock(offset, buf));
                        offset += buf.length;
                        break;
                    case OP_LITERAL_N4:
                        buf = new byte[(int) readInt(4, deltas)];
                        deltas.read(buf);
                        rs.update(new DataBlock(offset, buf));
                        offset += buf.length;
                        break;
                    case OP_COPY_N4_N4:
                        int oldOff = (int) readInt(4, deltas);
                        int bs = (int) readInt(4, deltas);
                        rs.update(new Offsets(oldOff, offset, bs));
                        offset += bs;
                        break;
                    default:
                        throw new IOException("Bad delta command: 0x" + Integer.toHexString(command));
                }
            } catch (ListenerException le) {
                throw (IOException) le.getCause();
            }
        }
        if (!end) throw new IOException("Didn't recieve RS_OP_END.");
        f.close();
        FileInputStream fin = new FileInputStream(temp);
        buf = new byte[CHUNK_SIZE];
        int len = 0;
        while ((len = fin.read(buf)) != -1) out.write(buf, 0, len);
    }
