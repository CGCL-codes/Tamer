    private void connect(String language) throws IOException {
        URL url = new URL(SPELLCHECK_PROTOCOL, SPELLCHECK_HOST, SPELLCHECK_PORT, SPELLCHECK_PATH + QUERY[0] + ((language.compareTo("") == 0) ? QUERY[1] : language) + QUERY[2]);
        uc = url.openConnection();
    }
