    public void processEvent(JSONObject evt) {
        if ("doSearch".equals(evt.get("method"))) {
            NamedSearch[] searches = theSession.getProperty(log4j.app.Log4jProperties.searches);
            for (NamedSearch search : searches) if (search.getName().equals(evt.get("search"))) {
                theSession.setProperty(log4j.app.Log4jProperties.search, search.getSearch());
                break;
            }
        } else if ("forgetSearch".equals(evt.get("method"))) {
            Search srch = theSession.getProperty(log4j.app.Log4jProperties.search);
            final NamedSearch[] searches = theSession.getProperty(log4j.app.Log4jProperties.searches);
            int s;
            for (s = 0; s < searches.length; s++) {
                if (searches[s].getSearch() == srch) {
                    if (searches[s].getName().equals(log4j.app.LoggerMonitor.ALL_SEARCH) || searches[s].getName().equals(log4j.app.LoggerMonitor.DEFAULT_SEARCH)) {
                        theSession.getUI().error("Search \"" + searches[s].getName() + "\" is not deletable");
                        return;
                    }
                    break;
                }
            }
            if (s == searches.length) {
                theSession.getUI().error("The current search is not saved");
                return;
            }
            final int fS = s;
            theSession.getUI().confirm("Are you sure you want to forget search \"" + searches[s].getName() + "\"?", new prisms.ui.UI.ConfirmListener() {

                public void confirmed(boolean confirm) {
                    if (!confirm) return;
                    getSession().setProperty(log4j.app.Log4jProperties.searches, prisms.util.ArrayUtils.remove(searches, fS));
                }
            });
        } else if ("rememberSearch".equals(evt.get("method"))) {
            final Search srch = theSession.getProperty(log4j.app.Log4jProperties.search);
            if (srch == log4j.app.Log4jProperties.NO_SEARCH) {
                theSession.getUI().error("No search has been made");
                return;
            }
            theSession.getUI().input("What name would you like to remember the new search as?", null, new prisms.ui.UI.InputListener() {

                public void inputed(final String input) {
                    if (input == null) return;
                    boolean found = false;
                    NamedSearch[] searches = getSession().getProperty(log4j.app.Log4jProperties.searches);
                    for (NamedSearch search : searches) if (search.getName().equals(input)) {
                        found = true;
                        break;
                    }
                    if (found) {
                        getSession().getUI().confirm("A search named \"" + input + "\" already exists. Are you sure you want to overwrite it?", new prisms.ui.UI.ConfirmListener() {

                            public void confirmed(boolean confirm) {
                                if (!confirm) return;
                                NamedSearch[] newSearches = getSession().getProperty(log4j.app.Log4jProperties.searches);
                                boolean found2 = false;
                                for (int s = 0; s < newSearches.length; s++) if (newSearches[s].getName().equals(input)) {
                                    newSearches[s] = new NamedSearch(input, srch);
                                    found2 = true;
                                    break;
                                }
                                if (!found2) newSearches = prisms.util.ArrayUtils.add(newSearches, new NamedSearch(input, srch));
                                getSession().setProperty(log4j.app.Log4jProperties.searches, newSearches);
                            }
                        });
                    } else {
                        searches = prisms.util.ArrayUtils.add(searches, new NamedSearch(input, srch));
                        getSession().setProperty(log4j.app.Log4jProperties.searches, searches);
                    }
                }
            });
        } else throw new IllegalArgumentException("Unrecognized " + theName + " event: " + evt);
    }
