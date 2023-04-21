    public QuickUI(URL url, EventHandler eventHandler) throws QuickUIException {
        handler = eventHandler;
        frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainPanel = new Box(BoxLayout.Y_AXIS);
        mainPanel.setBorder(new EmptyBorder(3, 3, 10, 3));
        MyHandler xmlHandler = new MyHandler();
        SAXParserFactory parserFactory = SAXParserFactory.newInstance();
        SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        try {
            Schema schema = schemaFactory.newSchema(this.getClass().getResource("quickui.xsd"));
            parserFactory.setSchema(schema);
            SAXParser parser = parserFactory.newSAXParser();
            parser.parse(url.openStream(), xmlHandler);
            if (xmlHandler.getErrorsExists()) {
                throw new QuickUIException("Validation error:\n" + xmlHandler.getMessages());
            }
        } catch (ParserConfigurationException e) {
            throw new QuickUIException(e);
        } catch (SAXException e) {
            throw new QuickUIException(e);
        } catch (IOException e) {
            throw new QuickUIException(e);
        }
        frame.add(mainPanel);
        frame.pack();
        frame.setSize(Math.max(500, frame.getSize().width), frame.getSize().height);
        int x = (frame.getGraphicsConfiguration().getBounds().width - frame.getWidth()) / 2;
        int y = (frame.getGraphicsConfiguration().getBounds().height - frame.getHeight()) / 2;
        frame.setLocation(x, y);
        frame.setVisible(true);
    }
