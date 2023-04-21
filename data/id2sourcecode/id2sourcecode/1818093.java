    public static void getUserProfile(String username) {
        try {
            URL url = new URL("http://ws.audioscrobbler.com/1.0/user/" + username + "/profile.xml");
            URLConnection con = url.openConnection();
            InputStream is = con.getInputStream();
            Document document = null;
            try {
                Gui.getBalken().setValue(15);
                Gui.getBalken().paint(Gui.getBalken().getGraphics());
                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                DocumentBuilder builder = factory.newDocumentBuilder();
                document = builder.parse(is);
                System.out.println("Datei existiert ");
                NodeList taglist_profile = document.getElementsByTagName("profile");
                Node tag_profile = taglist_profile.item(0);
                String id_name = new String();
                String username_name = new String();
                if (tag_profile.hasAttributes()) {
                    NamedNodeMap nnm = tag_profile.getAttributes();
                    Node tag_id = nnm.getNamedItem("id");
                    id_name = tag_id.getNodeValue();
                    Node tag_username = nnm.getNamedItem("username");
                    username_name = tag_username.getNodeValue();
                }
                NodeList taglist_avatar = document.getElementsByTagName("avatar");
                Node tag_avatar = taglist_avatar.item(0);
                String avatar_name = tag_avatar.getFirstChild().getNodeValue();
                System.out.println("avatar = " + avatar_name);
                NodeList taglist_url = document.getElementsByTagName("url");
                Node tag_url = taglist_url.item(0);
                String url_name = tag_url.getFirstChild().getNodeValue();
                System.out.println("url = " + url_name);
                NodeList taglist_realname = document.getElementsByTagName("realname");
                String realname_name;
                if (taglist_realname.getLength() != 0) {
                    Node tag_realname = taglist_realname.item(0);
                    realname_name = tag_realname.getFirstChild().getNodeValue();
                    System.out.println("realname  = " + realname_name);
                } else {
                    realname_name = "unknown";
                }
                NodeList taglist_age = document.getElementsByTagName("age");
                String age_name;
                if (taglist_age.getLength() != 0) {
                    Node tag_age = taglist_age.item(0);
                    age_name = tag_age.getFirstChild().getNodeValue();
                    System.out.println("age = " + age_name);
                } else {
                    age_name = "0";
                }
                NodeList taglist_gender = document.getElementsByTagName("gender");
                String gender_name;
                if (taglist_gender.getLength() != 0) {
                    Node tag_gender = taglist_gender.item(0);
                    gender_name = tag_gender.getFirstChild().getNodeValue();
                    System.out.println("gender = " + gender_name);
                } else {
                    gender_name = "?";
                }
                NodeList taglist_country = document.getElementsByTagName("country");
                String country_name;
                if (taglist_country.getLength() != 0) {
                    Node tag_country = taglist_country.item(0);
                    country_name = tag_country.getFirstChild().getNodeValue();
                    System.out.println("country = " + country_name);
                } else {
                    country_name = "unknown";
                }
                NodeList taglist_playcount = document.getElementsByTagName("playcount");
                Node tag_playcount = taglist_playcount.item(0);
                String playcount_name = tag_playcount.getFirstChild().getNodeValue();
                System.out.println("playcount = " + playcount_name);
                NodeList taglist_registered = document.getElementsByTagName("registered");
                Node tag_registered = taglist_registered.item(0);
                String registered_name = tag_registered.getFirstChild().getNodeValue();
                System.out.println("registered = " + registered_name);
                String crawlflag = "1";
                System.out.println("TEST CHECK USER1 : " + DB_User.checkUser(username));
                if (!DB_User.checkUser(username)) {
                    DB_User.deleteUser(username);
                }
                DB_User.addUser(id_name, realname_name, username, age_name, gender_name, url_name, avatar_name, country_name, registered_name, playcount_name, crawlflag);
                Friends.getFriendDetails(username);
                Neighbours.getNeighbourDetails(username);
                Groups.getGroups(username);
                Groups.getGroupsImage(username);
                Gui.getBalken().setValue(100);
                Gui.getBalken().setString("...starting");
                Gui.getBalken().paint(Gui.getBalken().getGraphics());
                ActionEventHandler.changer();
                ActionEventHandler.performSearch();
            } catch (SAXParseException error) {
                System.out.print("E1");
            } catch (ParserConfigurationException pce) {
                System.out.print("E2");
            } catch (IOException ioe) {
                System.out.print("E3");
            } catch (Throwable t) {
                System.out.print("E4");
            }
        } catch (MalformedURLException e) {
            System.out.println("User existiert nicht bei lastfm - vermutlich vertippt ;-)");
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
            e.printStackTrace();
        }
    }
