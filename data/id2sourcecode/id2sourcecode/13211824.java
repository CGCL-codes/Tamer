    private void executeAddGraphs() throws MalformedURLException, IOException {
        Iterator<String> it = this.graphsToAdd.iterator();
        while (it.hasNext()) {
            String graphuri = (String) it.next();
            URL url = null;
            HttpURLConnection connection = null;
            url = new URL(graphuri);
            if (url != null && url.getProtocol().equals("file")) {
                client.read(url.openStream(), FileUtils.guessLang(url.toString()), url.toString());
                System.out.println("Successfully added: " + graphuri);
            } else if (url != null) {
                connection = (HttpURLConnection) url.openConnection();
                connection.addRequestProperty("accept", "application/rdf+xml ; q=1, " + "text/xml ; q=0.6 , text/rdf+n3 ; q=0.9 , " + "application/octet-stream ; q=0.5 , " + "application/xml q=0.5, application/rss+xml ; q=0.5 , " + "text/plain ; q=0.5, application/x-turtle ; q=0.5, " + "application/x-trig ; q=0.5");
                this.client.read(connection.getInputStream(), guessLanguage(connection), url.toString());
                System.out.println("Successfully added: " + graphuri);
            }
        }
    }
