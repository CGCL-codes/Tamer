    public static void writeFile(String file, byte[] data, MyByte bin, InputStream in, long len, ProgressCanvas pb) throws Exception {
        FileConnection f = (FileConnection) Connector.open(file, Connector.READ_WRITE);
        OutputStream out = null;
        Closeable c = null;
        try {
            if (!f.exists()) f.create();
            out = f.openOutputStream();
            int blockSize = HelperHTTP.BUFFER_SIZE, read = 0;
            if (pb != null) c = Closeable.create(out, pb.curThread);
            if (data != null) {
                if (pb != null) {
                    pb.setMaxValue(data.length);
                    pb.setText("Byte Size:" + HelperStd.formatBytes(data.length));
                }
                while (read < data.length) {
                    if (read + blockSize > data.length) blockSize = data.length - read;
                    out.write(data, read, blockSize);
                    read += blockSize;
                    if (pb != null) pb.setValueRePaint(read);
                }
            } else {
                if (len <= 0) len = -1;
                if (len > 0) {
                    if (bin != null) {
                        bin.reopenInputStream();
                        in = bin.in;
                    } else in = openInputStreamTmpFile(in);
                }
                if (pb != null) {
                    if (len > 0) pb.setText("Stream Size:" + HelperStd.formatBytes(len)); else pb.setText("Writing Stream ...");
                    if (len > 0) pb.setMaxValue((int) len); else if (len < 0 && in.available() > 0) pb.setMaxValue(in.available()); else pb.setInfinite(true);
                }
                System.out.println(in.available() + ";" + len);
                while ((in.available() > 0 && len == -1) || read < len) {
                    byte[] b = new byte[blockSize];
                    int ergeb = in.read(b);
                    if (ergeb == -1) break;
                    out.write(b, 0, ergeb);
                    read += ergeb;
                    if (pb != null) pb.setValueRePaint(read);
                }
                System.out.println("finished:" + read);
            }
        } finally {
            if (c != null) c.remove();
            HelperStd.closeStream(out);
            HelperFileIO.closeConnection(f);
        }
    }
