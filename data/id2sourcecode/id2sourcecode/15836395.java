    private boolean applyXsltFile(String xsltFileName, StringWriter writer, Result result) throws IOException {
        URL xsltUrl = getResource(xsltFileName);
        if (xsltUrl == null) {
            logger.severe("Can't find " + xsltFileName + " as resource.");
            throw new IllegalArgumentException("Can't find " + xsltFileName + " as resource.");
        }
        InputStream xsltStream = xsltUrl.openStream();
        Source xsltSource = new StreamSource(xsltStream);
        try {
            StringReader reader = new StringReader(writer.getBuffer().toString());
            TransformerFactory transFact = TransformerFactory.newInstance();
            Transformer trans = transFact.newTransformer(xsltSource);
            trans.setParameter("date", DateFormat.getDateInstance(DateFormat.SHORT).format(new Date()));
            trans.transform(new StreamSource(reader), result);
            return true;
        } catch (Exception e) {
            freemind.main.Resources.getInstance().logException(e);
            return false;
        }
    }
