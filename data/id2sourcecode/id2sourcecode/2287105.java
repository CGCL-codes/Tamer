    protected void writeAMF3Xml(Document doc) throws IOException {
        if (debugMore) logMore.debug("writeAMF3Xml(doc=%s)", doc);
        byte xmlType = AMF3_XMLSTRING;
        Channel channel = getChannel();
        if (channel != null && channel.isLegacyXmlSerialization()) xmlType = AMF3_XML;
        write(xmlType);
        int index = indexOfStoredObjects(doc);
        if (index >= 0) writeAMF3IntegerData(index << 1); else {
            addToStoredObjects(doc);
            byte[] bytes = xmlUtil.toString(doc).getBytes("UTF-8");
            writeAMF3IntegerData((bytes.length << 1) | 0x01);
            write(bytes);
        }
    }
