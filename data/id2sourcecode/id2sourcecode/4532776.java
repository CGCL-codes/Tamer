    private String fetchLog(String fileName, String revision, String branch, String encoding) throws MalformedURLException, UnsupportedEncodingException, IOException {
        if (viewcvsUrl == null) {
            return "";
        }
        InputStream in;
        try {
            if (branch != null) {
                in = new URL(viewcvsUrl + urlquote(fileName, false) + "?only_with_tag=" + urlquote(branch, true)).openStream();
            } else {
                in = new URL(viewcvsUrl + urlquote(fileName, false)).openStream();
            }
        } catch (IOException e) {
            log.warn(Messages.getString("CVSHistory.logNotFound1") + fileName + Messages.getString("CVSHistory.logNotFound2") + branch, e);
            return null;
        }
        if (!(in instanceof BufferedInputStream)) {
            in = new BufferedInputStream(in);
        }
        try {
            for (; ; ) {
                skipTag(in, "hr");
                if (!"Revision".equals(readToken(in))) {
                    continue;
                }
                if (!revision.equals(readToken(in))) {
                    continue;
                }
                return readBlock(in, "pre", encoding);
            }
        } catch (EOFException e) {
        } finally {
            in.close();
        }
        return null;
    }
