    public void run() {
        int counter = 0;
        this.supernodeEngine.start();
        while (!this.terminate) {
            if (this.resetCycle == true) {
                this.resetCycle = false;
                this.forceIndexing = true;
            }
            try {
                boolean changes = false;
                this.propertyChangeSupport.firePropertyChange("fileIndexingCompleted", null, null);
                Thread.sleep(this.forceIndexing ? 0 : BackgroundEngine.refreshRate);
                while (wa != null && wa.writingFileLock.intValue() > 0) {
                    Thread.sleep(BackgroundEngine.refreshRate);
                }
                this.propertyChangeSupport.firePropertyChange("fileIndexingInit", null, null);
                ArrayList fileList = new ArrayList();
                Hashtable tempHashtable = (Hashtable) sharedFilesIndexName.clone();
                boolean modifications = false;
                for (int x = 0; x < this.sharedDirectories.size(); x++) {
                    DirInfos sharedDir = (DirInfos) this.sharedDirectories.get(x);
                    if (!sharedDir.getIndexed() || sharedDir.getLastModified() != sharedDir.getFile().lastModified() || recursiveExploreForModifications(sharedDir.getFile())) {
                        modifications = true;
                    }
                }
                if (!modifications && !forceIndexing) {
                    continue;
                } else {
                    nestedDirectories = new Hashtable();
                    forceIndexing = false;
                }
                _logger.info("Shared directory structure modification! Analyzing..." + this.sharedDirectories.size());
                for (int x = 0; x < this.sharedDirectories.size(); x++) {
                    DirInfos sharedDir = (DirInfos) this.sharedDirectories.get(x);
                    File[] files = sharedDir.getFile().listFiles();
                    if (files != null) {
                        for (int y = 0; y < files.length; y++) {
                            if (files[y].isFile() && !fileList.contains(files[y].getAbsolutePath())) {
                                fileList.add(files[y].getAbsolutePath());
                                tempHashtable.remove(files[y].getAbsolutePath());
                            } else if (files[y].isDirectory() && BackgroundEngine.recursiveExplore) {
                                DirInfos infos = new DirInfos(files[y].getAbsolutePath(), files[y].lastModified());
                                this.nestedDirectories.put(files[y].getAbsolutePath(), infos);
                                this.recursiveExplore(files[y], fileList, tempHashtable);
                            }
                            try {
                                this.sleep(50);
                            } catch (InterruptedException ex) {
                            }
                        }
                    }
                    sharedDir.setLastModified(sharedDir.getFile().lastModified());
                    sharedDir.setIndexed();
                }
                _logger.info("Total files: " + fileList.size() + "   Removing: " + tempHashtable.size());
                Enumeration toBeRemoved = tempHashtable.keys();
                while (toBeRemoved.hasMoreElements()) {
                    changes = true;
                    String removeKey = (String) toBeRemoved.nextElement();
                    sharedFilesIndexHash.remove(((FileInfos) sharedFilesIndexName.get(removeKey)).getHash());
                    sharedFilesIndexName.remove(removeKey);
                    synchronized (this.localIndexMonitor) {
                        IndexReader reader = IndexReader.open(storeIndex);
                        Term term = new Term("Path", removeKey);
                        int deleted = reader.deleteDocuments(term);
                        reader.close();
                    }
                }
                _logger.info("Total shared files: " + sharedFilesIndexName.size() + "   Modifier: " + (sharedFilesIndexName.size() - fileList.size()));
                for (int y = fileList.size() - 1; y >= 0 && !this.terminate && !this.resetCycle && (wa == null || !(wa.writingFileLock.intValue() > 0)); y--) {
                    if (!sharedFilesIndexName.containsKey(fileList.get(y))) {
                        changes = true;
                        FileInfos infos = new FileInfos(new File((String) fileList.get(y)));
                        sharedFilesIndexName.put(fileList.get(y), infos);
                        sharedFilesIndexHash.put(infos.getHash(), infos);
                        synchronized (this.localIndexMonitor) {
                            IndexWriter writer = new IndexWriter(storeIndex, new StandardAnalyzer(), false);
                            IndexerGraphicEngine ige = new IndexerGraphicEngine(new File((String) fileList.get(y)));
                            ige.start();
                            writer.addDocument(FileDocument.Document(infos));
                            writer.close();
                            ige.terminate();
                        }
                        this.store(store);
                        this.propertyChangeSupport.firePropertyChange("fileIndexed", new Integer(fileList.size()), new Integer(y));
                    } else {
                        if (!(((FileInfos) sharedFilesIndexName.get(fileList.get(y))).getLastModified() == (new File((String) fileList.get(y))).lastModified())) {
                            changes = true;
                            FileInfos infos = new FileInfos(new File((String) fileList.get(y)));
                            sharedFilesIndexName.put(fileList.get(y), infos);
                            sharedFilesIndexHash.put(infos.getHash(), infos);
                            synchronized (this.localIndexMonitor) {
                                IndexWriter writer = new IndexWriter(storeIndex, new StandardAnalyzer(), false);
                                writer.addDocument(FileDocument.Document(infos));
                                writer.close();
                            }
                            this.store(store);
                            this.propertyChangeSupport.firePropertyChange("fileIndexed", new Integer(fileList.size()), new Integer(y));
                        }
                    }
                }
                if (changes) {
                    this.forceUploadingLists = true;
                    this.store(store);
                    if (Logger.getRootLogger().getEffectiveLevel().toInt() <= Level.DEBUG_INT) {
                        Enumeration visualizeKeys = sharedFilesIndexName.keys();
                        Enumeration visualizeValues = sharedFilesIndexName.elements();
                        while (visualizeKeys.hasMoreElements()) {
                            _logger.debug(visualizeKeys.nextElement() + ".....");
                            FileInfos fi = (FileInfos) visualizeValues.nextElement();
                            _logger.debug(fi.getHash() + "....." + fi.getLastModified() + "\n");
                        }
                    }
                    this.propertyChangeSupport.firePropertyChange("SharedDirectoriesModification", null, this);
                }
                totalLocalSharedFileSize = 0;
                Enumeration localFiles = this.sharedFilesIndexHash.elements();
                while (localFiles.hasMoreElements()) {
                    totalLocalSharedFileSize += ((FileInfos) localFiles.nextElement()).getSize();
                }
                totalRemoteSharedFileSize = 0;
                Enumeration remoteFiles = this.remoteFilesIndexHash.elements();
                while (remoteFiles.hasMoreElements()) {
                    totalRemoteSharedFileSize += ((RemoteFileInfos) remoteFiles.nextElement()).getSize();
                }
                if (this.getTimesToRemoteCrawling() > 0) {
                    if (this.wa != null && counter == 0) {
                        this.currentQuery = this.wa.doRandomQuery(broadcastTimeToLive);
                    }
                    counter = (counter + 1) % this.getTimesToRemoteCrawling();
                }
                System.gc();
            } catch (Exception e) {
                _logger.error("Background Indexer Error", e);
                this.propertyChangeSupport.firePropertyChange("fileIndexingCompleted", null, null);
            }
        }
        synchronized (this) {
            while (this.synchronizingRemote) {
                try {
                    this.wait();
                } catch (InterruptedException ex1) {
                }
            }
            this.loggingOut = true;
        }
    }
