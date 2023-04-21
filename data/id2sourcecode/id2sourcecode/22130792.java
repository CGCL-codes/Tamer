    public int doWrites() {
        int amount = 0;
        for (WriteRequest request : writeRequests) {
            String data;
            if (request.getType() == WriteRequest.Type.CHANNEL_MSG) {
                data = "PRIVMSG " + request.getChannel().getName() + " :" + request.getMessage() + "\r\n";
            } else if (request.getType() == WriteRequest.Type.PRIVATE_MSG) {
                data = "PRIVMSG " + request.getNick() + " :" + request.getMessage() + "\r\n";
            } else {
                data = request.getMessage();
                if (!data.endsWith("\r\n")) {
                    data += "\r\n";
                }
            }
            try {
                writer.write(data.getBytes());
                amount += data.getBytes().length;
                fireWriteEvent(request);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return amount;
    }
