    private String makeRequest(String request) throws MalformedURLException, IOException {
        URL url = new URL(request);
        BufferedReader stream = new BufferedReader(new InputStreamReader(url.openStream()));
        int i = 0;
        String matches = "FAILURE";
        for (String line = stream.readLine(); line != null; line = stream.readLine()) {
            if (i == 2) {
                matches = line.trim();
            }
            i++;
        }
        stream.close();
        return matches;
    }
