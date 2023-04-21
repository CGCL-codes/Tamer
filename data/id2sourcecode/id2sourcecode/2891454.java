    private String parse(String relativeURL, ValueHandler h) {
        String url = url(relativeURL);
        try {
            SAXParser p = SAXParserFactory.newInstance().newSAXParser();
            p.parse(new URL(url).openStream(), h);
        } catch (ValueHandler.Done d) {
        } catch (ParserConfigurationException e) {
            barf(e);
        } catch (SAXException e) {
            barf(e);
        } catch (MalformedURLException e) {
            barf(e);
        } catch (IOException e) {
            barf(e);
        }
        return h.getValue();
    }
