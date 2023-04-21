        @Override
        public void run() {
            try {
                finish_ = false;
                setFileMonitorThreadState(FileMonitorState.WAITING);
                pathToMonitor_ = toMonitor_.getAbsolutePath();
                super.setName("FileMonitor_for_#" + pathToMonitor_ + "#");
                while (!toMonitor_.canRead() && !Settings.isAppExit() && !finish_) {
                    FileMonitorImpl.log_.warn(FileMonitorImpl.messages_.getString("fileMonitor.fileMonitorThread.log.warn.invalidPathToMonitor") + toMonitor_.getPath());
                    if (Settings.isSingleRun()) {
                        FileMonitorImpl.log_.warn(FileMonitorImpl.messages_.getString("fileMonitor.fileMonitorThread.log.warn.noRetry") + toMonitor_.getPath());
                        finish();
                    } else {
                        try {
                            FileMonitorImpl.log_.info(FileMonitorImpl.messages_.getString("fileMonitor.fileMonitorThread.log.info.thread.retrying") + retryInterval_);
                            setFileMonitorThreadState(FileMonitorState.WAITING);
                            sleep(retryInterval_);
                            setFileMonitorThreadState(FileMonitorState.RUNNING);
                        } catch (InterruptedException e) {
                            FileMonitorImpl.log_.error(getName() + FileMonitorImpl.messages_.getString("fileMonitor.fileMonitorThread.log.error.thread.interrupted"));
                            setFileMonitorThreadState(FileMonitorState.STOPPED);
                        }
                    }
                }
                if (!Settings.isAppExit() && !finish_) {
                    setFileMonitorThreadState(FileMonitorState.RUNNING);
                    FileMonitorImpl.log_.info(this.getName() + FileMonitorImpl.messages_.getString("fileMonitor.fileMonitorThread.log.info.thread.started"));
                    boolean excludeSubFolders = false;
                    boolean includeHidden = false;
                    short toInclude = -1;
                    int maxItems = 0;
                    int listSizeAfterLastRun = 0;
                    int numberOfNodesInDirectoryTree = 0;
                    long newestInList = 0;
                    long tempOldestInList = 0;
                    long tempNewestInList = 0;
                    long updateInterval = 0;
                    String feedTitle = null;
                    String feedDescription = null;
                    String feedLink = null;
                    String feedType = null;
                    String feedPath = null;
                    String[] arrayOfCategories = null;
                    FileFilter fileFilter = null;
                    List<File> treeList = null;
                    SyndFeed feed = null;
                    threadMessageArguments_ = new Object[2];
                    threadMessageArguments_[0] = this.getName();
                    threadSleepingMessageFormatter_ = new MessageFormat("");
                    threadSleepingMessageFormatter_.setLocale(Locale.getDefault());
                    threadSleepingMessageFormatter_.applyPattern(FileMonitorImpl.messages_.getString("fileMonitor.fileMonitorThread.log.debug.thread.sleepingFor"));
                    MessageFormat threadNextRunMessageFormatter = new MessageFormat("");
                    threadNextRunMessageFormatter.setLocale(Locale.getDefault());
                    threadNextRunMessageFormatter.applyPattern(FileMonitorImpl.messages_.getString("fileMonitor.fileMonitorThread.log.info.thread.nextRun"));
                    feedType = feedProperties_.getProperty("feed.settings.feedType");
                    feedTitle = feedProperties_.getProperty("feed.title");
                    feedDescription = feedProperties_.getProperty("feed.description");
                    feedLink = feedProperties_.getProperty("feed.link");
                    feedPath = feedProperties_.getProperty("feed.settings.pathToXML");
                    toInclude = Utils.whatToInclude(feedProperties_.getProperty("feed.settings.include"));
                    excludeSubFolders = Boolean.parseBoolean(feedProperties_.getProperty("feed.settings.excludeSubFolders"));
                    includeHidden = Boolean.parseBoolean(feedProperties_.getProperty("feed.settings.includeHidden"));
                    FileMonitorImpl.log_.debug(FileMonitorImpl.messages_.getString("fileMonitor.fileMonitorThread.log.debug.splittingCategories"));
                    arrayOfCategories = (feedProperties_.getProperty("feed.tags")).split("\\s*,\\s*");
                    maxItems = Integer.parseInt(feedProperties_.getProperty("feed.settings.maxItems"));
                    updateInterval = Long.parseLong(feedProperties_.getProperty("feed.settings.updateInterval"));
                    fileFilter = ConfigurableEndsWithFileFilter.getInstance(feedProperties_.getProperty("feed.settings.fileTypes"));
                    FileMonitorImpl.log_.debug(FileMonitorImpl.messages_.getString("fileMonitor.fileMonitorThread.log.debug.property.feedType") + feedType);
                    FileMonitorImpl.log_.debug(FileMonitorImpl.messages_.getString("fileMonitor.fileMonitorThread.log.debug.property.feedTitle") + feedTitle);
                    FileMonitorImpl.log_.debug(FileMonitorImpl.messages_.getString("fileMonitor.fileMonitorThread.log.debug.property.feedDescription") + feedDescription);
                    FileMonitorImpl.log_.debug(FileMonitorImpl.messages_.getString("fileMonitor.fileMonitorThread.log.debug.property.feedLink") + feedLink);
                    FileMonitorImpl.log_.debug(FileMonitorImpl.messages_.getString("fileMonitor.fileMonitorThread.log.debug.property.feedPath") + feedPath);
                    FileMonitorImpl.log_.debug(FileMonitorImpl.messages_.getString("fileMonitor.fileMonitorThread.log.debug.property.toInclude") + ((toInclude == Utils.FILES_ONLY) ? "FILES_ONLY" : toInclude == Utils.FOLDERS_ONLY ? "FOLDERS_ONLY" : "FILES_AND_FOLDERS"));
                    FileMonitorImpl.log_.debug(FileMonitorImpl.messages_.getString("fileMonitor.fileMonitorThread.log.debug.property.excludeSubFolders") + excludeSubFolders);
                    FileMonitorImpl.log_.debug(FileMonitorImpl.messages_.getString("fileMonitor.fileMonitorThread.log.debug.property.includeHidden") + includeHidden);
                    FileMonitorImpl.log_.debug(FileMonitorImpl.messages_.getString("fileMonitor.fileMonitorThread.log.debug.property.maxItems") + maxItems);
                    FileMonitorImpl.log_.debug(FileMonitorImpl.messages_.getString("fileMonitor.fileMonitorThread.log.debug.property.updateInterval") + updateInterval);
                    FileMonitorImpl.log_.debug(FileMonitorImpl.messages_.getString("fileMonitor.fileMonitorThread.log.debug.property.fileTypes") + feedProperties_.getProperty("feed.settings.fileTypes"));
                    feed = new SyndFeedImpl();
                    feed.setFeedType(feedType);
                    feed.setTitle(feedTitle);
                    feed.setDescription(feedDescription);
                    feed.setLink(feedLink);
                    feedType = null;
                    feedDescription = null;
                    feedLink = null;
                    treeList = new ArrayList<File>();
                    while (!(Settings.isAppExit() | finish_)) {
                        numberOfNodesInDirectoryTree = getTreeList(toMonitor_, treeList, fileFilter, toInclude, excludeSubFolders, includeHidden);
                        if (numberOfNodesInDirectoryTree > 0) {
                            if (treeList.size() < maxItems && numberOfNodesInDirectoryTree >= maxItems) {
                                FileMonitorImpl.log_.info("fileMonitor.fileMonitorThread.log.info.forceCheck");
                                forceCheck();
                                numberOfNodesInDirectoryTree = getTreeList(toMonitor_, treeList, fileFilter, toInclude, excludeSubFolders, includeHidden);
                            }
                            Collections.sort(treeList, new FileDateComparator());
                            Commons.trim(treeList, maxItems);
                            tempOldestInList = Commons.getLatestTimestamp((File) treeList.get(treeList.size() - 1));
                            tempNewestInList = Commons.getLatestTimestamp((File) treeList.get(0));
                            if ((tempOldestInList - oldestInList_) > 1 || (tempNewestInList - newestInList) > 0 || (listSizeAfterLastRun - treeList.size()) > 0) {
                                FileMonitorImpl.log_.info(FileMonitorImpl.messages_.getString("fileMonitor.fileMonitorThread.log.info.writePending"));
                                feed.setEntries(createEntries(treeList, arrayOfCategories));
                                try {
                                    CommonsIO.writeFeed(feedPath, feed);
                                    FileMonitorImpl.log_.info(feedPath + FileMonitorImpl.messages_.getString("fileMonitor.fileMonitorThread.log.info.feedXMLWritten"));
                                    feed.setEntries(null);
                                } catch (IOException e) {
                                    FileMonitorImpl.log_.error(FileMonitorImpl.messages_.getString("fileMonitor.log.error.ioExceptionWhileWritingToFile") + e);
                                    e.printStackTrace();
                                } catch (FeedException e) {
                                    FileMonitorImpl.log_.error(FileMonitorImpl.messages_.getString("fileMonitor.log.error.feedExceptionWhileWritingToFile") + e);
                                    e.printStackTrace();
                                }
                            } else {
                                FileMonitorImpl.log_.debug(FileMonitorImpl.messages_.getString("fileMonitor.fileMonitorThread.log.debug.noNewItems"));
                            }
                            oldestInList_ = tempOldestInList - 1;
                            newestInList = tempNewestInList;
                        }
                        listSizeAfterLastRun = treeList.size();
                        treeList.clear();
                        System.gc();
                        if (Settings.isSingleRun()) {
                            break;
                        }
                        try {
                            threadMessageArguments_[1] = updateInterval;
                            FileMonitorImpl.log_.debug(threadSleepingMessageFormatter_.format(threadMessageArguments_));
                            threadMessageArguments_[1] = (new Date()).getTime() + updateInterval;
                            FileMonitorImpl.log_.info(threadNextRunMessageFormatter.format(threadMessageArguments_));
                            setFileMonitorThreadState(FileMonitorState.SLEEPING);
                            sleep(updateInterval);
                            FileMonitorImpl.log_.debug(this.getName() + FileMonitorImpl.messages_.getString("fileMonitor.fileMonitorThread.log.debug.thread.wokenUp"));
                        } catch (InterruptedException e) {
                            FileMonitorImpl.log_.error(this.getName() + FileMonitorImpl.messages_.getString("fileMonitor.fileMonitorThread.log.error.thread.interrupted"));
                            e.printStackTrace();
                        } finally {
                            setFileMonitorThreadState(FileMonitorState.RUNNING);
                        }
                    }
                    FileMonitorImpl.log_.info(this.getName() + FileMonitorImpl.messages_.getString("fileMonitor.fileMonitorThread.log.info.thread.exiting"));
                    setFileMonitorThreadState(FileMonitorState.STOPPED);
                } else {
                    setFileMonitorThreadState(FileMonitorState.STOPPED);
                }
            } catch (Exception e) {
                setFileMonitorThreadState(FileMonitorState.ERROR);
            }
        }
