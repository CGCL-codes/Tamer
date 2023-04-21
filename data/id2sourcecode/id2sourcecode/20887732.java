    private static Document getXml(String urlString) {
        Document doc = null;
        HttpURLConnection uc = null;
        try {
            URL url = new URL(urlString);
            uc = (HttpURLConnection) url.openConnection();
            if (StaticObj.PROXY_ENABLED) {
                Properties systemSettings = System.getProperties();
                systemSettings.put("http.proxyHost", StaticObj.PROXY_URL);
                systemSettings.put("http.proxyPort", StaticObj.PROXY_PORT);
                System.setProperties(systemSettings);
                sun.misc.BASE64Encoder encoder = new sun.misc.BASE64Encoder();
                String encoded = new String(encoder.encode(new String(StaticObj.PROXY_USERNAME + ":" + StaticObj.PROXY_PASSWORD).getBytes()));
                uc.setRequestProperty("Proxy-Authorization", "Basic " + encoded);
            }
            BufferedReader ir = null;
            if (uc.getInputStream() != null) {
                ir = new BufferedReader(new InputStreamReader(uc.getInputStream(), "ISO8859_1"));
                SAXBuilder builder = new SAXBuilder();
                doc = builder.build(ir);
            }
        } catch (IOException io) {
            LogManager.getInstance().getLogger().error(io);
            if (StaticObj.DEBUG) io.printStackTrace();
        } catch (Exception e) {
            LogManager.getInstance().getLogger().error(e);
            if (StaticObj.DEBUG) e.printStackTrace();
            System.out.println("No Data found!");
        } finally {
            try {
                uc.disconnect();
            } catch (Exception ex) {
            }
            return doc;
        }
    }
