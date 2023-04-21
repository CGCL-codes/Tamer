    private void load(URL url) throws XMLException, IOException {
        try {
            InputSource in;
            in = new InputSource(url.openStream());
            load(in);
        } catch (XMLException xe) {
            throw xe;
        } catch (IOException ioe) {
            throw ioe;
        } catch (Exception e) {
            e.printStackTrace();
            throw new XMLException("Failed to load XML document " + url);
        }
        return;
    }
