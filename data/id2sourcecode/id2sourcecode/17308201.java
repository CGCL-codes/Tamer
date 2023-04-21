        public boolean onConnect(INonBlockingConnection connection) throws IOException, BufferUnderflowException, MaxReadSizeExceededException {
            connection.setAttachment(new SessionData());
            connection.write("220 SMTP ready \r\n");
            return true;
        }
