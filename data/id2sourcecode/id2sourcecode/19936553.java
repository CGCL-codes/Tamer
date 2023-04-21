    public static void main(String[] args) {
        try {
            if (args.length < 1) {
                System.out.println("Usage: SetupChannels channels.xml");
                System.exit(1);
            }
            Document d = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
            ChannelsReader channelsReader = new ChannelsReader();
            channelsReader.getChannelsList(d);
            NodeList channels = d.getElementsByTagName("channel");
            for (int i = 0; i < channels.getLength(); i++) {
                Element channel = (Element) channels.item(i);
                String id = channel.getAttribute("id");
                Element displayNameElement = (Element) channel.getElementsByTagName("display-name").item(0);
                String displayName = displayNameElement.getTextContent();
            }
            XMLWriter.writeXMLToFile(d, args[0]);
            System.out.println("Channel list saved to: " + args[0]);
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }
