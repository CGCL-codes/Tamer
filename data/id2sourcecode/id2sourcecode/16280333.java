    private void resetToDefaultMenuItemActionPerformed(java.awt.event.ActionEvent evt) {
        String surl = AutoplotUtil.getProperty("autoplot.default.bookmarks", "http://www.autoplot.org/data/demos.xml");
        int r = JOptionPane.showConfirmDialog(this, "Reset your bookmarks to " + surl + "?", "Reset Bookmarks", JOptionPane.OK_CANCEL_OPTION);
        if (r == JOptionPane.OK_OPTION) {
            try {
                URL url = new URL(surl);
                Document doc = AutoplotUtil.readDoc(url.openStream());
                List<Bookmark> book = Bookmark.parseBookmarks(doc.getDocumentElement());
                model.setList(book);
                formatToFile(bookmarksFile);
            } catch (SAXException ex) {
                logger.log(Level.SEVERE, null, ex);
            } catch (FileNotFoundException ex) {
                new GuiExceptionHandler().handle(ex);
            } catch (IOException ex) {
                logger.log(Level.SEVERE, null, ex);
            } catch (ParserConfigurationException ex) {
                logger.log(Level.SEVERE, null, ex);
            }
        }
    }
