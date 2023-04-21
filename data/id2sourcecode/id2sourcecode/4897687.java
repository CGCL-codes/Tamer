        Writer(String filename, INonBlockingConnection con) throws IOException {
            this.con = con;
            raf = new RandomAccessFile(filename, "r");
            channel = raf.getChannel();
        }
