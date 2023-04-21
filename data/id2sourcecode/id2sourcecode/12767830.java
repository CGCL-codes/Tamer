    private void setupSpecialPage(String vWiki, String specialPage) throws Exception {
        File dummy = getPathFor(vWiki, specialPage + ".txt");
        if (!dummy.exists()) {
            Writer writer = new OutputStreamWriter(new FileOutputStream(dummy), Environment.getValue(Environment.PROP_FILE_ENCODING));
            writer.write(Topic.readDefaultTopic(specialPage));
            writer.close();
        }
    }
