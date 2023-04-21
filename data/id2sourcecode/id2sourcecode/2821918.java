    private synchronized void initialise() throws Exception {
        final String GO_ID = "(GOID \"";
        final String GO_ID_REGEXP = "\\(GOID \"";
        final String COMPONENTS = "(COMPONENTS ";
        final String COMPONENTS_REGEXP = "\\(COMPONENTS ";
        final String EMPTY_STRING = "";
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(url.openStream(), Charset.defaultCharset()));
            String line = null;
            boolean newEntry = false;
            String id = null;
            while ((line = reader.readLine()) != null) {
                if (line.equals(EMPTY_STRING)) {
                    newEntry = true;
                } else if (newEntry) {
                    id = line.replaceAll("\\(", EMPTY_STRING).split("\\s")[0];
                    newEntry = false;
                } else if (line.startsWith(GO_ID)) {
                    line = line.replaceAll(GO_ID_REGEXP, EMPTY_STRING);
                    idToOntologyTermId.put(id, new GoTerm(line.substring(0, line.indexOf("\""))));
                } else if (line.startsWith(COMPONENTS)) {
                    line = line.replaceAll(COMPONENTS_REGEXP, EMPTY_STRING).replace(")", EMPTY_STRING);
                    for (String term : line.split("\\s+")) {
                        idToContainedById.put(term, id);
                    }
                }
            }
            initialised = true;
        } finally {
            if (reader != null) {
                reader.close();
            }
        }
    }
