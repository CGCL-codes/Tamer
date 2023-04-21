    private void handlePart(final Part part) throws MessagingException, IOException {
        if (part.getContent() instanceof Multipart) {
            Multipart multipart = (Multipart) part.getContent();
            for (int i = 0; i < multipart.getCount(); i++) {
                handlePart(multipart.getBodyPart(i));
            }
            return;
        }
        if (part.getContentType().startsWith("text/html") || part.getContentType().startsWith("text/plain")) {
            if (bodytext == null) bodytext = (String) part.getContent(); else bodytext = bodytext + (String) part.getContent();
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
