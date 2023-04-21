    public void send(String name, File file) throws IOException {
        IBlockingConnection con = null;
        try {
            con = pool.getBlockingConnection(host, port);
            con.write("put\r\n");
            con.write(name + "\r\n");
            con.write((int) file.length());
            RandomAccessFile raf = new RandomAccessFile(file, "r");
            FileChannel fc = raf.getChannel();
            long size = con.transferFrom(fc);
            fc.close();
            raf.close();
            con.close();
            System.out.println("client uploaded (size=" + size + ")");
        } catch (IOException ioe) {
            if (con != null) {
                pool.destroy(con);
            }
        }
    }
