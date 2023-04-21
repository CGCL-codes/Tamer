    @Override
    public void toXML(XMLStreamWriter streamWriter, boolean standalone) throws PrintException {
        try {
            XMLHelper.writeStartElement(streamWriter, "block");
            XMLHelper.writeAttribute(streamWriter, "id", getId());
            XMLHelper.writeAttribute(streamWriter, "version", getVersion());
            XMLHelper.writeRawData(streamWriter, new String(ContentReader.read(getURL()), "UTF-8"));
            XMLHelper.writeEndElement(streamWriter, "block");
        } catch (XMLStreamException e) {
            throw new PrintException(e.getMessage() + " " + getURL(), e);
        } catch (UnsupportedEncodingException e) {
            throw new PrintException(e.getMessage() + " " + getURL(), e);
        } catch (IOException e) {
            throw new PrintException(e.getMessage() + " " + getURL(), e);
        }
    }
