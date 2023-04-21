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
