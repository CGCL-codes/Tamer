    DOMSource resolveDOM(Source source, String base, String href) throws TransformerException {
        if (source != null && source instanceof DOMSource) {
            return (DOMSource) source;
        }
        String systemId = (source == null) ? null : source.getSystemId();
        long lastModified = 0L, lastLastModified = 0L;
        try {
            URL url = resolveURL(systemId, base, href);
            Node node = null;
            InputStream in = null;
            if (source instanceof StreamSource) {
                StreamSource ss = (StreamSource) source;
                in = ss.getInputStream();
                if (in == null) {
                    Reader reader = ss.getReader();
                    if (reader != null) {
                        in = new ReaderInputStream(reader);
                    }
                }
            }
            if (in == null) {
                if (url != null) {
                    systemId = url.toString();
                    node = (Node) nodeCache.get(systemId);
                    URLConnection conn = url.openConnection();
                    Long llm = (Long) lastModifiedCache.get(systemId);
                    if (llm != null) {
                        lastLastModified = llm.longValue();
                        conn.setIfModifiedSince(lastLastModified);
                    }
                    conn.connect();
                    lastModified = conn.getLastModified();
                    if (node != null && lastModified > 0L && lastModified <= lastLastModified) {
                        return new DOMSource(node, systemId);
                    } else {
                        in = conn.getInputStream();
                        nodeCache.put(systemId, node);
                        lastModifiedCache.put(systemId, new Long(lastModified));
                    }
                } else {
                    throw new TransformerException("can't resolve URL: " + systemId);
                }
            }
            InputSource input = new InputSource(in);
            input.setSystemId(systemId);
            DocumentBuilder builder = getDocumentBuilder();
            node = builder.parse(input);
            return new DOMSource(node, systemId);
        } catch (IOException e) {
            throw new TransformerException(e);
        } catch (SAXException e) {
            throw new TransformerException(e);
        }
    }
