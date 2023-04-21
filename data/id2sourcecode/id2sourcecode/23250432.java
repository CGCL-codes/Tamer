    public void getAttachment(String folderFqn, String uid, String attachmentName) throws TeqloException {
        try {
            Message message = this.getMessage(folderFqn, uid);
            MimeMultipart multipart = (MimeMultipart) message.getContent();
            for (int k = 0; k < multipart.getCount(); k++) {
                MimeBodyPart part = (MimeBodyPart) multipart.getBodyPart(k);
                String disposition = part.getDisposition();
                if (disposition.equalsIgnoreCase(Part.ATTACHMENT) || disposition.equalsIgnoreCase(Part.INLINE)) {
                    String fileName = part.getFileName();
                    if (fileName.equals(attachmentName)) {
                        InputStream istream = part.getInputStream();
                        ByteArrayOutputStream bout = new ByteArrayOutputStream();
                        byte[] buffer = new byte[4000];
                        int length;
                        while ((length = istream.read(buffer)) >= 0) bout.write(buffer, 0, length);
                        bout.flush();
                        buffer = bout.toByteArray();
                        bout.close();
                    }
                }
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
