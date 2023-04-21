    public String parse(String text) throws UnsupportedEncodingException, IOException, TransformerException {
        javax.xml.transform.Source xmlSource = new javax.xml.transform.stream.StreamSource(new StringReader(text));
        javax.xml.transform.Source xsltSource = new javax.xml.transform.stream.StreamSource(new InputStreamReader(url.openStream(), "euc-kr"));
        StringWriter sw = new StringWriter();
        javax.xml.transform.Result result = new javax.xml.transform.stream.StreamResult(sw);
        javax.xml.transform.TransformerFactory transFact = javax.xml.transform.TransformerFactory.newInstance();
        javax.xml.transform.Transformer trans;
        trans = transFact.newTransformer(xsltSource);
        trans.transform(xmlSource, result);
        return sw.getBuffer().toString();
    }
