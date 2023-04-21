    private boolean definitionExtraction() {
        if (definition == null || definition.getCachedURLs() == null || definition.getCachedURLs().isEmpty()) {
            return false;
        }
        URL url;
        try {
            if (definition.getCachedURLs().get(0).endsWith("pdf") || definition.getCachedURLs().get(0).endsWith("ppt") || definition.getCachedURLs().get(0).endsWith("doc")) {
                logger.error("Definitions from files not in HTML are not extended.");
                return false;
            }
            url = new URL(definition.getCachedURLs().get(0));
            HTMLEditorKit kit = new HTMLEditorKit();
            HTMLDocument doc = (HTMLDocument) kit.createDefaultDocument();
            doc.putProperty("IgnoreCharsetDirective", Boolean.TRUE);
            URLConnection con = url.openConnection();
            con.setConnectTimeout(1000);
            if (System.getProperty(ProxyInfo.PROXY_HOST) != null) {
                final String proxyUser = System.getProperty(ProxyInfo.PROXY_USERNAME);
                final String proxyPassword = System.getProperty(ProxyInfo.PROXY_PASSWORD);
                Authenticator.setDefault(new Authenticator() {

                    @Override
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(proxyUser, proxyPassword.toCharArray());
                    }
                });
            }
            con.connect();
            Reader HTMLReader = new InputStreamReader(con.getInputStream());
            kit.read(HTMLReader, doc, 0);
            String def = definition.getDefinition();
            if (def.endsWith("...")) {
                def = def.substring(0, def.length() - 3);
            }
            if (def.contains("[") || def.contains("]")) {
                StringTokenizer tokenizer = new StringTokenizer(def);
                String token;
                def = "";
                while (tokenizer.hasMoreTokens()) {
                    token = tokenizer.nextToken();
                    if (token.contains("[")) {
                        token = token.replace('[', ' ').trim();
                    } else if (token.contains("]")) {
                        token = token.replace(']', ' ').trim();
                    }
                    def = def + " " + token;
                }
            }
            ElementIterator iter = new ElementIterator(doc);
            Element elem;
            boolean run = true;
            while (run) {
                elem = iter.next();
                if (elem == null) {
                    run = false;
                    continue;
                }
                if (elem.getName().equals("content")) {
                    String element = elem.getDocument().getText(0, elem.getDocument().getLength() - 1);
                    int defLength = def.length();
                    if (defLength > 2 && element.contains(def.subSequence(0, defLength - 2))) {
                        int begin = element.indexOf(def.substring(0, defLength - 2));
                        boolean extending = true;
                        int i = begin + defLength - 2;
                        while (extending) {
                            i++;
                            if (element.charAt(i) == '.') {
                                extending = false;
                                String newDef = element.subSequence(begin, i) + ".";
                                if (def.equals(newDef)) {
                                    return false;
                                } else {
                                    definition.setDefinition(newDef);
                                    generateHTMLFormattedDefinition();
                                    return true;
                                }
                            }
                        }
                        break;
                    }
                }
            }
        } catch (MalformedURLException e) {
            return false;
        } catch (SocketTimeoutException e) {
            logger.error("Timeout during connection.", e);
            return false;
        } catch (IOException e) {
            return false;
        } catch (BadLocationException e) {
            return false;
        } catch (RuntimeException e) {
            logger.error("Error during fetching extended definitions. Definition is skipped.");
            return false;
        }
        return false;
    }
