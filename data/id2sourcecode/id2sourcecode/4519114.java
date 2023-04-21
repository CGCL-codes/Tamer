                        public void run() {
                            try {
                                final URL url = new URL(bookmarks);
                                Document doc = AutoplotUtil.readDoc(url.openStream());
                                List<Bookmark> book = Bookmark.parseBookmarks(doc);
                                model.setBookmarks(book);
                            } catch (Exception ex) {
                                ex.printStackTrace();
                                model.getCanvas().getApplication().getExceptionHandler().handle(ex);
                            }
                        }
