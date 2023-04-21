    private static String transferData(String path) {
        String res = "";
        try {
            URL url = URI.create(path).toURL();
            InputStream is = url.openStream();
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            byte[] buf = new byte[BUFFER_SIZE];
            int len;
            log.log(Level.INFO, "##### inputstream available: " + is.available());
            while ((len = is.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            log.log(Level.INFO, "##### buf length: " + len + ", out.len: " + out.size());
            byte[] byteContent = out.toByteArray();
            log.log(Level.INFO, "evaluateContent() byteContent.length: " + byteContent.length);
            out.close();
            is.close();
            res = new String(byteContent);
        } catch (Exception e) {
            log.info("OAIDigitalObjectManagerKBImpl error: " + e.getMessage());
        }
        return res;
    }
