    static void getFriendDetails(String username) {
        try {
            Gui.getBalken().setValue(35);
            Gui.getBalken().setString("crawling Friendsprofiles");
            Gui.getBalken().paint(Gui.getBalken().getGraphics());
            URL url = new URL("http://ws.audioscrobbler.com/1.0/user/" + username + "/friends.xml");
            URLConnection con = url.openConnection();
            InputStream is = con.getInputStream();
            Document document = null;
            try {
                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                DocumentBuilder builder = factory.newDocumentBuilder();
                document = builder.parse(is);
            } catch (SAXParseException error) {
                System.out.print("1");
            } catch (ParserConfigurationException pce) {
                System.out.print("2");
            } catch (IOException ioe) {
                System.out.print("3");
            } catch (Throwable t) {
                System.out.print("4");
            }
            NodeList taglist_user = document.getElementsByTagName("user");
            for (int i = 0; i < taglist_user.getLength(); i++) {
                Node tag_user = taglist_user.item(i);
                if (tag_user.hasAttributes()) {
                    NamedNodeMap attributes_map = tag_user.getAttributes();
                    Node user_node = attributes_map.item(0);
                    User.getUserProfile_Stop(user_node.getNodeValue().replace(" ", "%20"));
                    DB_Friends.addFriendRelation(username, user_node.getNodeValue().replace(" ", "%20"));
                }
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
            e.printStackTrace();
        }
    }
