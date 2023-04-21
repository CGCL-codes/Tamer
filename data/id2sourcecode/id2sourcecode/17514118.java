        public boolean onData(INonBlockingConnection connection) throws IOException, BufferUnderflowException, MaxReadSizeExceededException {
            RandomAccessFile raf = new RandomAccessFile(file, "r");
            FileChannel fc = raf.getChannel();
            connection.transferFrom(fc);
            fc.close();
            raf.close();
            return true;
        }
