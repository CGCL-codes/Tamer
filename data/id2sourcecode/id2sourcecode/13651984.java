    public static String prettyPrint(String document) {
        try {
            SAXReader xmlReader = new SAXReader(false);
            InputSource is = new InputSource(new StringReader(document));
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            OutputFormat format = OutputFormat.createPrettyPrint();
            XMLWriter writer = new XMLWriter(out, format);
            writer.write(xmlReader.read(is));
            writer.close();
            return new String(out.toByteArray(), "UTF-8");
        } catch (Exception e) {
            document = e.getMessage();
        }
        return document;
    }
