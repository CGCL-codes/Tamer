    private boolean isTopicExisting(String wikiWord) {
        if (existingTopics.contains(wikiWord)) {
            return true;
        }
        try {
            URL url = new URL(formatUrl(wikiWord, existingTopicUrlPattern));
            InputStream page = url.openStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(page));
            try {
                String line = reader.readLine();
                while (line != null) {
                    if (line.matches(substituteWikiWord(wikiWord, newTopicPattern))) {
                        return false;
                    }
                    line = reader.readLine();
                }
            } finally {
                reader.close();
            }
        } catch (java.io.IOException e) {
            return false;
        }
        existingTopics.add(wikiWord);
        return true;
    }
