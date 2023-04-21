        public void send(Item item) throws XMLException {
            if (level >= 0) {
                switch(item.getType()) {
                    case ElementItem:
                        level++;
                        break;
                    case ElementEndItem:
                        level--;
                }
            } else {
                if (item.getType() == Item.ItemType.ElementItem && ((Named) item).getName().equals(XINCLUDE)) {
                    level = 0;
                    if (output == null) {
                        return;
                    }
                    Element xinclude = (Element) item;
                    String href = xinclude.getAttributeValue("href");
                    if (href == null) {
                        throw new XMLException("Missing 'href' attribute on xinclude.");
                    }
                    URI location = xinclude.getBaseURI().resolve(href);
                    String parse = xinclude.getAttributeValue("parse");
                    boolean asXML = !"text".equals(parse);
                    if (asXML) {
                        try {
                            XInclude nextXInclude = new XInclude();
                            nextXInclude.attach(new RemoveDocumentFilter(output));
                            loader.generate(location, nextXInclude);
                        } catch (IOException ex) {
                            throw new XMLException("Cannot load xincluded document " + location, ex);
                        }
                    } else {
                        try {
                            URL url = location.toURL();
                            URLConnection connection = url.openConnection();
                            InputStream is = connection.getInputStream();
                            int statusCode = HttpURLConnection.HTTP_OK;
                            if (connection instanceof HttpURLConnection) {
                                statusCode = ((HttpURLConnection) connection).getResponseCode();
                            }
                            String contentType = connection.getContentType();
                            String charset = "UTF-8";
                            if (contentType != null) {
                                int semicolon = contentType.indexOf(';');
                                if (semicolon >= 0) {
                                    String type = contentType.substring(0, semicolon);
                                    String rest = contentType.substring(semicolon + 1);
                                    int equals = rest.indexOf('=');
                                    if (equals >= 0 && rest.substring(0, equals).equals("charset")) {
                                        charset = rest.substring(equals + 1);
                                    }
                                    contentType = type;
                                }
                            }
                            Reader input = new InputStreamReader(is, charset);
                            StringBuilder builder = new StringBuilder();
                            char[] buffer = new char[8192];
                            int len;
                            while ((len = input.read(buffer)) > 0) {
                                builder.append(buffer, 0, len);
                            }
                            output.send(item.getInfoset().createItemConstructor().createCharacters(builder.toString()));
                        } catch (IOException ex) {
                            throw new XMLException("Cannot load xincluded text document " + location, ex);
                        }
                    }
                } else {
                    if (output != null) {
                        output.send(item);
                    }
                }
            }
        }
