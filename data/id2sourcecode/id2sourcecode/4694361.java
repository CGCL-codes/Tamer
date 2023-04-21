        public void run() {
            Looper.prepare();
            RSSDB db = new RSSDB(c);
            ContentValues values = new ContentValues();
            while (mDownloads.size() > 0) {
                try {
                    try {
                        mCurrent = mDownloads.get(0);
                    } catch (IndexOutOfBoundsException e) {
                        throw new DownloadException("Error collecting next queued download.");
                    }
                    RSSFeed feed = db.getFeed(mCurrent.feedId);
                    if (feed.secure) {
                        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(DownloadService.this);
                        String feedId = String.format("feed_%d", feed.id);
                        String username = prefs.getString(String.format("%s_username", feedId.toString()), "");
                        String password = prefs.getString(String.format("%s_password", feedId.toString()), "");
                        mClient = new DownloadHttpClient(username, password);
                    } else {
                        mClient = new DefaultHttpClient();
                    }
                    HttpGet GET = new HttpGet(mCurrent.remotefile.toString());
                    BasicHttpResponse response = (BasicHttpResponse) mClient.execute(GET);
                    if (response.getStatusLine().getStatusCode() == 401) {
                        throw new DownloadException("Unathorised access - check your username and password.");
                    } else if (response.getStatusLine().getStatusCode() != 200) {
                        throw new DownloadException("Error connecting to server.");
                    }
                    HttpEntity entity = response.getEntity();
                    long fileSize = entity.getContentLength();
                    String[] fragments = mCurrent.remotefile.getFile().split("/");
                    String filename = fragments[fragments.length - 1];
                    String storageState = Environment.getExternalStorageState();
                    if (storageState.compareTo("mounted") != 0) {
                        throw new DownloadException("No external storage available: " + storageState);
                    }
                    String storageFS = Environment.getExternalStorageDirectory().toString();
                    StatFs storageStats = new StatFs(storageFS);
                    int blockSize = storageStats.getBlockSize();
                    int availableBlocks = storageStats.getAvailableBlocks();
                    int availableSize = blockSize * availableBlocks;
                    if (availableSize < fileSize) {
                        throw new DownloadException(String.format("File is %s but external storage only has %s available.", readableSize(fileSize), readableSize(availableSize)));
                    }
                    File path = new File(storageFS + storagePath);
                    path.mkdirs();
                    File f = new File(storageFS + storagePath + filename);
                    values.put("downloadstate", RSSItem.downloadstates.DOWNLOADING.ordinal());
                    db.updateArticle(mCurrent.id, values);
                    postMessage(START_DOWNLOAD, mCurrent, 0);
                    try {
                        assert (f.createNewFile() == true);
                        FileOutputStream out = new FileOutputStream(f);
                        InputStream is = entity.getContent();
                        BufferedInputStream bi = new BufferedInputStream(is);
                        byte[] b = new byte[10000];
                        int result, percent;
                        long index = 0;
                        long time = System.currentTimeMillis();
                        while (true) {
                            if (stopFlag) {
                                stopFlag = false;
                                throw new DownloadStoppedException();
                            }
                            result = bi.read(b);
                            if (result == -1) break;
                            index += result;
                            if (time < System.currentTimeMillis() + 3000) {
                                time = System.currentTimeMillis();
                                percent = (int) (index * 100 / fileSize);
                            }
                            out.write(b, 0, result);
                        }
                        entity.consumeContent();
                        bi.close();
                        is.close();
                    } catch (IOException e) {
                        throw new DownloadException("Failed to download file.");
                    }
                    values.put("downloadstate", RSSItem.downloadstates.DOWNLOADED.ordinal());
                    values.put("localfile", filename);
                    db.updateArticle(mCurrent.id, values);
                    postMessage(DOWNLOADED, mCurrent, 0);
                } catch (DownloadException e1) {
                    e1.printStackTrace();
                    mErrorString = e1.getMessage();
                    postMessage(DOWNLOAD_ERROR, mCurrent, 0);
                    values.put("downloadstate", RSSItem.downloadstates.FAILED.ordinal());
                    db.updateArticle(mCurrent.id, values);
                } catch (DownloadStoppedException e1) {
                    postMessage(DOWNLOAD_STOPPED, mCurrent, 0);
                    values.put("downloadstate", RSSItem.downloadstates.UNDOWNLOADED.ordinal());
                    db.updateArticle(mCurrent.id, values);
                } catch (ClientProtocolException e1) {
                    e1.printStackTrace();
                    mErrorString = e1.getMessage();
                    postMessage(DOWNLOAD_ERROR, mCurrent, 0);
                    db.updateArticle(mCurrent.id, values);
                } catch (IOException e1) {
                    e1.printStackTrace();
                    mErrorString = e1.getMessage();
                    postMessage(DOWNLOAD_ERROR, mCurrent, 0);
                    db.updateArticle(mCurrent.id, values);
                }
                mDownloads.remove(0);
            }
            mCurrent = null;
            nm.cancel(DOWNLOADING_NOTIFY_ID);
        }
