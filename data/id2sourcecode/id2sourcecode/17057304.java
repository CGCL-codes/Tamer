        public boolean openConnection(Exchange[] excs, int timeoutMillisec) throws IOException {
            Category log = ThreadCategory.getInstance(getClass());
            InputStream instr = tc.getInputStream();
            String lastCommand = null;
            log.debug("Opening telnet connnection... " + excs.length + " Exchanges");
            try {
                String tmpCommand = "";
                for (int i = 0; i < excs.length; ) {
                    Exchange ex = excs[i];
                    int numBytesRead = 0;
                    log.debug("Sleeping for " + timeoutMillisec + " millisecs...");
                    Thread.sleep(timeoutMillisec);
                    log.debug("End sleeping");
                    String strRecv = null;
                    strRecv = getResponse(instr);
                    log.debug("Received: " + strRecv);
                    if (strRecv == null) {
                        if (tc.isConnected()) {
                            tc.disconnect();
                        }
                        log.info("Telnet Connection open error");
                        return false;
                    }
                    strRecv = strRecv.trim();
                    if (strRecv.endsWith(ex.getPrompt())) {
                        tmpCommand = ex.getCommand();
                        i++;
                    } else if (strRecv.equals("") || strRecv.endsWith(ex.getErrorprompt())) {
                        if (tc.isConnected()) {
                            tc.disconnect();
                        }
                        log.info("Telnet Connection open error");
                        return false;
                    }
                    prompt = strRecv;
                    lastCommand = tmpCommand;
                    sendCommand(tmpCommand);
                    log.debug("Sending command " + tmpCommand);
                }
                Thread.sleep(timeoutMillisec);
                prompt = getResponse(instr);
            } catch (Exception e) {
                throw new IOException("Unable to read/write with target machine.");
            }
            log.info("Telnet Conneciton opened successfully");
            return true;
        }
