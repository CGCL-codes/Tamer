    public void doCheckForUpdates() {
        try {
            URL url = new URL("http://spaz.ca/cronometer/updates.xml");
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document d = db.parse(url.openStream());
            Element e = d.getDocumentElement();
            int b = XMLNode.getInt(e, "build");
            if (b > BUILD) {
                final String www = e.getAttribute("url");
                SwingUtilities.invokeLater(new Runnable() {

                    public void run() {
                        int result = JOptionPane.showOptionDialog(CRONOMETER.getInstance(), "A new version of " + TITLE + " is available!", "New Version", JOptionPane.OK_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE, null, null, null);
                        if (result == JOptionPane.OK_OPTION && www != null) {
                            ToolBox.launchURL(CRONOMETER.mainFrame, www);
                        }
                    }
                });
            }
        } catch (Exception ex) {
            Logger.error(ex);
        }
    }
