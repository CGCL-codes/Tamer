    private void setupSpecialPage(String vWiki, String specialPage) throws Exception {
        File dummy = getPathFor(vWiki, specialPage + TEXT_EXTENSION);
        if (!dummy.exists()) {
            Writer writer = new OutputStreamWriter(new FileOutputStream(dummy), Environment.getInstance().getFileEncoding());
            writer.write(WikiBase.readDefaultTopic(specialPage));
            writer.close();
        }
    }
