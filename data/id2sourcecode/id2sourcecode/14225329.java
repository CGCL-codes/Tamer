                public void run() {
                    try {
                        String meta = prefs.getMetaServer().toString();
                        URL url = new URL(meta + "?version=" + referenceManager.getSettingAttribute("client", "protocol", "version", "0.9.2"));
                        try {
                            SAXBuilder sax = new SAXBuilder();
                            Document doc = sax.build(url.openStream());
                            SwingUtilities.invokeLater(new ReportThread(doc));
                            XMLOutputter xout = new XMLOutputter();
                            xout.setNewlines(true);
                            xout.setIndent("	");
                            xout.setTextNormalize(true);
                            System.out.println(xout.outputString(doc));
                            logger.debug(xout.outputString(doc));
                        } catch (Exception ex) {
                            ExceptionHandler.handleException(ex);
                        }
                    } catch (Exception ex) {
                    }
                }
