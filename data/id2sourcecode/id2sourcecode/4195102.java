    public U read(String urlToFile, TypeAdapter adapter) {
        try {
            URL url = new URL(urlToFile);
            InputStream stream = url.openStream();
            return readFromStream(adapter, stream);
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
