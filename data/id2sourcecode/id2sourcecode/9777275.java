    public static void main(String[] args) {
        ChannelsReader reader = new ChannelsReader();
        try {
            Set channels = reader.read();
            Document d = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
            reader.getChannelsList(d);
            String filename = "xmltv.xml";
            if (args.length > 0) {
                filename = args[0];
            }
            XMLWriter.writeXMLToFile(d, filename);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
