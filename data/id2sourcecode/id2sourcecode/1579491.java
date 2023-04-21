    public void receiveMSG(MessageMSG message) {
        System.out.println("S: receiveMSG");
        InputDataStream xmlInput = message.getDataStream();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        do {
            try {
                System.out.println("S: waitForNextSegment");
                BufferSegment b = xmlInput.waitForNextSegment();
                System.out.println("S: ok " + b.getLength());
                if (b == null) {
                    out.flush();
                    break;
                }
                out.write(b.getData());
            } catch (Exception e) {
                message.getChannel().getSession().terminate(e.getMessage());
                return;
            }
        } while (!xmlInput.isComplete());
        System.out.println("S: read " + out.size() + " bytes from input.");
        ByteArrayInputStream in = new ByteArrayInputStream(out.toByteArray());
        out = process(in);
        OutputDataStream xmlOutput = new OutputDataStream();
        byte[] data = out.toByteArray();
        BufferSegment segment = new BufferSegment(data);
        xmlOutput.add(segment);
        xmlOutput.setComplete();
        try {
            message.sendRPY(xmlOutput);
        } catch (BEEPException be) {
            try {
                message.sendERR(BEEPError.CODE_REQUESTED_ACTION_ABORTED, "Error sending RPY");
            } catch (BEEPException x) {
                message.getChannel().getSession().terminate(x.getMessage());
            }
            return;
        }
    }
