    private void readEntries(final Set entries, final URL url) throws IOException {
        final BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
        try {
            String entry = reader.readLine();
            while (entry != null) {
                entries.add(entry);
                entry = reader.readLine();
            }
        } finally {
            reader.close();
        }
    }
