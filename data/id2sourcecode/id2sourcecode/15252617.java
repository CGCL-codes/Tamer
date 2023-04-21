    public void handleRetr(String body) {
        if (!parseParams(body, kRegExRequiredMsgId)) return;
        MailMessage mail = mMail[mMsgIndex];
        mStream.writeOk(mail.getSize() + " octets");
        InputStream stream = mail.openMessage();
        try {
            mStream.beginMultiLine();
            BufferedReader in = new BufferedReader(new InputStreamReader(stream));
            for (String line; (line = in.readLine()) != null; ) mStream.writeLine(line);
            mStream.endMultiLine();
        } catch (Exception e) {
            Ex.failed("Failed to read message", e);
        } finally {
            try {
                stream.close();
            } catch (Exception e) {
            }
        }
    }
