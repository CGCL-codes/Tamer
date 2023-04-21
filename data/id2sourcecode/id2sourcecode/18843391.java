    public void receiveMSG(MessageMSG message) {
        OutputDataStream data = new OutputDataStream();
        InputDataStream ds = message.getDataStream();
        while (true) {
            try {
                BufferSegment b = ds.waitForNextSegment();
                if (b == null) {
                    break;
                }
                data.add(b);
            } catch (InterruptedException e) {
                message.getChannel().getSession().terminate(e.getMessage());
                return;
            }
        }
        data.setComplete();
        try {
            message.sendRPY(data);
        } catch (BEEPException e) {
            try {
                message.sendERR(BEEPError.CODE_REQUESTED_ACTION_ABORTED, "Error sending RPY");
            } catch (BEEPException x) {
                message.getChannel().getSession().terminate(x.getMessage());
            }
            return;
        }
    }
