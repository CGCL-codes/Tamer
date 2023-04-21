    private void readTables() {
        String[] s = ImageIO.getReaderMIMETypes();
        for (int i = 0, imax = s.length; i < imax; i++) adaptor_.put(s[i].toLowerCase(), "RawImage");
        s = ImageIO.getReaderFormatNames();
        for (int i = 0, imax = s.length; i < imax; i++) {
            String key = s[i].toLowerCase();
            if (adaptor_.get(key) == null) adaptor_.put(key, "RawImage");
        }
        if (adaptor_.get("jpeg2000") != null && adaptor_.get("jp2") == null) adaptor_.put("jp2", "RawImage");
        try {
            Map seen = new HashMap(13);
            for (Enumeration e = cl_.getResources("sys/" + FILENAME_PREFERENCES); e.hasMoreElements(); ) {
                URL url = (URL) e.nextElement();
                if (seen.get(url) != null) continue; else seen.put(url, url);
                readPreferences(url.openStream());
            }
        } catch (IOException ioe) {
            System.err.println("startup: " + ioe);
        }
        defadaptor_ = (CHashMap) adaptor_.clone();
        defberemap_ = (HashMap) beremap_.clone();
        defpref_ = (CHashMap) pref_.clone();
        String home = System.getProperty("user.home");
        String username = System.getProperty("user.name");
        if (!home.endsWith(username)) home += File.separatorChar + username;
        home += File.separatorChar + ".Multivalent";
        File homedir = new File(home), userpref = new File(homedir, FILENAME_PREFERENCES);
        if (standalone_) System.out.println("HOME = " + home);
        if (userpref.exists()) try {
            readPreferences(new FileInputStream(userpref));
        } catch (IOException ioe) {
            System.out.println("couldn't read user prefs: " + ioe);
        } else if (!homedir.exists()) homedir.mkdirs();
        String cache = getPreference(PREF_CACHEDIR, System.getProperty("java.io.tmpdir"));
        File cachedir = new File(cache);
        if (!cachedir.exists()) cachedir.mkdirs();
        cache_ = new Cache(home, cache, adaptor_);
    }
