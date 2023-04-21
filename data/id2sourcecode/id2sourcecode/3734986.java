    private void fetchIds() throws IOException {
        String urlString = BASE_URL + IDS_URL + DATASET_URL + "&family=" + mFamily;
        URL url = new URL(urlString);
        BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
        ArrayList<String> ids = new ArrayList<String>();
        List<String> lines = getHTMLLines(in);
        for (String line : lines) {
            line = line.trim();
            if (line.length() == 0) continue;
            if (line.contains("<b>")) continue;
            ids.add(line);
        }
        in.close();
        processIds(ids);
    }
