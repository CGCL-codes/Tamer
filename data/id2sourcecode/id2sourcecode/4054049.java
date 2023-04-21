    private byte[] saveAttachment(AttachmentPart part, File docFile) throws SOAPException, IOException, MessagingException, NoSuchAlgorithmException {
        log.info("Save Attachment " + part.getContentId() + " (size=" + part.getSize() + ") to file " + docFile);
        Object content = part.getContent();
        BufferedOutputStream bos = null;
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("SHA1");
            DigestOutputStream dos = new DigestOutputStream(new FileOutputStream(docFile), md);
            bos = new BufferedOutputStream(dos);
            if (content instanceof String) {
                dos.write(content.toString().getBytes());
            } else {
                if (content instanceof StreamSource) {
                    content = ((StreamSource) content).getInputStream();
                }
                if (content instanceof InputStream) {
                    InputStream is = (InputStream) content;
                    byte[] buffer = new byte[BUFFER_SIZE];
                    for (int len; (len = is.read(buffer)) > 0; ) {
                        dos.write(buffer, 0, len);
                    }
                } else if (content instanceof MimeMultipart) {
                    MimeMultipart mmp = (MimeMultipart) content;
                    mmp.writeTo(dos);
                } else {
                    throw new IllegalArgumentException("Unknown content:" + content.getClass().getName() + " contentType:" + part.getContentType());
                }
            }
        } finally {
            if (bos != null) bos.close();
        }
        return md == null ? null : md.digest();
    }
