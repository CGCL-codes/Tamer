    @Override
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) {
        log.trace("Received message from " + e.getChannel().getId());
        try {
            this.msgBuffer += e.getMessage();
            int seperatorPos = this.msgBuffer.indexOf("|");
            if (seperatorPos != -1) {
                String message = this.msgBuffer.substring(0, seperatorPos);
                if (this.msgBuffer.length() == seperatorPos + 1) {
                    this.msgBuffer = "";
                } else {
                    this.msgBuffer = this.msgBuffer.substring(seperatorPos + 1);
                }
                JSONObject jsonObject = JSONObject.fromObject(message);
                String command = jsonObject.getString("command");
                RougeObject payload = new RougeObject(jsonObject.getJSONObject("payload"));
                this.onMessageReceived(e.getChannel(), command, payload);
            }
        } catch (Exception ex) {
            log.error("Error processing message " + ex.getMessage());
            ex.printStackTrace();
        }
    }
