            public void run() {
                if ((imageLink != null) && !imageLink.equals("")) {
                    try {
                        URL url = new URL(imageLink);
                        URLConnection conn = url.openConnection();
                        conn.connect();
                        BufferedInputStream bis = new BufferedInputStream(conn.getInputStream());
                        imageBitmap = BitmapFactory.decodeStream(bis);
                    } catch (IOException e) {
                        Log.e(Constants.LOG_TAG, e.getMessage(), e);
                    }
                }
                handler.sendEmptyMessage(1);
            }
