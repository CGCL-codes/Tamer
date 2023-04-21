    private HTMLPane getRelnotesPane() throws IOException {
        if (relnotesPane == null) {
            URL url = LocalizationData.getURL("relnotes.txt");
            BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream(), "UTF-8"));
            try {
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                ReleaseNotesFormatter releaseNotesFormatter = new ReleaseNotesFormatter();
                releaseNotesFormatter.setIgnoreNext(true);
                releaseNotesFormatter.build(reader, new BufferedWriter(new OutputStreamWriter(stream)));
                String html = stream.toString();
                relnotesPane = new HTMLPane(html);
            } finally {
                reader.close();
            }
            relnotesPane.setPreferredSize(PREFERED_HTML_PANE_SIZE);
        }
        return relnotesPane;
    }
