    public static void load(String layerName, File geotiffFile, File sldFile, String geoserverBaseUrl, String geoserverUsername, String geoserverPassword) throws Exception {
        DefaultHttpClient httpClient = new DefaultHttpClient();
        httpClient.getCredentialsProvider().setCredentials(new AuthScope(null, -1), new UsernamePasswordCredentials(geoserverUsername, geoserverPassword));
        System.out.println("Creating layer in geoserver...");
        HttpPut createLayerPut = new HttpPut(String.format(geoserverBaseUrl + "/rest/workspaces/ALA/coveragestores/%s/external.geotiff", layerName));
        createLayerPut.setHeader("Content-type", "text/plain");
        createLayerPut.setEntity(new StringEntity(geotiffFile.toURI().toURL().toString()));
        HttpResponse createLayerResponse = httpClient.execute(createLayerPut);
        if (createLayerResponse.getStatusLine().getStatusCode() != 201) {
            throw new RuntimeException("Error creating layer in geoserver: " + createLayerResponse.toString());
        }
        EntityUtils.consume(createLayerResponse.getEntity());
        System.out.println("Creating style in geoserver");
        HttpPost createStylePost = new HttpPost(geoserverBaseUrl + "/rest/styles");
        createStylePost.setHeader("Content-type", "text/xml");
        createStylePost.setEntity(new StringEntity(String.format("<style><name>%s_style</name><filename>%s.sld</filename></style>", layerName, layerName)));
        HttpResponse createStyleResponse = httpClient.execute(createStylePost);
        if (createStyleResponse.getStatusLine().getStatusCode() != 201) {
            throw new RuntimeException("Error creating style in geoserver: " + createStyleResponse.toString());
        }
        EntityUtils.consume(createStyleResponse.getEntity());
        System.out.println("Uploading sld file to geoserver");
        String sldData = FileUtils.readFileToString(sldFile);
        HttpPut uploadSldPut = new HttpPut(String.format(geoserverBaseUrl + "/rest/styles/%s_style", layerName));
        System.out.println(uploadSldPut.getURI().toString());
        System.out.println(sldData);
        uploadSldPut.setHeader("Content-type", "application/vnd.ogc.sld+xml");
        uploadSldPut.setEntity(new StringEntity(sldData));
        HttpResponse uploadSldResponse = httpClient.execute(uploadSldPut);
        if (uploadSldResponse.getStatusLine().getStatusCode() != 200) {
            throw new RuntimeException("Error uploading sld file geoserver: " + uploadSldResponse.toString());
        }
        EntityUtils.consume(uploadSldResponse.getEntity());
        System.out.println("Setting default style in geoserver");
        HttpPut setDefaultStylePut = new HttpPut(String.format(geoserverBaseUrl + "/rest/layers/ALA:%s", layerName));
        setDefaultStylePut.setHeader("Content-type", "text/xml");
        setDefaultStylePut.setEntity(new StringEntity(String.format("<layer><enabled>true</enabled><defaultStyle><name>%s_style</name></defaultStyle></layer>", layerName)));
        HttpResponse setDefaultStyleResponse = httpClient.execute(setDefaultStylePut);
        if (setDefaultStyleResponse.getStatusLine().getStatusCode() != 200) {
            throw new RuntimeException("Setting default style in geoserver: " + setDefaultStyleResponse.toString());
        }
        EntityUtils.consume(setDefaultStyleResponse.getEntity());
    }
