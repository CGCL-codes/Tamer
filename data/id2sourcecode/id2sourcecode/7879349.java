    void doImportUrl() {
        String ansr = null;
        URL url = null;
        boolean okay = false;
        while (okay == false) {
            String s;
            if (ansr == null) {
                s = JOptionPane.showInputDialog(this, "Enter the URL of a bookmarks file:", "");
            } else {
                s = JOptionPane.showInputDialog(this, "Whoops, Enter the URL of a bookmarks file:", ansr);
            }
            if (s == null) {
                return;
            } else {
                try {
                    url = new URL(s);
                    okay = true;
                } catch (MalformedURLException ex) {
                }
            }
        }
        try {
            Document doc = AutoplotUtil.readDoc(url.openStream());
            List<Bookmark> book = Bookmark.parseBookmarks(doc);
            this.setList(book);
        } catch (SAXException ex) {
            Logger.getLogger(BookmarksManager.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(BookmarksManager.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(BookmarksManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
