    public static void parseRDF(URL url, IRDFContainer sink) throws RDFException {
        try {
            ARP arp = new ARP();
            arp.setStatementHandler(new StatementHandlerImpl(sink));
            InputStream is = url.openStream();
            arp.load(is, url.toExternalForm());
            is.close();
        } catch (IOException ioe) {
            throw new RDFException("I/O error.", ioe);
        } catch (org.xml.sax.SAXException saxe) {
            throw new RDFException("Malformed RDF.", saxe);
        }
    }
