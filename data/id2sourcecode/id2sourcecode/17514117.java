        public boolean onData(INonBlockingConnection connection) throws IOException, BufferUnderflowException, MaxReadSizeExceededException {
            RandomAccessFile raf = new RandomAccessFile(file, "r");
            FileChannel fc = raf.getChannel();
            fc.transferTo(0, fc.size(), connection);
            fc.close();
            raf.close();
            return true;
        }
