    private static Element getGdataFeedElement(String urlStr) {
        try {
            URL url = new URL(urlStr);
            Document xmlDoc = new SAXBuilder().build(url.openStream());
            Element feed = xmlDoc.getRootElement();
            return feed;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (JDOMException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
