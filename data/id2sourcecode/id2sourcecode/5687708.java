        public boolean onData(INonBlockingConnection connection) throws IOException, BufferUnderflowException, MaxReadSizeExceededException {
            connection.readStringByDelimiter("\r\n");
            File file = QAUtil.createTestfile_400k();
            RandomAccessFile raf = new RandomAccessFile(file, "r");
            ReadableByteChannel in = raf.getChannel();
            connection.transferFrom(in);
            in.close();
            raf.close();
            file.delete();
            return true;
        }
