    protected void Construire() {
        URL urlCatalog;
        try {
            urlCatalog = new URL("http://" + ip + ":" + port + "/catalogue.txt");
            connection = urlCatalog.openConnection();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
