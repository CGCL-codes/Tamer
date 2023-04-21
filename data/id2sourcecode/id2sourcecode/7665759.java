    public void download(String url) {
        this.currentLabel.setText("Processing feed: " + url);
        URL feedUrl = null;
        String localFile;
        this.currentLabel.setForeground(new Color(34, 139, 34));
        this.currentLabel.setText("Downloading RSS Feed XML");
        this.currentProgress.setForeground(new Color(34, 139, 34));
        try {
            feedUrl = new URL(url);
            localFile = this.downloadFile(feedUrl, Settings.downloadDir + "\\tmp\\" + this.currentFeed.group, this.currentFeed.name + ".xml");
        } catch (final Exception se) {
            String msg = new String("ERROR downloading podcast: " + this.currentFeed.group + "/" + this.currentFeed.name + " " + se.getMessage());
            this.log(url + ":" + msg);
            this.feedManager.writeToHistory(feedUrl.toString() + " error", 0, this.download);
            this.currentLabel.setForeground(Color.red);
            this.currentLabel.setText(msg);
            this.currentProgress.setValue(100);
            this.currentProgress.setForeground(Color.red);
            this.currentProgress.setString(this.feedManager.downloadFailure(feedUrl.toString(), "error"));
            return;
        }
        String[] downloadLocs = new String[0];
        String lastError = new String("last error:");
        for (FeedReader feedRead : this.feedReader) {
            try {
                this.currentLabel.setForeground(new Color(34, 139, 34));
                this.currentLabel.setText("Parsing RSS Feed XML and retrieving podcast locations using" + feedRead.getClass().getName() + " rss library for" + feedUrl.toString());
                this.currentProgress.setForeground(new Color(34, 139, 34));
                downloadLocs = feedRead.extractPodCasts(localFile, this.currentFeed.number);
                if (downloadLocs.length == 0) {
                    this.feedManager.writeToHistory(feedUrl.toString() + " no_podcast", 0, this.download);
                    this.currentLabel.setForeground(Color.blue);
                    this.currentLabel.setText("No podcasts for: " + this.currentFeed.group + "/" + this.currentFeed.name);
                    this.currentProgress.setValue(100);
                    this.currentProgress.setForeground(Color.blue);
                    this.currentProgress.setString(this.feedManager.downloadFailure(feedUrl.toString(), "no_podcast"));
                    return;
                }
                if (downloadLocs.length > 0) {
                    break;
                }
            } catch (final Exception se1) {
                String msg = new String("ERROR with feedreader " + feedRead.getClass().getName() + " " + this.currentFeed.group + "/" + this.currentFeed.name + " : " + se1.getMessage());
                this.log(msg + "\n\n");
                this.currentLabel.setForeground(Color.red);
                this.currentLabel.setText(msg);
                this.currentProgress.setForeground(Color.red);
                lastError = se1.toString();
            }
        }
        if (downloadLocs.length == 0) {
            if (lastError.compareTo("last error:") == 0) {
                this.feedManager.writeToHistory(feedUrl.toString() + " no_podcast", 0, this.download);
                this.currentLabel.setForeground(Color.blue);
                this.currentLabel.setText("No podcasts for: " + this.currentFeed.group + "/" + this.currentFeed.name);
                this.currentProgress.setValue(100);
                this.currentProgress.setForeground(Color.blue);
                this.currentProgress.setString(this.feedManager.downloadFailure(feedUrl.toString(), "no_podcast"));
            } else {
                String msg = new String("Problem with all feed readers " + this.currentFeed.group + "/" + this.currentFeed.name + " : " + lastError);
                this.log(msg + "\n\n");
                this.feedManager.writeToHistory(feedUrl.toString() + " error", 0, this.download);
                this.currentLabel.setForeground(Color.red);
                this.currentLabel.setText(msg);
                this.currentProgress.setValue(100);
                this.currentProgress.setForeground(Color.red);
                this.currentProgress.setString(this.feedManager.downloadFailure(feedUrl.toString(), "error"));
            }
            return;
        }
        this.currentLabel.setForeground(new Color(34, 139, 34));
        this.currentLabel.setText("Found podcasts, start downloading " + this.currentFeed.group + "/" + this.currentFeed.name);
        this.currentProgress.setForeground(new Color(34, 139, 34));
        int tries = 0;
        final int maxTries = 3;
        while (tries < maxTries) {
            try {
                double totalSize = 0;
                for (final String element : downloadLocs) {
                    Boolean downloaded = true;
                    if (element.endsWith("torrent") && Settings.torrentLink.compareTo("true") == 0) {
                        this.currentLabel.setText("Activating torrent link: " + this.currentFeed.group + "/" + this.currentFeed.name + " : " + element);
                        if (!this.feedManager.history.containsKey(feedUrl.toString() + " " + element)) {
                            if (!this.download) {
                                this.currentLabel.setForeground(new Color(0, 100, 0));
                                this.currentLabel.setText("Torrent link. Size could not be determined" + this.currentFeed.group + "\\" + this.currentFeed.name);
                            } else {
                                Runtime.getRuntime().exec(Settings.torrentExe + ' ' + element);
                                System.out.println("writing " + element + " to repo");
                                this.feedManager.writeToHistory(feedUrl.toString() + " " + element, 0, this.download);
                            }
                        } else {
                            this.feedManager.writeToHistory(feedUrl.toString() + " already_downloaded", 0, this.download);
                            this.currentLabel.setForeground(Color.blue);
                            this.currentLabel.setText("Already downloaded before: " + this.currentFeed.group + "/" + this.currentFeed.name + "/" + element);
                            this.currentProgress.setValue(100);
                            this.currentProgress.setForeground(Color.blue);
                            this.currentProgress.setString(this.feedManager.downloadFailure(feedUrl.toString(), "already_downloaded"));
                            this.currentProgress.setValue(100);
                        }
                    } else {
                        final URL podCastUrl = new URL(element);
                        final File file = new File(podCastUrl.getFile());
                        if (!this.feedManager.history.containsKey(url + " " + file.getName())) {
                            System.out.println("writing " + file.getName() + " to repo when finished");
                            downloaded = false;
                            HttpURLConnection urlConnection = null;
                            int size = -1;
                            try {
                                urlConnection = (HttpURLConnection) podCastUrl.openConnection();
                                size = urlConnection.getContentLength();
                                urlConnection.disconnect();
                            } catch (Exception se) {
                                try {
                                    urlConnection.disconnect();
                                } catch (Exception se1) {
                                    System.out.println("ignore exception");
                                }
                            }
                            final double sizeInMB = (double) size / (double) (1024 * 1024);
                            totalSize += sizeInMB;
                            if (!this.download) {
                                this.currentLabel.setText("Size: " + this.feedManager.round2Decimals(totalSize) + "(MB): " + this.currentFeed.group + "\\" + this.currentFeed.name);
                                this.feedManager.writeToHistory(feedUrl.toString() + " " + file.getName(), sizeInMB, this.download);
                            } else {
                                this.currentLabel.setForeground(new Color(34, 139, 34));
                                this.currentLabel.setText("Downloading " + this.feedManager.round2Decimals(sizeInMB) + "(MB): " + this.currentFeed.group + "\\" + this.currentFeed.name + "\\" + file.getName());
                                this.currentProgress.setForeground(new Color(34, 139, 34));
                                final String fileOutPath = Settings.downloadDir + "\\" + this.currentFeed.group + "\\" + this.currentFeed.name;
                                if (this.download) {
                                    downloadFile(new URL(element), fileOutPath, null);
                                }
                                this.feedManager.writeToHistory(url + " " + file.getName(), sizeInMB, this.download);
                                this.currentLabel.setForeground(new Color(184, 134, 11));
                                this.currentLabel.setText("Finished " + this.feedManager.round2Decimals(totalSize) + "(MB): " + this.currentFeed.group + "\\" + this.currentFeed.name + "\\" + file.getName());
                                this.currentProgress.setForeground(new Color(184, 134, 11));
                            }
                        } else {
                            if (downloaded) {
                                this.feedManager.writeToHistory(feedUrl.toString() + " already_downloaded", 0, this.download);
                                this.currentLabel.setForeground(Color.blue);
                                this.currentLabel.setText("Already downloaded before: " + this.currentFeed.group + "/" + this.currentFeed.name + "/" + file.getName());
                                this.currentProgress.setValue(100);
                                this.currentProgress.setForeground(Color.blue);
                                this.currentProgress.setString(this.feedManager.downloadFailure(feedUrl.toString(), "already_downloaded"));
                            }
                        }
                    }
                }
                return;
            } catch (final Exception se) {
                tries++;
                final long sleep = tries * 10;
                if (this.stopDownload) {
                    this.currentLabel.setText("Stopped. Deleted: " + this.currentFeed.group + "/" + this.currentFeed.name);
                    return;
                }
                if (tries < maxTries) {
                    this.currentLabel.setForeground(Color.red);
                    this.currentLabel.setText("ERROR downloading: " + this.currentFeed.group + "/" + this.currentFeed.name + " " + se.getMessage() + " " + tries + "/" + maxTries + " tries. Sleeping: " + sleep + " seconds");
                    try {
                        Thread.sleep(sleep * 1000);
                    } catch (final Exception e) {
                        System.out.println("Problem sleeping: " + e.getMessage());
                    }
                } else {
                    String msg = new String("ERROR downloading: " + this.currentFeed.group + "/" + this.currentFeed.name + se.getMessage() + " " + tries + "/" + maxTries + " tries");
                    this.log(msg + "\n\n");
                    this.feedManager.writeToHistory(feedUrl.toString() + " error", 0, this.download);
                    this.currentLabel.setForeground(Color.red);
                    this.currentLabel.setText(msg);
                    this.currentProgress.setValue(100);
                    this.currentProgress.setForeground(Color.red);
                    this.currentProgress.setString(this.feedManager.downloadFailure(feedUrl.toString(), "error"));
                    return;
                }
            }
        }
    }
