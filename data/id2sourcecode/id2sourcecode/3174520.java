    private void handlePublicCommand(ArrayList<String> pm, String sender, String login, String hostname) {
        if (pm.get(0).equalsIgnoreCase(CMD_HELP)) {
            if (pm.size() > 1) {
                this.handleHelp(pm.get(1), sender, login, hostname);
            } else if (pm.size() == 1) {
                this.handleHelp(sender, login, hostname);
            }
        } else if (pm.size() >= 2 && (pm.get(0).equalsIgnoreCase(CMD_2V2) || pm.get(0).equalsIgnoreCase(CMD_3V3) || pm.get(0).equalsIgnoreCase(CMD_5V5))) {
            try {
                this.handleArenaRating(pm.get(0), Integer.parseInt(pm.get(1)), this.channel);
            } catch (NumberFormatException nfe) {
                log("Failed parsing 'rating' for function '" + pm.get(0) + "'");
            }
        } else if (pm.get(0).equalsIgnoreCase(CMD_TIME)) {
            log("Queried for the time.");
            this.sendNotice(this.channel, (new Date(System.currentTimeMillis())).toString());
        } else if (pm.get(0).equalsIgnoreCase(CMD_ROLL)) {
            if (pm.size() == 1) {
                log("Queried normal roll.");
                this.sendNotice(this.channel, sender + " has rolled: " + this.random.nextInt(100));
            } else if (pm.size() == 2) {
                log("Queried for upper-bound roll.");
                try {
                    log("Trying to parse second argument as int");
                    int top = Integer.parseInt(pm.get(1));
                    if (top >= 0) {
                        this.sendNotice(this.channel, sender + " has rolled (0-" + top + "): " + this.random.nextInt(top));
                    }
                } catch (NumberFormatException nfe) {
                    log("Couldn't parse second arg as int, defaulting to normal roll.");
                    this.sendNotice(this.channel, sender + " has rolled: " + this.random.nextInt(100));
                }
            } else if (pm.size() == 3) {
                log("Queried for special roll.");
                try {
                    int bottom = Integer.parseInt(pm.get(1));
                    int top = Integer.parseInt(pm.get(2));
                    if (top >= bottom) {
                        this.sendNotice(this.channel, sender + " has rolled (" + bottom + "-" + top + "): " + (this.random.nextInt(top - bottom) + bottom));
                    }
                } catch (NumberFormatException nfe) {
                    log("Couldn't parse second (or third possibly) arg as int, defaulting to normal roll.");
                    this.sendNotice(this.channel, sender + " has rolled: " + this.random.nextInt(100));
                }
            }
        } else if (pm.get(0).equalsIgnoreCase(CMD_FLIP)) {
            log("Queried for a flip.");
            this.sendNotice(this.channel, sender + " has requested a flip: " + (this.random.nextBoolean() ? "HEADS" : "TAILS"));
        } else if (pm.get(0).equalsIgnoreCase(CMD_SIXES)) {
            if (this.rollers.isEmpty()) {
                log("Starting new roll");
                this.rollers.add(sender);
                this.sendNotice(this.channel, sender + " has entered a game of sixes.");
            }
            if (!this.rollers.contains(sender)) {
                this.rollers.add(sender);
                this.sendNotice(this.channel, sender + " has entered a game of sixes.");
            }
            log("Queried for a sixes roll.");
        } else if (pm.get(0).equalsIgnoreCase(CMD_SHA1)) {
            if (pm.size() >= 2) {
                log("Returning a sha1 hash of: " + pm.get(1));
                try {
                    MessageDigest md = MessageDigest.getInstance("SHA1");
                    md.reset();
                    byte[] digest;
                    digest = md.digest(pm.get(1).getBytes());
                    StringBuffer hexString = new StringBuffer();
                    for (int i = 0; i < digest.length; i++) {
                        hexString.append(hexDigit(digest[i]));
                    }
                    this.sendNotice(this.channel, hexString.toString());
                } catch (NoSuchAlgorithmException e) {
                    throw new IllegalStateException(e.getMessage());
                }
            }
        } else if (pm.get(0).equalsIgnoreCase(CMD_MD5)) {
            if (pm.size() >= 2) {
                log("Returning a md5 hash of: " + pm.get(1));
                try {
                    MessageDigest md = MessageDigest.getInstance("MD5");
                    md.reset();
                    byte[] digest;
                    digest = md.digest(pm.get(1).getBytes());
                    StringBuffer hexString = new StringBuffer();
                    for (int i = 0; i < digest.length; i++) {
                        hexString.append(hexDigit(digest[i]));
                    }
                    this.sendNotice(this.channel, hexString.toString());
                } catch (NoSuchAlgorithmException e) {
                    throw new IllegalStateException(e.getMessage());
                }
            }
        } else if (pm.get(0).equalsIgnoreCase(CMD_FULL_SEARCH)) {
            if (pm.size() > 2) {
                log("Requesting all urls for multiple tags.");
                try {
                    pm.remove(0);
                    ArrayList<String> urls = this.taghandler.getUrlsForTags(pm);
                    if (urls.size() > 0) {
                        String tags = "";
                        for (String s : pm) {
                            if (tags.equalsIgnoreCase("")) {
                                tags += s;
                            } else tags += ", " + s;
                        }
                        this.sendNotice(sender, "Here are the results for tags: " + tags);
                        for (String s : urls) {
                            this.sendNotice(sender, s);
                        }
                    } else {
                        this.sendNotice(sender, "There were no urls for that tag.");
                    }
                } catch (SQLException sqle) {
                    log("Error searching for urls with multiple tags.");
                    sqle.printStackTrace();
                }
            } else if (pm.size() == 2) {
                log("Requesting all urls for tag: " + pm.get(1).replaceAll("[^A-Za-z0-9]", ""));
                try {
                    ArrayList<String> passingTags = new ArrayList<String>();
                    passingTags.add(pm.get(1).replaceAll("[^A-Za-z0-9]", ""));
                    ArrayList<String> urls = this.taghandler.getUrlsForTags(passingTags);
                    if (urls.size() > 0) {
                        this.sendNotice(sender, "Here are the results for tag: " + pm.get(1).replaceAll("[^A-Za-z0-9]", ""));
                        for (String s : urls) {
                            this.sendNotice(sender, s);
                        }
                    } else {
                        this.sendNotice(sender, "There were no urls for that tag.");
                    }
                } catch (SQLException sqle) {
                }
            }
        } else if (pm.get(0).equalsIgnoreCase(CMD_SEARCH)) {
            if (pm.size() > 2) {
                log("Requesting all urls for multiple tags.");
                try {
                    pm.remove(0);
                    ArrayList<String> urls = this.taghandler.getUrlsForTags(pm, this.numberOfResults);
                    if (urls.size() > 0) {
                        String tags = "";
                        for (String s : pm) {
                            if (tags.equalsIgnoreCase("")) {
                                tags += s;
                            } else tags += ", " + s;
                        }
                        this.sendNotice(sender, "Here are the results for tags: " + tags);
                        for (String s : urls) {
                            this.sendNotice(sender, s);
                        }
                    } else {
                        this.sendNotice(sender, "There were no urls for that tag.");
                    }
                } catch (SQLException sqle) {
                    log("Error searching for urls with multiple tags.");
                    sqle.printStackTrace();
                }
            } else if (pm.size() == 2) {
                log("Requesting all urls for tag: " + pm.get(1).replaceAll("[^A-Za-z0-9]", ""));
                try {
                    ArrayList<String> passingTags = new ArrayList<String>();
                    passingTags.add(pm.get(1).replaceAll("[^A-Za-z0-9]", ""));
                    ArrayList<String> urls = this.taghandler.getUrlsForTags(passingTags, this.numberOfResults);
                    if (urls.size() > 0) {
                        this.sendNotice(sender, "Here are the results for tag: " + pm.get(1).replaceAll("[^A-Za-z0-9]", ""));
                        for (String s : urls) {
                            this.sendNotice(sender, s);
                        }
                    } else {
                        this.sendNotice(sender, "There were no urls for that tag.");
                    }
                } catch (SQLException sqle) {
                }
            }
        } else if (pm.get(0).equalsIgnoreCase(CMD_TAGS_FOR)) {
            if (pm.size() >= 2) {
                log("Requesting all tags for url: " + pm.get(1));
                try {
                    ArrayList<String> tags = this.taghandler.getTagsForUrl(pm.get(1));
                    if (tags.size() > 0) {
                        this.sendNotice(sender, "Here are the tags for url: " + pm.get(1));
                        for (String s : tags) {
                            this.sendNotice(sender, s);
                        }
                    } else {
                        this.sendNotice(sender, "There were no tags for that url.");
                    }
                } catch (SQLException sqle) {
                    this.sendNotice(sender, "There were no tags for that url.");
                }
            }
        } else if (pm.get(0).equalsIgnoreCase(CMD_COUNT)) {
            if (pm.size() > 2) {
                log("Counting all urls for multiple tags.");
                try {
                    pm.remove(0);
                    int count = this.taghandler.getCountForTags(pm);
                    String tags = "";
                    for (int i = 0; i < pm.size(); i++) {
                        if (i == pm.size() - 1) {
                            tags += pm.get(i).replaceAll("[^A-Za-z0-9]", "");
                        } else {
                            tags += pm.get(i).replaceAll("[^A-Za-z0-9]", "") + ", ";
                        }
                    }
                    this.sendNotice(sender, "There are " + count + " urls for tags: " + tags);
                } catch (SQLException sqle) {
                    log("An error occurred while counting urls for tag: " + pm.get(1).replaceAll("[^A-Za-z0-9]", ""));
                    log(sqle.getLocalizedMessage());
                }
            } else if (pm.size() == 2) {
                log("Counting all urls for tag: " + pm.get(1).replaceAll("[^A-Za-z0-9]", ""));
                try {
                    int count = this.taghandler.getCountForTag(pm.get(1).replaceAll("[^A-Za-z0-9]", ""));
                    this.sendNotice(sender, "There are " + count + " urls for tag: " + pm.get(1).replaceAll("[^A-Za-z0-9]", ""));
                } catch (SQLException sqle) {
                    log("An error occurred while counting urls for tag: " + pm.get(1).replaceAll("[^A-Za-z0-9]", ""));
                    log(sqle.getLocalizedMessage());
                }
            }
        } else if (pm.get(0).equalsIgnoreCase(CMD_VERSION_NOTES)) {
            log("Dispatching the version notes.");
            for (String note : this.versionNotes.get(this.version)) {
                this.sendNotice(sender, note);
            }
        } else if (pm.get(0).equalsIgnoreCase(CMD_VERSION)) {
            log("Dispatching the version number.");
            this.sendNotice(sender, this.version);
        } else if (pm.get(0).equalsIgnoreCase(CMD_RANDOM) && pm.size() == 2) {
            try {
                this.log(sender + "!" + login + "@" + hostname + " issued random for: " + pm.get(1));
                this.findRandom(this.channel, sender, login, hostname, pm.get(1));
            } catch (NoSuchURLException nste) {
                this.sendNotice(this.channel, "Sorry, nothing found with tag: " + pm.get(1));
            }
        } else if (pm.get(0).equalsIgnoreCase(CMD_RANDOM) && pm.size() == 1) {
            boolean foundOne = false;
            while (!foundOne) {
                try {
                    this.log(sender + "!" + login + "@" + hostname + " issued random with no tag.");
                    String tag = this.taghandler.getRandomTag();
                    this.findRandom(this.channel, sender, login, hostname, tag);
                    foundOne = true;
                } catch (NoSuchURLException nste) {
                    this.log(nste.getMessage());
                } catch (SQLException sqle) {
                }
            }
        } else if (pm.get(0).equalsIgnoreCase(CMD_UPTIME)) {
            this.sendNotice(sender, "Started: " + this.startDate.get(Calendar.YEAR) + "-" + (this.startDate.get(Calendar.MONTH) + 1) + "-" + this.startDate.get(Calendar.DATE) + " " + this.startDate.get(Calendar.HOUR) + ":" + this.startDate.get(Calendar.MINUTE) + ":" + this.startDate.get(Calendar.SECOND) + " " + (this.startDate.get(Calendar.AM_PM) == 1 ? "PM" : "AM"));
        }
    }
