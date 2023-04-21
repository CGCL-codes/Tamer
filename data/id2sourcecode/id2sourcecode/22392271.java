    public static void writeFileData(String fileURL, OutputStream out, ProgressCanvas bc) throws IOException {
        System.out.println("writeFileData:" + fileURL);
        FileConnection fileConn = null;
        Closeable c = null;
        InputStream is = null;
        try {
            HelperApp.runJavaGarbageCollector();
            fileConn = (FileConnection) Connector.open(fileURL, Connector.READ);
            int blockSize = HelperHTTP.BUFFER_SIZE, read = 0, ergeb = 0, size = (int) fileConn.fileSize();
            if (bc != null) bc.setMaxValue(size);
            is = fileConn.openInputStream();
            if (bc != null) c = Closeable.create(is, bc.curThread);
            byte[] b = new byte[blockSize <= size ? blockSize : size];
            String status = "start";
            try {
                while ((ergeb = is.read(b)) != -1) {
                    status = "writing";
                    out.write(b, 0, ergeb);
                    read += ergeb;
                    if (read >= size) return;
                    if (size - read < b.length) b = new byte[size - read];
                    status = "rep";
                    if (bc != null) bc.setValueRePaint(read);
                    status = "reading";
                }
            } catch (Exception ee) {
                throw new IOException("read-write:" + ee.getMessage() + "::" + status);
            }
        } finally {
            if (c != null) c.remove();
            HelperStd.closeStream(is);
            HelperFileIO.closeConnection(fileConn);
        }
    }
