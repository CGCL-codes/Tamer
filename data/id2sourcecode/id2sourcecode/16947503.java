    public Transformer createTransformer(URL url) throws NAFException {
        InputStream in = null;
        try {
            in = url.openStream();
            Transformer t = tFactory.newTransformer(new StreamSource(in));
            return t;
        } catch (IOException ex) {
            throw new NAFException("Error loading style sheet URL \"" + url + "\"", ex);
        } catch (TransformerConfigurationException ex) {
            throw new NAFException("Error configuring XML transformer", ex);
        } finally {
            if (in != null) try {
                in.close();
            } catch (IOException ignored) {
            }
        }
    }
