    public static boolean load(String geoserverBaseUrl, String geoserverUsername, String geoserverPassword, int layerId, String layerName, String layerDescription) throws Exception {
        System.out.println("Creating layer in geoserver...");
        DefaultHttpClient httpClient = new DefaultHttpClient();
        httpClient.getCredentialsProvider().setCredentials(new AuthScope(null, -1), new UsernamePasswordCredentials(geoserverUsername, geoserverPassword));
        HttpPost post = new HttpPost(geoserverBaseUrl + RELATIVE_URL_FOR_LAYER_CREATION);
        post.setHeader("Content-type", "text/xml");
        post.setEntity(new StringEntity(String.format("<featureType><name>%s</name><nativeName>%s</nativeName><title>%s</title></featureType>", layerName, layerId, layerDescription)));
        HttpResponse response = httpClient.execute(post);
        if (response.getStatusLine().getStatusCode() != 201) {
            throw new RuntimeException("Error creating layer in geoserver: " + response.toString());
        }
        EntityUtils.consume(response.getEntity());
        HttpPut put = new HttpPut(String.format(geoserverBaseUrl + TEMPLATE_RELATIVE_URL_FOR_BORDER_SETTING, layerName));
        put.setHeader("Content-type", "text/xml");
        put.setEntity(new StringEntity("<layer><defaultStyle><name>generic_border</name></defaultStyle><enabled>true</enabled></layer>"));
        HttpResponse response2 = httpClient.execute(put);
        if (response2.getStatusLine().getStatusCode() != 200) {
            throw new RuntimeException("Error setting layer border in geoserver: " + response2.toString());
        }
        EntityUtils.consume(response2.getEntity());
        return true;
    }
