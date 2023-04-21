    public static void toXML(URL dest, Marshallable vo) throws PrintException {
        XMLStreamWriter xmlStreamWriter = null;
        OutputStream os = null;
        try {
            URLConnection urlConnection = dest.openConnection();
            urlConnection.setDoOutput(true);
            os = urlConnection.getOutputStream();
            xmlStreamWriter = XMLOutputFactory.newInstance().createXMLStreamWriter(os, "UTF-8");
            vo.toXML(xmlStreamWriter, true);
        } catch (XMLStreamException e) {
            throw new PrintException(e.getMessage(), e);
        } catch (IOException e) {
            throw new PrintException(e.getMessage(), e);
        } finally {
            IOUtil.flushAndClose(xmlStreamWriter);
            IOUtil.close(os);
        }
    }
