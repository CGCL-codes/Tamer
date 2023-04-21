    public String getCalendarPart(Message aMessage) {
        Object content = null;
        String retVal = null;
        try {
            LOG.info("\nTrying to get calendar part from Message with content type: " + aMessage.getContentType());
            if (aMessage.getContentType().toLowerCase().startsWith(MIME_MULTIPLART_ALTERNATIVE)) {
                if (aMessage.getContent() instanceof Multipart) {
                    content = extractCalendarPartFromMultipart((Multipart) aMessage.getContent());
                } else {
                    LOG.info("Content class of Message is no MultiPart: " + (null != aMessage.getContent() ? aMessage.getContent().getClass().getName() : null));
                }
            } else if (aMessage.getContentType().toLowerCase().startsWith(MIME_MULTIPLART_MIXED)) {
                if (aMessage.getContent() instanceof Multipart) {
                    LOG.info("Content of the Message is multipart");
                    Multipart multipart = (Multipart) aMessage.getContent();
                    int count = multipart.getCount();
                    LOG.info("Multipart count: " + count);
                    for (int i = 0; i < count; i++) {
                        BodyPart body = multipart.getBodyPart(i);
                        LOG.info("Content-type of the BodyPart: " + body.getContentType());
                        if (body.getContentType().toLowerCase().startsWith(MIME_TEXT_CALENDAR)) {
                            LOG.info("Content class of the BodyPart: " + (null != body.getContent() ? body.getContent().getClass().getName() : null));
                            if (body.getContent() instanceof InputStream) {
                                InputStream is = (InputStream) body.getContent();
                                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                                byte[] buffer = new byte[1024 * 4];
                                int readBytes;
                                while ((readBytes = is.read(buffer)) >= 0) {
                                    baos.write(buffer, 0, readBytes);
                                }
                                baos.flush();
                                content = baos.toString();
                                baos.close();
                            } else {
                                content = body.getContent();
                            }
                            break;
                        } else if (body.getContentType().toLowerCase().startsWith(MIME_MULTIPLART_ALTERNATIVE)) {
                            if (body.getContent() instanceof Multipart) {
                                content = extractCalendarPartFromMultipart((Multipart) body.getContent());
                            } else {
                                LOG.info("Content class of Message is no MultiPart: " + (null != aMessage.getContent() ? aMessage.getContent().getClass().getName() : null));
                            }
                        }
                    }
                } else {
                    LOG.info("Content class of Message is no MultiPart: " + (null != aMessage.getContent() ? aMessage.getContent().getClass().getName() : null));
                }
            } else if (aMessage.getContentType().toLowerCase().startsWith(MIME_TEXT_PLAIN)) {
                if (aMessage.getContent() instanceof String) {
                    content = (String) aMessage.getContent();
                } else {
                    LOG.warning("Content of Message is no string: " + (null != aMessage.getContent() ? aMessage.getContent().getClass().getName() : null));
                }
            } else {
                LOG.warning("Unsupported content type: " + aMessage.getContentType());
            }
            if (content instanceof String) {
                LOG.info("Final content class is a string");
                retVal = (String) content;
            } else {
                LOG.info("Final content class is: " + (null != content ? content.getClass().getName() : null));
            }
        } catch (MessagingException me) {
            me.printStackTrace();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        return retVal;
    }
