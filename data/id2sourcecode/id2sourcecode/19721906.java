    private TagTreeNodeInterface getServerTreeNode() {
        String species = speciesComboBox.getSelectedItem().toString();
        String version = getVersionComboBox().getSelectedItem().toString();
        SearchPositionConnector searchPositionConnector = new SearchPositionConnector();
        TagTreeNode root = new TagTreeNode("root");
        getChildNode(root, LOCAL + " - " + initDataSource.getAliasName());
        for (String server : initDataSource.getParents()) {
            String serverURL = server + SERVERURL;
            URL url = searchPositionConnector.getXMLStream(serverURL, species, version);
            SAXSearchPositionParser handler;
            SAXParserFactory parserFactory = SAXParserFactory.newInstance();
            handler = new SAXSearchPositionParser();
            String parentAlias = null;
            try {
                SAXParser saxParser = parserFactory.newSAXParser();
                saxParser.parse(url.openStream(), handler);
                LinkedList<TMGFF> tagList = handler.getInformation();
                for (TMGFF tag : tagList) {
                    String serverName = tag.getServer();
                    if (serverName == null) {
                        if (parentAlias == null) {
                            parentAlias = getParentAlias(server);
                        }
                        serverName = parentAlias;
                    }
                    getChildNode(root, serverName).addChild(new TagTreeNode(tag.getSource()));
                }
            } catch (SAXException ex) {
                logger.error("Error reading xml from SearchPosition servlet url:" + url, ex);
            } catch (ParserConfigurationException ex) {
                logger.error("Error reading xml from SearchPosition servlet url:" + url, ex);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(ServicesPanel.this, "Could not connect to:" + serverURL, "Server connection error(IOException)", JOptionPane.ERROR_MESSAGE);
            } catch (NullPointerException ex) {
                JOptionPane.showMessageDialog(ServicesPanel.this, "Could not connect to:" + serverURL, "Server connection error", JOptionPane.ERROR_MESSAGE);
            }
        }
        return root;
    }
