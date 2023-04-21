    public void preload() {
        synchronized (loadingLock) {
            if (image == null) {
                MediaTracker mt = new MediaTracker(_c);
                Image im;
                try {
                    URL url = getResourceLocator().toURL();
                    InputStream is = url.openStream();
                    is.close();
                    im = Toolkit.getDefaultToolkit().createImage(url);
                    mt.addImage(im, 0);
                    mt.waitForID(0);
                    if (mt.statusID(0, false) == MediaTracker.COMPLETE) {
                        image = im;
                    }
                } catch (Exception e) {
                    logger.warning("Failed to load image from: " + getResourceLocator() + "\r\nProblem: " + e);
                }
            }
        }
    }
