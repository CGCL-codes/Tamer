    private void resetToDefaultMenuItemActionPerformed(java.awt.event.ActionEvent evt) {
        String surl = System.getProperty("autoplot.default.bookmarks", "http://www.autoplot.org/data/demos.xml");
        int r = JOptionPane.showConfirmDialog(this, "Reset your bookmarks to " + surl + "?");
        if (r == JOptionPane.OK_OPTION) {
            try {
                URL url = new URL(surl);
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
    }
