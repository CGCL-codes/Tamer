            public void run() {
                try {
                    HttpsURLConnection connection = (HttpsURLConnection) urlGenerator(DreamHostCommands.CMD_DOMAIN_LIST_DOMAINS, null).openConnection();
                    String response = getResponse(connection);
                    Document document = builder.parse(new ByteArrayInputStream(response.getBytes()));
                    String result = document.getElementsByTagName("result").item(0).getTextContent();
                    logged = result.equals("success");
                } catch (SAXException ex) {
                    Logger.getLogger(DreamHostConnector.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    Logger.getLogger(DreamHostConnector.class.getName()).log(Level.SEVERE, null, ex);
                } finally {
                    if (isLogged()) {
                        NbPreferences.forModule(DreamHostConnector.class).put("username", getUsername());
                        NbPreferences.forModule(DreamHostConnector.class).put("key", getKey());
                    }
                    handle.finish();
                    working = false;
                    fireChangeEvent();
                }
            }
