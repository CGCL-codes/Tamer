    public Attribute[] getAttributeNames(String layerName) {
        Attribute[] attributes = null;
        String request = this.web3DService.getServiceEndPoint() + "?" + "SERVICE=" + service + "&" + "REQUEST=getLayerInfo&" + "VERSION=" + version + "&" + "Layer=" + layerName;
        if (Navigator.isVerbose()) {
            System.out.println(request);
        }
        URL url = null;
        try {
            InputStream urlIn;
            url = new URL(request);
            URLConnection urlc = url.openConnection();
            urlc.setReadTimeout(Navigator.TIME_OUT);
            if (web3DService.getEncoding() != null) {
                urlc.setRequestProperty("Authorization", "Basic " + web3DService.getEncoding());
            }
            urlIn = urlc.getInputStream();
            if (urlIn != null) {
                org.gdi3d.xnavi.services.w3ds.x030.GetLayerInfoLoader getLayerInfoLoader = new org.gdi3d.xnavi.services.w3ds.x030.GetLayerInfoLoader(urlIn);
                Vector<String> attributeNames = getLayerInfoLoader.getAttr();
                if (attributeNames != null) {
                    int numAttributeNames = attributeNames.size();
                    attributes = new Attribute[numAttributeNames];
                    for (int i = 0; i < numAttributeNames; i++) {
                        attributes[i] = new Attribute();
                        attributes[i].setName(attributeNames.get(i));
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
        return attributes;
    }
