    public static void main(String[] args) throws IOException {
        if (args.length != 1) {
            System.err.println("USAGE: TraceFormatter trace_file");
            System.exit(2);
        }
        FileChannel fc = new FileInputStream(args[0]).getChannel();
        while (true) {
            ByteBuffer bb = ByteBuffer.allocate(41);
            fc.read(bb);
            bb.flip();
            byte app = bb.get();
            long time = bb.getLong();
            long id = bb.getLong();
            int cxid = bb.getInt();
            long zxid = bb.getLong();
            int txnType = bb.getInt();
            int type = bb.getInt();
            int len = bb.getInt();
            bb = ByteBuffer.allocate(len);
            fc.read(bb);
            bb.flip();
            String path = "n/a";
            if (bb.remaining() > 0) {
                if (type != OpCode.createSession) {
                    int pathLen = bb.getInt();
                    byte b[] = new byte[pathLen];
                    bb.get(b);
                    path = new String(b);
                }
            }
            System.out.println(DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.LONG).format(new Date(time)) + ": " + (char) app + " id=" + Long.toHexString(id) + " cxid=" + cxid + " op=" + op2String(type) + " zxid=" + Long.toHexString(zxid) + " txnType=" + txnType + " len=" + len + " path=" + path);
        }
    }
