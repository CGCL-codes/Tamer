    public void writeTo(OutputStream os) throws IOException, MessagingException {
        InputStream is = null;
        boolean pk = getPeek();
        synchronized (getMessageCacheLock()) {
            try {
                IMAPProtocol p = getProtocol();
                checkExpunged();
                if (p.isREV1()) {
                    BODY b;
                    if (pk) b = p.peekBody(getSequenceNumber(), sectionId); else b = p.fetchBody(getSequenceNumber(), sectionId);
                    if (b != null) is = b.getByteArrayInputStream();
                } else {
                    RFC822DATA rd = p.fetchRFC822(getSequenceNumber(), null);
                    if (rd != null) is = rd.getByteArrayInputStream();
                }
            } catch (ConnectionException cex) {
                throw new FolderClosedException(folder, cex.getMessage());
            } catch (ProtocolException pex) {
                forceCheckExpunged();
                throw new MessagingException(pex.getMessage(), pex);
            }
        }
        if (is == null) throw new MessagingException("No content");
        byte[] bytes = new byte[1024];
        int count;
        while ((count = is.read(bytes)) != -1) os.write(bytes, 0, count);
    }
