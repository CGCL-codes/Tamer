            public void run() {
                waiting(200);
                txtinfo.post(new Runnable() {

                    public void run() {
                        txtinfo.setText(R.string.searching);
                    }
                });
                try {
                    if (iListTypeFlag < 2) {
                        URL url = new URL(urlAddress);
                        SAXParserFactory spf = SAXParserFactory.newInstance();
                        SAXParser sp = spf.newSAXParser();
                        XMLReader xr = sp.getXMLReader();
                        XMLHandlerAlbums myXMLHandler = new XMLHandlerAlbums(iListTypeFlag);
                        xr.setContentHandler(myXMLHandler);
                        xr.parse(new InputSource(url.openStream()));
                        nResultsOnPage = myXMLHandler.nItems;
                        statuscode = myXMLHandler.statuscode;
                        if (statuscode != 200 && statuscode != 206) {
                            throw new Exception();
                        }
                        if (iListTypeFlag == 0) {
                            albums = new String[nResultsOnPage];
                            albumIds = new String[nResultsOnPage];
                            artists = new String[nResultsOnPage];
                            dates = new String[nResultsOnPage];
                            aurls = new String[nResultsOnPage];
                            images = new String[nResultsOnPage];
                            listText = new String[nResultsOnPage];
                            for (int i = 0; i < nResultsOnPage; i++) {
                                albumIds[i] = myXMLHandler.albumIds[i];
                                albums[i] = myXMLHandler.albums[i];
                                dates[i] = myXMLHandler.dates[i];
                                artists[i] = myXMLHandler.artists[i];
                                images[i] = myXMLHandler.images[i];
                                aurls[i] = myXMLHandler.urls[i] + "?FREF=400062";
                                listText[i] = albums[i] + " - " + artists[i] + " - " + getString(R.string.added) + " " + dates[i];
                            }
                        } else if (iListTypeFlag == 1) {
                            books = new String[nResultsOnPage];
                            authors = new String[nResultsOnPage];
                            aurls = new String[nResultsOnPage];
                            images = new String[nResultsOnPage];
                            albumIds = new String[nResultsOnPage];
                            listText = new String[nResultsOnPage];
                            dates = new String[nResultsOnPage];
                            for (int i = 0; i < nResultsOnPage; i++) {
                                books[i] = myXMLHandler.albums[i];
                                albumIds[i] = myXMLHandler.albumIds[i];
                                images[i] = myXMLHandler.images[i];
                                dates[i] = myXMLHandler.dates[i];
                                authors[i] = myXMLHandler.artists[i];
                                aurls[i] = myXMLHandler.urls[i] + "?FREF=400062";
                                listText[i] = books[i] + " - " + authors[i] + " - " + getString(R.string.added) + " " + dates[i];
                            }
                        }
                        nTotalResults = myXMLHandler.nTotalItems;
                    } else if (iListTypeFlag == 2) {
                        URL url = new URL(urlAddress);
                        SAXParserFactory spf = SAXParserFactory.newInstance();
                        SAXParser sp = spf.newSAXParser();
                        XMLReader xr = sp.getXMLReader();
                        XMLHandlerArtists myXMLHandler = new XMLHandlerArtists();
                        xr.setContentHandler(myXMLHandler);
                        xr.parse(new InputSource(url.openStream()));
                        nResultsOnPage = myXMLHandler.nItems;
                        statuscode = myXMLHandler.statuscode;
                        if (statuscode != 200) {
                            throw new Exception();
                        }
                        artists = new String[nResultsOnPage];
                        aurls = new String[nResultsOnPage];
                        albums = new String[nResultsOnPage];
                        listText = new String[nResultsOnPage];
                        for (int i = 0; i < nResultsOnPage; i++) {
                            artists[i] = myXMLHandler.artists[i];
                            aurls[i] = myXMLHandler.urls[i];
                            albums[i] = myXMLHandler.artistsId[i];
                            listText[i] = artists[i];
                        }
                        nTotalResults = myXMLHandler.nTotalItems;
                    } else if (iListTypeFlag == 4) {
                        URL url = new URL(urlAddress);
                        SAXParserFactory spf = SAXParserFactory.newInstance();
                        SAXParser sp = spf.newSAXParser();
                        XMLReader xr = sp.getXMLReader();
                        XMLHandlerLabels myXMLHandler = new XMLHandlerLabels();
                        xr.setContentHandler(myXMLHandler);
                        xr.parse(new InputSource(url.openStream()));
                        nResultsOnPage = myXMLHandler.nItems;
                        statuscode = myXMLHandler.statuscode;
                        if (statuscode != 200) {
                            throw new Exception();
                        }
                        artists = new String[nResultsOnPage];
                        aurls = new String[nResultsOnPage];
                        albums = new String[nResultsOnPage];
                        listText = new String[nResultsOnPage];
                        for (int i = 0; i < nResultsOnPage; i++) {
                            artists[i] = myXMLHandler.labels[i];
                            aurls[i] = myXMLHandler.urls[i];
                            albums[i] = myXMLHandler.labelsId[i];
                            listText[i] = artists[i];
                        }
                        nTotalResults = myXMLHandler.nTotalItems;
                    } else {
                        URL url = new URL(urlAddress);
                        SAXParserFactory spf = SAXParserFactory.newInstance();
                        SAXParser sp = spf.newSAXParser();
                        XMLReader xr = sp.getXMLReader();
                        XMLHandlerTracks myXMLHandler = new XMLHandlerTracks();
                        xr.setContentHandler(myXMLHandler);
                        xr.parse(new InputSource(url.openStream()));
                        nResultsOnPage = myXMLHandler.nItems;
                        statuscode = myXMLHandler.statuscode;
                        if (statuscode != 200) {
                            throw new Exception();
                        }
                        tracks = new String[nResultsOnPage];
                        albums = new String[nResultsOnPage];
                        albumIds = new String[nResultsOnPage];
                        artists = new String[nResultsOnPage];
                        images = new String[nResultsOnPage];
                        aurls = new String[nResultsOnPage];
                        listText = new String[nResultsOnPage];
                        for (int i = 0; i < nResultsOnPage; i++) {
                            albumIds[i] = myXMLHandler.albumIds[i];
                            albums[i] = myXMLHandler.albums[i];
                            images[i] = myXMLHandler.images[i];
                            tracks[i] = myXMLHandler.tracks[i];
                            artists[i] = myXMLHandler.artists[i];
                            aurls[i] = myXMLHandler.urls[i] + "?FREF=400062";
                            listText[i] = tracks[i] + " - " + artists[i] + " (Album: " + albums[i] + ")";
                        }
                        nTotalResults = myXMLHandler.nTotalItems;
                    }
                    final int fnmin = iFirstResultOnPage;
                    final int fnmax = iFirstResultOnPage + nResultsOnPage - 1;
                    final int fntotalitems = nTotalResults;
                    if (nTotalResults > fnmax) {
                        nextButton.post(new Runnable() {

                            public void run() {
                                nextButton.setVisibility(0);
                            }
                        });
                    } else {
                        nextButton.post(new Runnable() {

                            public void run() {
                                nextButton.setVisibility(8);
                            }
                        });
                    }
                    if (iFirstResultOnPage > 1) {
                        previousButton.post(new Runnable() {

                            public void run() {
                                previousButton.setVisibility(0);
                            }
                        });
                    } else if (nTotalResults > fnmax) {
                        previousButton.post(new Runnable() {

                            public void run() {
                                previousButton.setVisibility(8);
                            }
                        });
                    } else {
                        previousButton.post(new Runnable() {

                            public void run() {
                                previousButton.setVisibility(4);
                            }
                        });
                    }
                    txtinfo.post(new Runnable() {

                        public void run() {
                            if (title != null && title != "") {
                                txtinfo.setText(title + "\n" + getString(R.string.showing) + " " + fnmin + " " + getString(R.string.through) + " " + fnmax + " " + getString(R.string.of) + " " + fntotalitems);
                            } else {
                                txtinfo.setText(getString(R.string.showing) + " " + fnmin + " " + getString(R.string.through) + " " + fnmax + " " + getString(R.string.of) + " " + fntotalitems);
                            }
                        }
                    });
                    handlerSetList.sendEmptyMessage(0);
                } catch (Exception e) {
                    final Exception ef = e;
                    txtinfo.post(new Runnable() {

                        public void run() {
                            txtinfo.setText(R.string.search_failed);
                        }
                    });
                }
                dialog.dismiss();
                handlerDoneLoading.sendEmptyMessage(0);
            }
