    private static void transform(String xml, String xsl, URL dest) throws PrintException {
        Writer result = null;
        URLConnection urlConnection = null;
        try {
            urlConnection = dest.openConnection();
            urlConnection.setDoOutput(true);
            result = new OutputStreamWriter(urlConnection.getOutputStream(), "utf-8");
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer;
            transformer = transformerFactory.newTransformer(new StreamSource(new StringReader(xsl)));
            transformer.transform(new StreamSource(new StringReader(xml)), new StreamResult(result));
            if (log.isDebugEnabled()) {
                log.debug("transformed");
            }
        } catch (IOException e) {
            throw new PrintException(e.getMessage(), e);
        } catch (TransformerConfigurationException e) {
            throw new PrintException(e.getMessage(), e);
        } catch (TransformerException e) {
            throw new PrintException(e.getMessage(), e);
        } finally {
            if (result != null) IOUtil.close(result);
        }
    }
