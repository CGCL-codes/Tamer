            public void run() {
                Debug.log("RSS Updater", "Starting run");
                logC.writeToLog(0, "Attempting RSS Read");
                if (rssVar) {
                    Debug.log("RSS Updater", "rss variables read succesfully from var.xml");
                    try {
                        Debug.log("RSS Updater", "reading local rss.xml file");
                        feed = rssC.getLocalFeed(rssXmlFile);
                    } catch (Exception ex) {
                        Debug.log("RSS Updater", "no local rss feed found");
                        logC.writeToLog(0, "No local feed found, trying online.");
                        feed = tempFeed;
                    }
                    try {
                        Debug.log("RSS Updater", "reading rss feed online");
                        newFeed = rssC.getFeed();
                    } catch (Exception e) {
                        Debug.log("RSS Updater", "online rss feed read failed");
                        logC.writeToLog(2, "Unable to read online feed.");
                    }
                    Debug.log("RSS Updater", "comparing old and new feed");
                    if (!feed.equals(newFeed)) {
                        if (!newFeed.isEmpty()) {
                            trayIcon.displayMessage("News!", "There is a new newspost", TrayIcon.MessageType.INFO);
                            try {
                                Debug.log("RSS Updater", "writing new feed to local xml file");
                                rssC.feedToLocalXML(newFeed, rssXmlFile);
                            } catch (Exception exx) {
                                Debug.log("RSS Updater", "local feed file write failed, keeping copy in memory");
                                tempFeed = newFeed;
                            }
                        }
                        Debug.log("RSS Updater", "updating gui news tab");
                        gui.setNewsTab();
                    } else {
                        if (feed.isEmpty()) {
                            gui.setNewsTab("Unable to load the feed");
                        } else {
                            Debug.log("RSS Updater", "updating gui news tab");
                            gui.setNewsTab();
                        }
                    }
                }
            }
