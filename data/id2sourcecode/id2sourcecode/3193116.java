            public void run() {
                final File cacheFile = new File(getTileDirName() + myZoom + File.separator + myTile2 + File.separator + myTile + ".png");
                if (cacheFile.exists() && cacheFile.length() > 0) try {
                    myBitmap = javax.imageio.ImageIO.read(cacheFile);
                    if (myBitmap != null && myBitmap.getWidth() > 0) {
                        repaintMe();
                        return;
                    }
                } catch (Exception e) {
                    LOG.log(Level.WARNING, "cannot load tile from cached file " + cacheFile.getAbsolutePath(), e);
                }
                try {
                    URL url = new URL("http://tile.openstreetmap.org/" + myZoom + "/" + myTile2 + "/" + myTile + ".png");
                    if (myTile2 < 0 || myTile < 0 || myTile2 >= 1 << myZoom || myTile >= 1 << myZoom) return;
                    try {
                        File dir = new File(getTileDirName());
                        if (!dir.exists()) {
                            dir.mkdirs();
                        }
                        javax.imageio.ImageIO.setCacheDirectory(dir);
                    } catch (Exception e) {
                        LOG.log(Level.WARNING, "cannot set ImageIO cache-directory to " + getTileDirName(), e);
                    }
                    try {
                        myBitmap = null;
                        URLConnection connection = url.openConnection();
                        connection.setRequestProperty("User-Agent", "osmnavigation SmoothTilePainter/" + OsmNavigationConfigSection.getVersion());
                        myBitmap = javax.imageio.ImageIO.read(connection.getInputStream());
                    } catch (UnknownHostException e) {
                        LOG.log(Level.INFO, "UnknownHostException, cannot download tile " + url.toExternalForm());
                    } catch (IIOException e) {
                        if (e.getCause() != null && e.getCause() instanceof UnknownHostException) {
                            LOG.log(Level.INFO, "UnknownHostException, cannot download tile " + url.toExternalForm());
                        } else {
                            LOG.log(Level.WARNING, "cannot download tile " + url.toExternalForm(), e);
                        }
                    }
                } catch (IOException e) {
                    LOG.log(Level.WARNING, "cannot download pre-rendered tile [" + e.getClass().getSimpleName() + "]", e);
                }
                if (myBitmap == null) try {
                    myBitmap = new BufferedImage(DEFAULTTILEWIDTH, DEFAULTTILEHEIGHT, BufferedImage.TYPE_INT_ARGB);
                    ODRPaintVisitor painter = new ODRPaintVisitor();
                    painter.setNoDataPainter(new SimplePaintVisitor());
                    painter.setFallbackPainter(new SimplePaintVisitor());
                    painter.setNavigatableComponent(new BufferdImageNavComponent());
                    painter.visitAll(getNavigatableComponent().getDataSet(), (Graphics2D) myBitmap.getGraphics());
                    repaintMe();
                    return;
                } catch (Exception e) {
                    myBitmap = null;
                    LOG.log(Level.WARNING, "cannot render tile", e);
                }
                if (myBitmap != null) {
                    try {
                        cacheFile.getParentFile().mkdirs();
                        javax.imageio.ImageIO.write(myBitmap, "png", cacheFile);
                    } catch (Exception e) {
                        LOG.log(Level.WARNING, "cannot save tile to cached file " + cacheFile.getAbsolutePath(), e);
                    }
                    repaintMe();
                }
            }
