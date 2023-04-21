    public void read(String name, File file) throws IOException {
        IBlockingConnection con = null;
        try {
            con = pool.getBlockingConnection(host, port);
            con.write("get\r\n");
            con.write(name + "\r\n");
            int size = con.readInt();
            RandomAccessFile raf = new RandomAccessFile(file, "rw");
            FileChannel fc = raf.getChannel();
            long s = con.transferTo(fc, size);
            if (s != size) {
                System.out.println("WARNING written size " + s + " is not equals expected " + size);
            }
            fc.close();
            raf.close();
            System.out.println("client downloaded (size=" + s + ")");
            con.close();
        } catch (IOException ioe) {
            if (con != null) {
                pool.destroy(con);
            }
        }
    }
