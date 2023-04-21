    private String getContent(final String adress) throws IOException {
        final URL url = new URL(adress);
        final StringBuilder content = new StringBuilder();
        final BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
        for (String s; (s = in.readLine()) != null && !this.killed; ) {
            if (this.matchesKeppPattern(s)) content.append(s);
            if (this.matchesEndPattern(s)) break;
        }
        in.close();
        return content.toString();
    }
