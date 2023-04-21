    private void initialFeed(RSSFeed feed) throws IOException, IllegalArgumentException, IllegalAccessException, InvocationTargetException, ClassNotFoundException {
        if (!xmlFile.exists()) {
            Document document = DocumentHelper.createDocument();
            Element rootElement = document.addElement("rss");
            rootElement.addAttribute("version", "2.0");
            Element channelElement = rootElement.addElement("channel");
            if (feed != null && feed.getChannel() != null) {
                addChannelAttribute(channelElement, feed.getChannel());
            }
            OutputFormat format = OutputFormat.createPrettyPrint();
            XMLWriter writer = new XMLWriter(new FileWriter(xmlFile), format);
            writer.write(document);
            writer.close();
        }
    }
