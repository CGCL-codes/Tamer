    @Override
    public void loadSE(String se) throws IOException {
        InputStream is = null;
        String fn = "/res/se/" + se + ".wav";
        URL u = rf.getResource(fn);
        if (u != null) is = u.openStream();
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream(131072);
            byte[] buf = new byte[65536];
            int read;
            while ((read = is.read(buf)) >= 0) {
                baos.write(buf, 0, read);
            }
            ses.put(se, baos.toByteArray());
        } catch (IOException e) {
            throw new RuntimeException("打开资源错误：" + fn, e);
        } finally {
            try {
                is.close();
            } catch (Exception e) {
            }
        }
    }
