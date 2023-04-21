    public byte[] getCodeByte(String url) {
        log.debug("-------------------get randcode start-------------------");
        HttpGet get = new HttpGet(url);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            HttpResponse response = httpclient.execute(get);
            HttpEntity entity = response.getEntity();
            log.debug(response.getStatusLine());
            if (entity != null) {
                InputStream is = entity.getContent();
                byte[] buf = new byte[1024];
                int len = -1;
                while ((len = is.read(buf)) > -1) {
                    baos.write(buf, 0, len);
                }
            }
        } catch (Exception e) {
            log.error(e);
        }
        log.debug("-------------------get randcode end-------------------");
        return baos.toByteArray();
    }
