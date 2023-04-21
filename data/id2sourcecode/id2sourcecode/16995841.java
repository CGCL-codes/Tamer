    public void onMessage(javax.jms.Message msg) {
        try {
            BytesMessage bmsg = (BytesMessage) msg;
            javax.jms.Queue replyTo = (javax.jms.Queue) msg.getJMSReplyTo();
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            byte b;
            try {
                while (true) {
                    bos.write(bmsg.readByte());
                }
            } catch (MessageEOFException e) {
            }
            byte[] response;
            try {
                response = delivery.send(bos.toByteArray());
            } catch (IOException e) {
                e.printStackTrace();
                response = "ERROR".getBytes();
            }
            synchronized (this) {
                BytesMessage reply = this.session.createBytesMessage();
                reply.writeBytes(response);
                this.sender.send(replyTo, reply);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
