    private void sendEmail() {
        List fileList = listHolder.getList();
        int listIndex = listHolder.getIndex();
        EmailMessage emailMsg = null;
        File currentFile = null;
        SMTPClient client = new SMTPClient();
        try {
            String host = InetAddress.getLocalHost().getHostName();
            for (; listIndex < fileList.size(); listIndex++) {
                currentFile = (File) fileList.get(listIndex);
                super.currentObjBeingProcessed = currentFile;
                if (currentFile == null || currentFile.length() == 0 || !currentFile.isFile()) {
                    continue;
                }
                try {
                    if (emailXml) {
                        emailMsg = parseXmlEmailDocument(currentFile, new EmailMessage());
                    } else {
                        emailMsg = parseTextEmailDocument(currentFile, new EmailMessage());
                    }
                    client.connect(smtpServerAddress);
                    if (!SMTPReply.isPositiveCompletion(client.getReplyCode())) {
                        client.disconnect();
                        throw new IOException("SMTP server refused connection:" + smtpServerAddress);
                    }
                    client.login(host);
                    boolean result;
                    result = client.setSender(emailMsg.from);
                    if (result == false) {
                        throw new IOException("Email 'From' invalid format: " + emailMsg.from);
                    }
                    StringTokenizer st = new StringTokenizer(emailMsg.to, ",");
                    while (st.hasMoreTokens()) {
                        String to = st.nextToken();
                        result = client.addRecipient(to);
                        if (result == false) {
                            throw new IOException("Email 'To' invalid format: " + emailMsg.to);
                        }
                    }
                    String data = "Subject: " + emailMsg.subject + "\nTo: " + emailMsg.to + "\n\n" + emailMsg.body;
                    result = client.sendShortMessageData(data);
                    if (result == false) {
                        throw new IOException("Email 'Body' invalid format: " + data);
                    }
                    client.logout();
                    if (client.isConnected()) {
                        client.disconnect();
                    }
                } catch (UnknownHostException e) {
                    msgEntry.setDocInfo(currentFile.toString());
                    msgEntry.setMessageText("Unknown host: " + smtpServerAddress);
                    msgEntry.setError(e.getMessage());
                    logger.logWarning(msgEntry);
                } catch (Exception e) {
                    msgEntry.setDocInfo(currentFile.toString());
                    msgEntry.setMessageText("Error in email element(s)");
                    msgEntry.setError(e.getMessage());
                    logger.logWarning(msgEntry);
                    fileUtils.moveFileToDoneLocation(currentFile, srcDoneLocation);
                    fileList.set(listIndex, null);
                    currentFile = null;
                    continue;
                }
                msgEntry.setDocInfo(currentFile.toString());
                msgEntry.setMessageText("Email successfully sent to '" + emailMsg.to + "'");
                logger.logProcess(msgEntry);
                if (processorStopSyncFlag.getFlag()) break;
            }
        } catch (IOException e) {
            if (noRetriesSoFar++ < noRetries) {
                waitBetweenRetry();
                notifyAndStartWaitingFlag = false;
            } else {
                notifyAndStartWaitingFlag = true;
                errEntry.setThrowable(e);
                errEntry.setAppContext("sendEmail()");
                errEntry.setAppMessage("Unable to send file after " + (noRetriesSoFar - 1) + " retries. Max Retries.");
                logger.logError(errEntry);
            }
            return;
        }
        moveFilesToDone(listHolder);
        notifyAndStartWaitingFlag = true;
    }
