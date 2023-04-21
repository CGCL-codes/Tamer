        ImageIcon getImage(String urlString) {
            URL url = null;
            try {
                url = new URL(urlString);
            } catch (MalformedURLException mfuE) {
                mfuE.printStackTrace();
            }
            ImageIcon image = null;
            if (isContentCached()) {
                ObjectInputStream input = null;
                try {
                    input = new ObjectInputStream(new FileInputStream(getCacheFilePath()));
                    image = (ImageIcon) input.readObject();
                    Logger.getLogger(DemoPanel.class.getName()).log(Level.FINE, "Demo image loaded from: " + getCacheFilePath());
                } catch (Exception e) {
                    image = null;
                } finally {
                    if (null != input) try {
                        input.close();
                    } catch (IOException e) {
                    }
                }
            }
            if (null == image) {
                ObjectOutputStream output = null;
                try {
                    URLConnection conn = url.openConnection();
                    boolean defCache = conn.getDefaultUseCaches();
                    conn.setDefaultUseCaches(true);
                    image = new ImageIcon(url);
                    conn.setDefaultUseCaches(defCache);
                    output = new ObjectOutputStream(new FileOutputStream(getCacheFilePath()));
                    output.writeObject(image);
                } catch (Exception e) {
                    Logger.getLogger(DemoPanel.class.getName()).log(Level.FINE, "Error while caching Welcome Page demo image", e);
                    image = ImageUtilities.loadImageIcon(Constants.BROKEN_IMAGE, false);
                } finally {
                    if (null != output) {
                        try {
                            output.close();
                        } catch (IOException e) {
                        }
                    }
                }
            }
            return image;
        }
