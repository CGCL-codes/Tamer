    public void retreiveMessage(StreamHandler pop3CH, int messageNumber) throws TooManyErrorsException, FileNotFoundException, IOException {
        DelimitedInputStream reader = null;
        List<byte[]> dataLines = new ArrayList<byte[]>(250);
        try {
            String messageLocation = user.getMessage(messageNumber).getMessageLocation();
            reader = new DelimitedInputStream(new FileInputStream(new File(getUserRepository(), messageLocation)));
            messageLocation = messageLocation.substring(messageLocation.lastIndexOf(File.separator) + 1, messageLocation.lastIndexOf('.'));
            boolean foundRPLCRCPT = false, foundRPLCID = false;
            String singleLine;
            byte[] currentLine = reader.readLine();
            while (currentLine != null) {
                dataLines.add(currentLine);
                singleLine = new String(currentLine, US_ASCII);
                if (singleLine.indexOf("<REPLACE-RCPT>") != -1) {
                    dataLines.set(dataLines.size() - 1, ("        for <" + user.getFullUsername() + ">" + singleLine.substring(singleLine.indexOf(';'))).getBytes(US_ASCII));
                    foundRPLCRCPT = true;
                } else if (singleLine.indexOf("<REPLACE-ID>") != -1) {
                    dataLines.set(dataLines.size() - 1, (singleLine.substring(0, singleLine.indexOf('<')) + messageLocation + (singleLine.charAt(singleLine.length() - 1) == ';' ? ";" : "")).getBytes(US_ASCII));
                    foundRPLCID = true;
                }
                currentLine = reader.readLine();
                if (currentLine.length == 0 || (foundRPLCRCPT && foundRPLCID)) break;
            }
            while (currentLine != null) {
                dataLines.add(currentLine);
                currentLine = reader.readLine();
                if (dataLines.size() == 250) {
                    for (byte[] readLine : dataLines) {
                        pop3CH.write(readLine);
                    }
                    dataLines.clear();
                }
            }
            int lineCount = dataLines.size();
            if (lineCount > 0) {
                for (byte[] readLine : dataLines) {
                    pop3CH.write(readLine);
                }
                dataLines.clear();
            }
            pop3CH.write(new byte[] { 0x2e });
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException ioe) {
                }
                reader = null;
            }
            dataLines.clear();
            dataLines = null;
        }
    }
