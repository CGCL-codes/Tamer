    public void writeShapeFileTo(InputStream inputStream, String dsName) {
        List<GeoServer> geoservers = geoServerDao.findAllGeoServers();
        if (geoservers.size() > 1) throw new FenixException("Number of registered Geoservers is more than one (" + geoservers.size() + "). This is not foreseen for now and therefore BOEM!");
        if (geoservers.size() == 0) throw new FenixException("No Geoserver yet published in Fenix at this point. Should be there, something is wrong (usually it is published at startup of Fenix)");
        GeoServer geoserver = geoservers.get(0);
        System.out.println("code to talk with Geoserver RestService to write the shapefile to the database.");
        try {
            System.out.println("------------- Storing Layer to : " + geoserver.getBaseUrl() + "/rest/folders/" + dsName + "/file.zip");
            URL url = new URL(geoserver.getBaseUrl() + "/rest/folders/" + dsName + "/file.zip");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setDoOutput(true);
            con.setDoInput(true);
            con.setRequestMethod("PUT");
            OutputStream outputStream = con.getOutputStream();
            copyInputStream(inputStream, outputStream);
            if (con.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStreamReader is = new InputStreamReader(con.getInputStream());
                String response = readIs(is);
                is.close();
                System.out.println(response);
                geoserverImporter.importAddedLayer();
            } else {
                System.out.println(con.getResponseCode());
                System.out.println(con.getResponseMessage());
                throw new FenixException(con.getResponseCode() + " - " + con.getResponseMessage());
            }
        } catch (MalformedURLException e) {
            throw new FenixException(e);
        } catch (IOException e) {
            throw new FenixException(e);
        }
    }
