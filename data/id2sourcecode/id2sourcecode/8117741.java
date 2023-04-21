    private DTD getDTD(final Document doc) throws Exception {
        if (doc == null || doc.getDoctype() == null) return null;
        String dtdURL = doc.getDoctype().getSystemId();
        LOG.debug("dtdURL " + dtdURL + " systemId " + doc.getDoctype().getSystemId() + " xxx " + doc.getDoctype().getInternalSubset());
        if (dtdURL == null) return null;
        URL url = new URL(dtdURL);
        URLConnection connection = url.openConnection();
        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), "ISO-8859-1"));
        DTDParser dtdParser = new DTDParser(reader);
        DTD parsedDtd = dtdParser.parse();
        reader.close();
        return parsedDtd;
    }
