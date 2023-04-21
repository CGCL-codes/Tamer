    private void getListFromXML() {
        Thread ta = new Thread() {

            public void run() {
                try {
                    handlerBeginLoading.sendEmptyMessage(0);
                    long currenttime = System.currentTimeMillis();
                    droidDB = new emuDB(thisActivity);
                    long cachetime = droidDB.getCachetime();
                    long arttime = droidDB.getArttime();
                    if ((currenttime - arttime) >= 604800000) {
                        droidDB.updateArttime(currenttime);
                        LazyAdapter adapter = new LazyAdapter(thisActivity);
                        adapter.imageLoader.clearCache();
                        adapter = null;
                    }
                    droidDB.close();
                    if ((currenttime - cachetime) > 14400000) {
                        XMLHandlerAlbums myXMLHandler = null;
                        XMLHandlerArtists myXMLHandlerArt = null;
                        for (int ilist = 0; ilist < 3; ilist++) {
                            URL url = new URL(XMLAddress[ilist]);
                            SAXParserFactory spf = SAXParserFactory.newInstance();
                            SAXParser sp = spf.newSAXParser();
                            XMLReader xr = sp.getXMLReader();
                            if (ilist < 2) {
                                myXMLHandler = new XMLHandlerAlbums(ilist);
                            } else {
                                myXMLHandlerArt = new XMLHandlerArtists();
                            }
                            Message handlermsg = new Message();
                            handlermsg.arg1 = ilist;
                            if (ilist == 0) {
                                xr.setContentHandler(myXMLHandler);
                                xr.parse(new InputSource(url.openStream()));
                                numberOfResults = myXMLHandler.nItems;
                                handlermsg.arg2 = numberOfResults;
                                statuscode = myXMLHandler.statuscode;
                                if (statuscode != 200 && statuscode != 206) {
                                    throw new Exception();
                                }
                                albums = new String[numberOfResults];
                                displayAlbums = new String[numberOfResults];
                                albumArtists = new String[numberOfResults];
                                albumURLs = new String[numberOfResults];
                                albumIds = new String[numberOfResults];
                                albumImages = new String[numberOfResults];
                                for (int i = 0; i < numberOfResults; i++) {
                                    albums[i] = myXMLHandler.albums[i];
                                    albumArtists[i] = myXMLHandler.artists[i];
                                    albumIds[i] = myXMLHandler.albumIds[i];
                                    albumImages[i] = myXMLHandler.images[i];
                                    displayAlbums[i] = albums[i] + " - " + albumArtists[i];
                                    albumURLs[i] = myXMLHandler.urls[i];
                                }
                                vAlbumCacheSaved = true;
                            } else if (ilist == 1) {
                                xr.setContentHandler(myXMLHandler);
                                xr.parse(new InputSource(url.openStream()));
                                numberOfResults = myXMLHandler.nItems;
                                handlermsg.arg2 = numberOfResults;
                                statuscode = myXMLHandler.statuscode;
                                if (statuscode != 200) {
                                    throw new Exception();
                                }
                                books = new String[numberOfResults];
                                bookIds = new String[numberOfResults];
                                bookImages = new String[numberOfResults];
                                authors = new String[numberOfResults];
                                bookURLs = new String[numberOfResults];
                                displayBooks = new String[numberOfResults];
                                for (int i = 0; i < numberOfResults; i++) {
                                    books[i] = myXMLHandler.albums[i];
                                    authors[i] = myXMLHandler.artists[i];
                                    displayBooks[i] = books[i] + " - " + authors[i];
                                    bookIds[i] = myXMLHandler.albumIds[i];
                                    bookImages[i] = myXMLHandler.images[i];
                                    bookURLs[i] = myXMLHandler.urls[i];
                                }
                                vBookCacheSaved = true;
                            } else if (ilist == 2) {
                                xr.setContentHandler(myXMLHandlerArt);
                                xr.parse(new InputSource(url.openStream()));
                                numberOfResults = myXMLHandler.nItems;
                                handlermsg.arg2 = numberOfResults;
                                statuscode = myXMLHandler.statuscode;
                                if (statuscode != 200) {
                                    throw new Exception();
                                }
                                artists = new String[numberOfResults];
                                artistIds = new String[numberOfResults];
                                artistURLs = new String[numberOfResults];
                                for (int i = 0; i < numberOfResults; i++) {
                                    artists[i] = myXMLHandlerArt.artists[i];
                                    artistIds[i] = myXMLHandlerArt.artistsId[i];
                                    artistURLs[i] = myXMLHandlerArt.urls[i];
                                }
                                vArtistCacheSaved = true;
                            }
                            handlerSetContent.sendMessage(handlermsg);
                        }
                        errorTextView.post(new Runnable() {

                            public void run() {
                                errorTextView.setVisibility(8);
                            }
                        });
                    } else {
                        Log.d("EMD - ", "Not time to update yet");
                    }
                } catch (Exception e) {
                    Log.d("EMD - ", "Failed to get chart info - " + e);
                    errorTextView.post(new Runnable() {

                        public void run() {
                            errorTextView.setText(R.string.failed_to_update_chart_info);
                            errorTextView.setVisibility(0);
                        }
                    });
                }
                handlerDoneLoading.sendEmptyMessage(0);
            }
        };
        ta.start();
    }
