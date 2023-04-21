    protected static InputStream getInputStream(Item item, XQueryContext context) throws XPathException, MalformedURLException, IOException {
        if (item.getType() == Type.ANY_URI) {
            LOG.debug("Streaming xs:anyURI");
            String url = item.getStringValue();
            if (url.startsWith("/")) {
                url = "xmldb:exist://" + url;
            }
            return new URL(url).openStream();
        } else if (item.getType() == Type.ELEMENT || item.getType() == Type.DOCUMENT) {
            LOG.debug("Streaming element or document node");
            Serializer serializer = context.getBroker().newSerializer();
            NodeValue node = (NodeValue) item;
            return new NodeInputStream(serializer, node);
        } else {
            LOG.error("Wrong item type " + Type.getTypeName(item.getType()));
            throw new XPathException("wrong item type " + Type.getTypeName(item.getType()));
        }
    }
