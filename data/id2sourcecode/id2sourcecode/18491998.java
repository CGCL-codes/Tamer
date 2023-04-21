            public void run() {
                Message msg = new Message();
                String msgText = "";
                for (int nfailed = 0; nfailed < 1; nfailed++) {
                    if (vConnecting) {
                        try {
                            boolean vnew = true;
                            int icount = 0;
                            try {
                                historys = Utils.droidDB.getHistory();
                                for (History history : historys) {
                                    icount++;
                                    if (XMLAddress.contains(history.url) || XMLAddress.replace("%20", " ").contains(history.urllocal)) {
                                        File cachedfile = new File(history.urllocal.replace("file://", ""));
                                        if (cachedfile.exists()) {
                                            XMLAddress = history.urllocal;
                                            vnew = false;
                                            handlerUsingCache.sendEmptyMessage(0);
                                        }
                                    }
                                }
                            } catch (Exception e2) {
                                final Exception ef = e2;
                            }
                            Log.d("EMD - ", "Parsing emx - " + XMLAddress);
                            InputStream content;
                            if (XMLAddress.contains("http")) {
                                HttpGet httpGet = new HttpGet(XMLAddress);
                                httpGet.setHeader("cookie", cookieString);
                                HttpClient httpclient = new DefaultHttpClient();
                                HttpResponse response = httpclient.execute(httpGet);
                                content = response.getEntity().getContent();
                            } else {
                                URL url = new URL(XMLAddress);
                                content = url.openStream();
                            }
                            SAXParserFactory spf = SAXParserFactory.newInstance();
                            SAXParser sp = spf.newSAXParser();
                            XMLReader xr = sp.getXMLReader();
                            XMLHandlerDownloads myXMLHandler = new XMLHandlerDownloads();
                            xr.setContentHandler(myXMLHandler);
                            xr.parse(new InputSource(content));
                            int newFiles = myXMLHandler.nTracks + 1;
                            String newArtURL = myXMLHandler.albumArt;
                            String newArtURLSml = myXMLHandler.albumArtSml;
                            String newGenre = myXMLHandler.genre;
                            int iBookFlag = 0;
                            if (newGenre.contains("Audiobooks")) {
                                iBookFlag = 1;
                            }
                            String newArtist = Utils.stringCleanUp(myXMLHandler.artist);
                            String newAlbum = Utils.stringCleanUp(myXMLHandler.album);
                            if (newFiles > 101) {
                                Message handlermsg = new Message();
                                String debugText = " " + newFiles + " ";
                                handlermsg.obj = debugText;
                                handler101Warn.sendMessage(handlermsg);
                                newFiles = 101;
                            }
                            String[] newTrackURLs = new String[newFiles];
                            String[] newTrackNums = new String[newFiles];
                            String[] newTrackNames = new String[newFiles];
                            String[] futureFileNames = new String[newFiles];
                            int[] newTrackStatus = new int[newFiles];
                            newTrackNames[0] = "AlbumArt.jpg";
                            newTrackURLs[0] = newArtURL;
                            newTrackStatus[0] = 10;
                            newTrackNums[0] = "0";
                            long ifs = 0;
                            String emxFutureFile = "";
                            if (vnew && vStorage) {
                                if (newFiles == 2) {
                                    emxFutureFile = newAlbum + " - " + myXMLHandler.downloadNames[0];
                                    emxFutureFile = Utils.stringCleanUp(emxFutureFile);
                                    emxFutureFile = storageRoot + "/" + emxFutureFile + ".emx";
                                } else {
                                    emxFutureFile = storageRoot + "/" + newAlbum + ".emx";
                                }
                            } else {
                                emxFutureFile = storageRoot + "/tmp";
                            }
                            BufferedWriter emxCacheFile = new BufferedWriter(new FileWriter(emxFutureFile, false));
                            if (vnew && vStorage) {
                                File sddir = new File(storageRoot);
                                sddir.mkdir();
                                emxCacheFile.write("<?xml version='1.0' encoding=\"UTF-8\"?>");
                                emxCacheFile.newLine();
                                emxCacheFile.write("  <PACKAGE>");
                            }
                            String pathTempChoice = "";
                            if (iBookFlag == 0) {
                                pathTempChoice = musicPath;
                            } else {
                                pathTempChoice = bookPath;
                            }
                            final String pathTemp = pathTempChoice;
                            final int newFilesFin = newFiles;
                            try {
                                parseTextView.post(new Runnable() {

                                    public void run() {
                                        parseTextView.setText(getString(R.string.checking_file_1_of) + " " + newFilesFin);
                                    }
                                });
                                URL currentTrackURL = new URL(newTrackURLs[0]);
                                HttpURLConnection currentTrackConnection = (HttpURLConnection) currentTrackURL.openConnection();
                                ifs = currentTrackConnection.getContentLength();
                                if (ifs == -1) {
                                    currentTrackConnection.disconnect();
                                    currentTrackConnection = (HttpURLConnection) currentTrackURL.openConnection();
                                    ifs = currentTrackConnection.getContentLength();
                                }
                                currentTrackConnection.disconnect();
                                if (ifs > 0) {
                                    final String filepath = pathTemp + newArtist + "/" + newAlbum + "/";
                                    File futureFile = new File(filepath, newTrackNames[0]);
                                    if (futureFile.exists()) {
                                        final long itmp = futureFile.length();
                                        if (itmp == ifs && itmp != 0) {
                                            newTrackStatus[0] = 2;
                                        }
                                    }
                                } else {
                                    newTrackURLs[0] = newArtURLSml;
                                    URL u2 = new URL(newTrackURLs[0]);
                                    HttpURLConnection c2 = (HttpURLConnection) u2.openConnection();
                                    ifs = c2.getContentLength();
                                    if (ifs == -1) {
                                        c2.disconnect();
                                        c2 = (HttpURLConnection) u2.openConnection();
                                        ifs = c2.getContentLength();
                                    }
                                    c2.disconnect();
                                    final String filepath = pathTemp + newArtist + "/" + newAlbum + "/";
                                    File futureFile = new File(filepath, newTrackNames[0]);
                                    if (futureFile.exists()) {
                                        final long itmp = futureFile.length();
                                        if (itmp == ifs && itmp != 0) {
                                            newTrackStatus[0] = 2;
                                        }
                                    }
                                }
                            } catch (Exception e) {
                            }
                            for (int i = 1; i < newFiles; i++) {
                                final int ifinal = i + 1;
                                parseTextView.post(new Runnable() {

                                    public void run() {
                                        parseTextView.setText(getString(R.string.checking_file_n) + " " + ifinal + " " + getString(R.string.of) + " " + newFilesFin);
                                    }
                                });
                                newTrackNums[i] = myXMLHandler.trackNumbers[i - 1];
                                newTrackURLs[i] = myXMLHandler.downloadURLs[i - 1];
                                newTrackNames[i] = myXMLHandler.downloadNames[i - 1].replace(":", "");
                                if (vnew && vStorage) {
                                    emxCacheFile.newLine();
                                    emxCacheFile.write("    <TRACK>");
                                    emxCacheFile.newLine();
                                    emxCacheFile.write("      <TRACKURL>" + newTrackURLs[i] + "</TRACKURL>");
                                    emxCacheFile.newLine();
                                    emxCacheFile.write("      <ALBUM>" + myXMLHandler.album.replace("&", "&amp;") + "</ALBUM>");
                                    emxCacheFile.newLine();
                                    emxCacheFile.write("      <ARTIST>" + myXMLHandler.artist.replace("&", "&amp;") + "</ARTIST>");
                                    emxCacheFile.newLine();
                                    emxCacheFile.write("      <TITLE>" + newTrackNames[i].replace("&", "&amp;") + "</TITLE>");
                                    emxCacheFile.newLine();
                                    emxCacheFile.write("      <GENRE>" + newGenre.replace("&", "&amp;") + "</GENRE>");
                                    emxCacheFile.newLine();
                                    emxCacheFile.write("      <TRACKNUM>" + newTrackNums[i].replace("&", "&amp;") + "</TRACKNUM>");
                                    emxCacheFile.newLine();
                                    emxCacheFile.write("      <ALBUMART>" + newArtURLSml + "</ALBUMART>");
                                    emxCacheFile.newLine();
                                    emxCacheFile.write("      <ALBUMARTLARGE>" + newArtURL + "</ALBUMARTLARGE>");
                                    emxCacheFile.newLine();
                                    emxCacheFile.write("    </TRACK>");
                                }
                                futureFileNames[i] = Utils.getTrackFileName(newTrackNames[i], newArtist, newAlbum, newTrackNums[i], thisActivity);
                                ifs = 0;
                                newTrackStatus[i] = 10;
                                try {
                                    URL currentTrackURL = new URL(newTrackURLs[i]);
                                    HttpURLConnection c = (HttpURLConnection) currentTrackURL.openConnection();
                                    ifs = c.getContentLength();
                                    if (ifs == -1) {
                                        c.disconnect();
                                        c = (HttpURLConnection) currentTrackURL.openConnection();
                                        ifs = c.getContentLength();
                                    }
                                    String fileType = c.getContentType();
                                    c.disconnect();
                                    final String filePath = pathTemp + newArtist + "/" + newAlbum + "/";
                                    File futureFile = new File(filePath, futureFileNames[i]);
                                    if (futureFile.exists()) {
                                        final long itmp = futureFile.length();
                                        if (itmp == ifs && itmp != 0) {
                                            newTrackStatus[i] = 2;
                                        } else if (itmp < ifs && itmp != 0) {
                                            newTrackStatus[i] = 12;
                                        }
                                    }
                                    if (fileType.lastIndexOf("audio") == -1 && fileType.lastIndexOf("octet") == -1) {
                                        newTrackStatus[i] = 3;
                                    }
                                } catch (Exception e) {
                                    newTrackStatus[i] = 3;
                                }
                            }
                            final String talbum = newAlbum;
                            final String tartist = newArtist;
                            final int tnfiles = newFiles;
                            final int tnfilesnew = nfilesNew;
                            try {
                                while (Utils.droidDB.isLocked()) {
                                    Thread.currentThread().sleep(1000);
                                }
                                if (Utils.droidDB.isLocked()) {
                                    Toast.makeText(thisActivity, R.string.database_locked, Toast.LENGTH_SHORT).show();
                                } else {
                                    if (vnew && vStorage) {
                                        emxCacheFile.newLine();
                                        emxCacheFile.write("  </PACKAGE>");
                                        emxCacheFile.close();
                                        Utils.droidDB.insertHistory(newAlbum, XMLAddress, "file://" + emxFutureFile);
                                    }
                                    List<Download> downloadsTemp = Utils.droidDB.getDownloads();
                                    for (int i = 0; i < newFiles; i++) {
                                        Boolean vFound = false;
                                        for (Download download : downloadsTemp) {
                                            if (download.trackurl.contains(newTrackURLs[i])) {
                                                vFound = true;
                                            }
                                        }
                                        if (!vFound) {
                                            Utils.droidDB.insertDownload(newArtist, newAlbum, newTrackNames[i], newArtURLSml, newTrackURLs[i], newTrackNums[i], iBookFlag, newTrackStatus[i]);
                                        }
                                    }
                                }
                            } catch (Exception e2) {
                                final Exception ef = e2;
                                Log.d("EMD - ", "Failed to insert download or history" + ef);
                            }
                            statusTextView.post(new Runnable() {

                                public void run() {
                                    statusTextView.setText(R.string.nothing_currently_downloading);
                                }
                            });
                            vConnecting = false;
                        } catch (Exception e) {
                            delay(1000);
                            Log.e("EMD - ", "Failed to parse " + nfailed);
                            Log.e("EMD - ", "Error message " + e);
                            if (nfailed == 0) {
                                final Exception ef = e;
                                handlerFail.sendEmptyMessage(0);
                                Log.d("EMD - ", "Parse failed " + ef);
                                parseTextView.post(new Runnable() {

                                    public void run() {
                                        parseTextView.setText(getString(R.string.cant_parse));
                                    }
                                });
                                vConnecting = false;
                                msgText = "Fail";
                            }
                        }
                    }
                }
                msg.obj = msgText;
                handlerDoneLoading.sendMessage(msg);
            }
