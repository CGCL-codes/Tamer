    private void handleXInclude(final Element elem) {
        currentElement.removeChild(elem);
        if ("include".equals(elem.getLocalName())) {
            inXInclude++;
            String href = elem.getAttribute("href");
            if ((href == null) || "".equals(href.trim())) {
                href = null;
            }
            String parse = elem.getAttribute("parse");
            if ((parse == null) || "".equals(parse.trim())) {
                parse = "xml";
            }
            String xpointer = elem.getAttribute("xpointer");
            if ((xpointer == null) || "".equals(xpointer.trim())) {
                xpointer = null;
            }
            String encoding = elem.getAttribute("encoding");
            if ((encoding == null) || "".equals(encoding.trim())) {
                encoding = null;
            }
            String accept = elem.getAttribute("accept");
            if ((accept == null) || "".equals(accept.trim())) {
                accept = null;
            }
            String accept_language = elem.getAttribute("accept-language");
            if ((accept_language == null) || "".equals(accept_language.trim())) {
                accept_language = null;
            }
            if (href != null) {
                if (href.indexOf(":/") == -1) {
                    if (href.startsWith("/")) {
                        href = href.substring(1);
                    }
                    href = documentURI + href;
                }
                if (localParser.get() == null) {
                    localParser.set(new CShaniDomParser());
                }
                CShaniDomParser p = (CShaniDomParser) localParser.get();
                InputStream in = null;
                try {
                    URL url = new URL(href);
                    URLConnection connection = url.openConnection();
                    if (accept != null) {
                        connection.addRequestProperty("Accept", accept);
                    }
                    if (accept_language != null) {
                        connection.addRequestProperty("Accept-Language", accept_language);
                    }
                    in = connection.getInputStream();
                    ADocument doc = null;
                    if (encoding != null) {
                        doc = (ADocument) p.parse(new InputStreamReader(in, encoding));
                    } else {
                        doc = (ADocument) p.parse(in);
                    }
                    if (xpointer == null) {
                        currentElement.appendChild(doc.getDocumentElement());
                    } else {
                        XPath xpath = new DOMXPath(xpointer);
                        for (Iterator it = doc.getNamespaceList().iterator(); it.hasNext(); ) {
                            CNamespace ns = (CNamespace) it.next();
                            xpath.addNamespace(ns.getPrefix() == null ? "" : ns.getPrefix(), ns.getNamespaceURI());
                        }
                        List result = xpath.selectNodes(doc.getDocumentElement());
                        for (final Iterator it = result.iterator(); it.hasNext(); ) {
                            final Node node = (Node) it.next();
                            currentElement.appendChild(node);
                        }
                    }
                } catch (final Exception e) {
                    xiFallbackFlag++;
                } finally {
                    try {
                        in.close();
                        in = null;
                    } catch (final Exception ignore) {
                    }
                }
            }
        }
    }
