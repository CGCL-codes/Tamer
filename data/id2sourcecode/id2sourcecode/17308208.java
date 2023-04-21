        public boolean onData(INonBlockingConnection connection) throws IOException, BufferUnderflowException, MaxReadSizeExceededException {
            if (fc == null) {
                file.createNewFile();
                raf = new RandomAccessFile(file, "rw");
                fc = raf.getChannel();
                fc.write(DataConverter.toByteBuffer(timeStamp, "US-ASCII"));
            }
            int delimiterPos = connection.indexOf("\r\n.\r\n");
            if (delimiterPos != -1) {
                connection.transferTo(fc, delimiterPos);
                connection.readByteBufferByLength(5);
                fc.close();
                raf.close();
                fc = null;
                smtpHandler.incCountReceiveMessages();
                connection.setHandler(smtpHandler);
                connection.write("250 OK\r\n");
            } else if (connection.available() > 5) {
                int size = connection.available() - 5;
                connection.transferTo(fc, size);
            }
            return true;
        }
