    public Vector<String> getAttributeValues(String layerName, String columnname) {
        Vector<String> values = null;
        String request = null;
        request = this.web3DService.getServiceEndPoint() + "?" + "SERVICE=" + service + "&" + "REQUEST=getLayerInfo&" + "VERSION=" + version + "&" + "Layer=" + layerName + "&" + "columnName=" + columnname;
        if (Navigator.isVerbose()) {
            System.out.println(request);
        }
        URL url = null;
        try {
            InputStream urlIn;
            url = new URL(request);
            URLConnection conn = url.openConnection();
            if (web3DService.getEncoding() != null) {
                conn.setRequestProperty("Authorization", "Basic " + web3DService.getEncoding());
            }
            urlIn = conn.getInputStream();
            if (urlIn != null) {
                org.gdi3d.xnavi.services.w3ds.x030.GetLayerInfoLoader getLayerInfoLoader = new org.gdi3d.xnavi.services.w3ds.x030.GetLayerInfoLoader(urlIn);
                Vector<String> attributeValues = getLayerInfoLoader.getValue();
                if (attributeValues != null) {
                    int numAttributeValues = attributeValues.size();
                    values = new Vector<String>();
                    for (int i = 0; i < numAttributeValues; i++) {
                        values.add(attributeValues.get(i));
                    }
                }
                urlIn.close();
            }
        } catch (NoRouteToHostException e) {
            e.printStackTrace();
        } catch (java.lang.NullPointerException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return values;
    }
