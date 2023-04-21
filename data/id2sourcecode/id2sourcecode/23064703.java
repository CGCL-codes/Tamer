    public static String prettifyXML(String xml) {
        String result = xml;
        try {
            StringReader reader = new StringReader(xml);
            StringWriter writer = new StringWriter();
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.setOutputProperty(OutputKeys.METHOD, "xml");
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.transform(new StreamSource(reader), new StreamResult(writer));
            writer.close();
            result = writer.toString();
        } catch (TransformerFactoryConfigurationError error) {
        } catch (TransformerException error) {
        } catch (IOException error) {
        }
        return result;
    }
