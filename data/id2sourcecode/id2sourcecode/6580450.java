        @Override
        public boolean onData(INonBlockingConnection connection) throws IOException, BufferUnderflowException, MaxReadSizeExceededException {
            String cmd = connection.readStringByDelimiter(DELIMITER);
            if (cmd.startsWith(PREPARE_DOWNLOAD)) {
                Integer size = Integer.parseInt(cmd.substring(PREPARE_DOWNLOAD.length(), cmd.length()));
                if (readFromFile) {
                    File file = File.createTempFile("test", TEMP_FILE_EXTENSION);
                    file.deleteOnExit();
                    FileChannel fc = new RandomAccessFile(file, "rw").getChannel();
                    fc.write(QAUtil.generateDirectByteBuffer(size));
                    fc.close();
                    connection.setAttachment(file);
                } else {
                    connection.setAttachment(size);
                }
                connection.write(OK + DELIMITER);
            } else if (cmd.startsWith(DOWNLOAD_REQUEST)) {
                if (readFromFile) {
                    try {
                        File file = (File) connection.getAttachment();
                        FileChannel fc = new RandomAccessFile(file, "r").getChannel();
                        connection.transferFrom(fc);
                        fc.close();
                        connection.write(DELIMITER);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    Integer size = (Integer) connection.getAttachment();
                    connection.write(QAUtil.generateDirectByteBuffer(size));
                    connection.write(DELIMITER);
                }
            }
            return true;
        }
