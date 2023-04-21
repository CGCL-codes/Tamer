    void doImportUrl(Component c) {
        String ansr = null;
        URL url = null;
        boolean okay = false;
        while (okay == false) {
            String s;
            if (ansr == null) {
                s = JOptionPane.showInputDialog(c, "Enter the URL of a bookmarks file:", "");
            } else {
                s = JOptionPane.showInputDialog(c, "Whoops, Enter the URL of a bookmarks file:", ansr);
            }
            if (s == null) {
                return;
            } else {
                try {
                    ansr = s;
                    url = new URL(s);
                    okay = true;
                } catch (MalformedURLException ex) {
                }
            }
        }
        try {
            Document doc = AutoplotUtil.readDoc(url.openStream());
            List<Bookmark> importBook = Bookmark.parseBookmarks(doc.getDocumentElement());
            importList(importBook);
        } catch (SAXException ex) {
            Logger.getLogger(BookmarksManager.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(BookmarksManager.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(BookmarksManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
