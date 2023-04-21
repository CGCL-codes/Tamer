    public String sendCmdID(String command, String ID) {
        String result = "OK";
        try {
            LOGGER.trace("create msg");
            byte[] cmdBundle = AgentCore.createMsg(command.getBytes(), ID.getBytes());
            byte[] signCmd = Crypto.sign(cmdBundle, editorKey);
            byte[] msg = AgentCore.createMsg(cmdBundle, editorCert.getEncoded(), signCmd);
            LOGGER.trace("cript MSG for agent");
            byte[] encBytes = Crypto.encrypt(msg, agentCert.getPublicKey());
            LOGGER.trace("Send bytes to agent");
            String sAgentServer = agentUrl + "/agent/sendCommandArg1";
            MessageDigest algorithm = MessageDigest.getInstance("MD5");
            algorithm.reset();
            algorithm.update(encBytes);
            byte[] tmpDigest = algorithm.digest();
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < tmpDigest.length; i++) {
                sb.append(Integer.toHexString((tmpDigest[i] & 0xFF) | 0x100).toUpperCase().substring(1, 3));
            }
            String response = AgentCore.sendBytes(encBytes, sAgentServer, rt);
            if (response.startsWith("FAIL")) {
                throw new MyException(MyException.ERROR_GENERIC, response);
            }
            return response;
        } catch (MyException e) {
            LOGGER.error("An error has occurred", e);
            result = e.getErrMsg();
        } catch (ConnectException e) {
            result = AgentCore.ERRMSG_CONNECT;
        } catch (Exception e) {
            LOGGER.error("Unexpected error", e);
            result = "FAIL";
        }
        if (LOGGER.isTraceEnabled()) LOGGER.trace("result: " + result);
        return result;
    }
