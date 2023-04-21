        byte[] getResponseBody(HttpEntity entity) {
            try {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                byte[] buf = new byte[2048];
                InputStream is = entity.getContent();
                for (int i = 0; i != -1; i = is.read(buf)) baos.write(buf, 0, i);
                return baos.toByteArray();
            } catch (Exception e) {
                return null;
            }
        }
