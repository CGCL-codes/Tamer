    private String extractCalendarPartFromMultipart(Multipart aMultipart) {
        assert null != aMultipart;
        String retVal = null;
        try {
            Object content = null;
            int count = aMultipart.getCount();
            LOG.info("Multipart count: " + count);
            for (int i = 0; i < count; i++) {
                BodyPart body = aMultipart.getBodyPart(i);
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
                }
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
