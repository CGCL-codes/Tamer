    public CHandler(StreamConnection con, int pID, int qID) {
        CLogging.simpleLog("handleClient");
        this.pID = pID;
        this.qID = qID;
        this.date = new java.util.Date().getTime();
        try {
            this.question = CDatabaseAdapter.getQuestion(this.qID);
        } catch (Exception ex) {
            CLogging.simpleLog("Exception: " + ex);
            CLogging.saveStackTrace(ex);
        }
        this.question.setDate(this.date);
        byte[] qBytes;
        String resultStr = null;
        InputStream in = null;
        ByteArrayOutputStream out = null;
        OutputStream out1 = null;
        int read = -1;
        try {
            in = con.openInputStream();
        } catch (Exception ex) {
            CLogging.simpleLog("openInputStream failed: " + ex);
        }
        try {
            out = new ByteArrayOutputStream();
            while (((read = in.read()) != -1) && (read != 27)) {
                out.write(read);
            }
            resultStr = new String(out.toByteArray(), "UTF-8");
            CLogging.simpleLog("Request Client: \"" + resultStr + "\"");
        } catch (Exception ex) {
            CLogging.simpleLog("Evaluate incoming stream failed: " + ex);
        } finally {
            out = null;
        }
        if (resultStr.equals("GetQ")) {
            CLogging.simpleLog("Preparing QPacket");
            try {
                out1 = con.openOutputStream();
                qBytes = CBluetoothAdapter.serialize(this.getMQPacket());
                out1.write(qBytes);
                CLogging.simpleLog("MQPacket Stream: " + out1.toString());
                CLogging.simpleLog("MQPacket " + qBytes.toString() + " gesendet");
            } catch (Exception ex) {
                CLogging.simpleLog("Sending serialized MQPacket failed: " + ex);
            } finally {
                out1 = null;
            }
        } else if (resultStr.equals("GiveA")) {
            CLogging.simpleLog("Give A");
            try {
                out = new ByteArrayOutputStream();
                while (((read = in.read()) != -1) && (read != 27)) {
                    out.write(read);
                }
                resultStr = new String(out.toByteArray(), "UTF-8");
                CLogging.simpleLog("Content of clien stream: " + resultStr);
            } catch (Exception ex) {
                CLogging.simpleLog("Evaluate incoming stream failed: " + ex);
            } finally {
                out = null;
            }
            this.nMAPacket = CBluetoothAdapter.deserialize(resultStr);
            CLogging.simpleLog("Answer Packet: " + this.nMAPacket);
        } else {
            CLogging.simpleLog("Client sent a defective stream: " + resultStr);
        }
    }
