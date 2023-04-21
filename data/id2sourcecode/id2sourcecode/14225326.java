    public void fetchServerList() {
        try {
            serverList.clearSelection();
            serverModel.clear();
            class ReportThread implements Runnable {

                Document doc;

                public ReportThread(Document d) {
                    doc = d;
                }

                public void run() {
                    Iterator iter = doc.getRootElement().getChildren("server").iterator();
                    while (iter.hasNext()) {
                        Element e = (Element) iter.next();
                        server_info info = new server_info(e.getAttributeValue("name"), e.getAttributeValue("address"), e.getAttributeValue("failed_count"), e.getAttributeValue("port"));
                        info.setPlayerCount(e.getAttributeValue("num_users"));
                        serverModel.addServer(info);
                    }
                }
            }
            class QueryThread implements Runnable {

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
            }
            Thread t = new Thread(new QueryThread());
            t.start();
        } catch (Exception ex) {
            ExceptionHandler.handleException(ex);
        }
    }
