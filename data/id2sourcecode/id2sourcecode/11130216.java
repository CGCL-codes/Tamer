            public void run() {
                InputStream is = null;
                URL url = deviceIcon.getUrl();
                try {
                    is = url.openStream();
                    final ImageData[] images = new ImageLoader().load(is);
                    if (images != null && images.length > 0) {
                        newItem.getDisplay().asyncExec(new Runnable() {

                            public void run() {
                                if (!newItem.isDisposed()) {
                                    newItem.setImage(new Image(newItem.getDisplay(), images[0]));
                                }
                            }
                        });
                    }
                } catch (IOException e) {
                } finally {
                    if (is != null) {
                        try {
                            is.close();
                        } catch (IOException ex1) {
                        }
                    }
                }
            }
