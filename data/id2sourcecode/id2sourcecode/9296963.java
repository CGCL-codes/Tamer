    public Object persistLocalMessage(User user, SMTPMessage message, EmailAddress address) throws IOException {
        File userDirectory = new File(getUserRepository(user));
        final File messageFile = new File(userDirectory, message.getSMTPUID() + ".tmp");
        BufferedOutputStream out = null;
        try {
            if (log.isDebugEnabled()) {
                log.debug("Delivering to: " + messageFile.getAbsolutePath());
            }
            out = new BufferedOutputStream(new FileOutputStream(messageFile), 4096);
            String outLine = "Return-Path: <" + message.getFromAddress().getAddress() + ">";
            out.write(outLine.getBytes(US_ASCII));
            out.write(EOL);
            out.flush();
            outLine = "Delivered-To: " + address.getAddress();
            out.write(outLine.getBytes(US_ASCII));
            out.write(EOL);
            out.flush();
            int count = 8;
            List<byte[]> dataLines = message.getSMTPPersistenceProccessor().loadIncrementally(count);
            while (dataLines.size() > 0) {
                for (byte[] singleLine : dataLines) {
                    if (singleLine.length > 0 && singleLine[0] == 0x2e) {
                        out.write(new byte[] { 0x02e });
                    }
                    out.write(singleLine);
                    out.write(EOL);
                    out.flush();
                }
                count += 250;
                dataLines.clear();
                dataLines = message.getSMTPPersistenceProccessor().loadIncrementally(count);
            }
            out.close();
            File messageLocation = new File(userDirectory, message.getSMTPUID() + ".loc");
            FileUtils.copyFile(messageFile, messageLocation);
            if (!messageFile.delete()) {
                throw new IOException("Failed to rename " + messageFile.getPath() + " to " + messageLocation.getPath());
            }
            return messageLocation.getPath();
        } catch (IOException ioe) {
            log.error("Error performing local delivery.", ioe);
            throw ioe;
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException ioe) {
                    log.error("Error closing output Stream.", ioe);
                }
            }
        }
    }
