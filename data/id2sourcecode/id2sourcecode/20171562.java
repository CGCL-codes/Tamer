        NonThreadedChannelAsyncWriter(String filename, INonBlockingConnection con) throws IOException {
            this.con = con;
            channel = new RandomAccessFile(filename, "r").getChannel();
            con.setFlushmode(FlushMode.ASYNC);
        }
