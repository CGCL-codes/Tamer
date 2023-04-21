    private void extractPart(final Part part) throws MessagingException, IOException {
        Object content = part.getContent();
        if (content instanceof Multipart) {
            for (int i = 0; i < ((Multipart) content).getCount(); i++) {
                extractPart(((Multipart) content).getBodyPart(i));
            }
            return;
        }
        if (part.getContentType().startsWith("text/html")) {
            if (bodytext == null) {
                bodytext = (String) part.getContent();
            } else {
                bodytext = bodytext + "<HR/>" + (String) part.getContent();
            }
        } else if (!part.getContentType().startsWith("text/plain")) {
            Attachment attachment = new Attachment();
            attachment.setContenttype(part.getContentType());
            attachment.setFilename(part.getFileName());
            InputStream in = part.getInputStream();
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            byte[] buffer = new byte[8192];
            int count = 0;
            while ((count = in.read(buffer)) >= 0) bos.write(buffer, 0, count);
            in.close();
            attachment.setContent(bos.toByteArray());
            attachments.add(attachment);
        }
    }
