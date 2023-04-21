    public String doCommand(int timeoutSec) throws IOException {
        InputStream instr = tc.getInputStream();
        String response = null;
        String lastPrompt = null;
        String lastCommand = null;
        try {
            int i = 0;
            String tmpCommand = "";
            boolean nextExit = false;
            for (i = 0; i < exchangeVec.size(); ) {
                Exchange ex = (Exchange) exchangeVec.get(i);
                int numBytesRead = 0;
                Thread.sleep(timeoutSec * 1000);
                byte[] buff = new byte[2048];
                String strRecv = null;
                if (nextExit) {
                    strRecv = getCommandResponse(instr, lastPrompt);
                } else {
                    strRecv = getResponse(instr);
                }
                if (nextExit) {
                    response = strRecv;
                    sendCommand("exit");
                    break;
                }
                if (strRecv == null) {
                    throw new IOException();
                }
                strRecv = strRecv.trim();
                if (strRecv.endsWith(ex.getPrompt())) {
                    tmpCommand = ex.getCommand();
                    i++;
                } else if (strRecv.equals("") || strRecv.endsWith(ex.getErrorprompt())) {
                    tc.disconnect();
                    throw new IOException();
                }
                lastPrompt = strRecv;
                lastCommand = tmpCommand;
                sendCommand(tmpCommand);
            }
            response = getCommandResponse(instr, lastPrompt);
        } catch (Exception e) {
            throw new IOException("Unable to read/write with target machine.");
        }
        tc.disconnect();
        response = response.replaceAll(lastPrompt, "");
        response = response.replaceAll(lastCommand, "");
        return response;
    }
