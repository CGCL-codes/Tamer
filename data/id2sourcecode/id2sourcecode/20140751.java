    public List<Bookmark> getRecent() {
        if (recent != null) return recent;
        String nodeName = "recent";
        File f2 = new File(AutoplotSettings.settings().resolveProperty(AutoplotSettings.PROP_AUTOPLOTDATA), "bookmarks/");
        if (!f2.exists()) {
            boolean ok = f2.mkdirs();
            if (!ok) {
                throw new RuntimeException("unable to create folder " + f2);
            }
        }
        final File f = new File(f2, nodeName + ".xml");
        if (f.exists()) {
            try {
                recent = Bookmark.parseBookmarks(AutoplotUtil.readDoc(new FileInputStream(f)).getDocumentElement(), 0);
            } catch (SAXException ex) {
                Logger.getLogger(ApplicationModel.class.getName()).log(Level.SEVERE, null, ex);
                return new ArrayList<Bookmark>();
            } catch (IOException ex) {
                Logger.getLogger(ApplicationModel.class.getName()).log(Level.SEVERE, null, ex);
                return new ArrayList<Bookmark>();
            } catch (ParserConfigurationException ex) {
                Logger.getLogger(ApplicationModel.class.getName()).log(Level.SEVERE, null, ex);
                return new ArrayList<Bookmark>();
            }
        } else {
            Preferences prefs = Preferences.userNodeForPackage(ApplicationModel.class);
            String srecent = prefs.get(PREF_RECENT, "");
            if (srecent.equals("") || !srecent.startsWith("<")) {
                String srecenturl = AutoplotUtil.getProperty("autoplot.default.recent", "");
                if (!srecenturl.equals("")) {
                    try {
                        URL url = new URL(srecenturl);
                        recent = Bookmark.parseBookmarks(AutoplotUtil.readDoc(url.openStream()).getDocumentElement());
                        prefs.put(PREF_RECENT, Bookmark.formatBooks(recent));
                        try {
                            prefs.flush();
                        } catch (BackingStoreException ex) {
                            ex.printStackTrace();
                        }
                    } catch (MalformedURLException e) {
                        return new ArrayList<Bookmark>();
                    } catch (IOException e) {
                        return new ArrayList<Bookmark>();
                    } catch (SAXException e) {
                        return new ArrayList<Bookmark>();
                    } catch (ParserConfigurationException e) {
                        return new ArrayList<Bookmark>();
                    }
                } else {
                    return new ArrayList<Bookmark>();
                }
            } else {
                try {
                    recent = Bookmark.parseBookmarks(AutoplotUtil.readDoc(new ByteArrayInputStream(srecent.getBytes())).getDocumentElement());
                } catch (SAXException e) {
                    return new ArrayList<Bookmark>();
                } catch (IOException e) {
                    return new ArrayList<Bookmark>();
                } catch (ParserConfigurationException e) {
                    return new ArrayList<Bookmark>();
                }
            }
            addRecent("");
        }
        return recent;
    }
