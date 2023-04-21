    public void readCatalog(Catalog catalog, String fileUrl) throws MalformedURLException, IOException {
        URL catURL = null;
        try {
            catURL = new URL(fileUrl);
        } catch (MalformedURLException e) {
            catURL = new URL("file:///" + fileUrl);
        }
        URLConnection urlCon = catURL.openConnection();
        try {
            readCatalog(catalog, urlCon.getInputStream());
        } catch (FileNotFoundException e) {
            catalog.getCatalogManager().debug.message(1, "Failed to load catalog, file not found", catURL.toString());
        }
    }
