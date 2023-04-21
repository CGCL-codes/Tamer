    private void readVersion() {
        version = "Error reading version file";
        URL url = ResourceLocator.getResource(this, "/version.txt");
        if (url != null) {
            try {
                BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
                version = br.readLine();
                br.close();
            } catch (IOException e) {
            }
        }
    }
