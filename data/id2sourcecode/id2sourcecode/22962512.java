    private void getInfoFromXML() {
        final ProgressDialog dialog = ProgressDialog.show(this, "", getString(R.string.loading), true, true);
        setProgressBarIndeterminateVisibility(true);
        Thread t3 = new Thread() {

            public void run() {
                waiting(200);
                try {
                    URL url = new URL(urlAddress);
                    SAXParserFactory spf = SAXParserFactory.newInstance();
                    SAXParser sp = spf.newSAXParser();
                    XMLReader xr = sp.getXMLReader();
                    XMLHandlerSingleBook myXMLHandler = new XMLHandlerSingleBook();
                    xr.setContentHandler(myXMLHandler);
                    xr.parse(new InputSource(url.openStream()));
                    genre = myXMLHandler.genre;
                    publisher = myXMLHandler.publisher;
                    narrator = myXMLHandler.narrator;
                    edition = myXMLHandler.edition;
                    artist = myXMLHandler.author;
                    authorId = myXMLHandler.authorId;
                    if (edition == null) {
                        edition = "";
                    }
                    date = myXMLHandler.releaseDate;
                    rating = myXMLHandler.rating;
                    sampleURL = myXMLHandler.sampleURL;
                    imageURL = myXMLHandler.imageURL;
                    statuscode = myXMLHandler.statuscode;
                    if (statuscode != 200 && statuscode != 206) {
                        throw new Exception();
                    }
                    blurb = myXMLHandler.blurb;
                    blurb = blurb.replace("<br> ", "<br>");
                    blurbSource = myXMLHandler.blurbSource;
                    handlerSetContent.sendEmptyMessage(0);
                    dialog.dismiss();
                    updateImage();
                } catch (Exception e) {
                    final Exception ef = e;
                    nameTextView.post(new Runnable() {

                        public void run() {
                            nameTextView.setText(R.string.couldnt_get_book_info);
                        }
                    });
                    dialog.dismiss();
                }
                handlerDoneLoading.sendEmptyMessage(0);
            }
        };
        t3.start();
    }
