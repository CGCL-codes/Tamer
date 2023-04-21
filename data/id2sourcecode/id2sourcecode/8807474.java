        public boolean onData(INonBlockingConnection connection) throws IOException, BufferUnderflowException, ClosedChannelException, MaxReadSizeExceededException {
            String filename = connection.readStringByDelimiter("\r\n");
            if (!new File(filename).exists()) {
                System.out.println("file " + filename + " not exists");
            }
            RandomAccessFile raf = new RandomAccessFile(filename, "r");
            FileChannel fc = raf.getChannel();
            connection.write((int) raf.length());
            connection.transferFrom(fc);
            fc.close();
            raf.close();
            System.out.println(raf.length() + " bytes written");
            return true;
        }
