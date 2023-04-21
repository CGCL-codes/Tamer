    @Override
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) {
        log.trace("Message received from channel " + e.getChannel().getId());
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
            JSONObject jSonPayload = jsonObject.getJSONObject("payload");
            RougeObject payload = new RougeObject(jSonPayload);
            this.driver.handle(command, payload);
        }
    }
