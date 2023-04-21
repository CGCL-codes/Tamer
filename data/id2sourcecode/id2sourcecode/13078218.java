    public void readCatalog(Catalog catalog, String fileUrl) throws MalformedURLException, IOException, CatalogException {
        URL url = new URL(fileUrl);
        URLConnection urlCon = url.openConnection();
        readCatalog(catalog, urlCon.getInputStream());
    }
