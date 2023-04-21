    public static String transform(String xml) throws TransformerException {
        xml = Val.removeBOM(Val.chkStr(xml));
        StringReader reader = new StringReader(xml);
        StringWriter writer = new StringWriter();
        XmlIoUtil.transform(new StreamSource(reader), new StreamResult(writer), true);
        xml = Val.chkStr(writer.toString());
        return xml;
    }
