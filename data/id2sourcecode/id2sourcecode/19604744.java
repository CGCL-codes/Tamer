    public String getResource(URL url) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
        Scanner scanner = new Scanner(in).useDelimiter("\\Z");
        return scanner.next();
    }
