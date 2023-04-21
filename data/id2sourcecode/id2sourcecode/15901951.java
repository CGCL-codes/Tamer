    protected Transformer getTransformer(URL url) throws Exception {
        TransformerFactory tf = TransformerFactory.newInstance();
        javax.xml.transform.Source source = new StreamSource(url.openStream());
        return tf.newTransformer(source);
    }
