    @Override
    protected synchronized void paintComponent(Graphics graphics0) {
        if (getCanvas().isValueAdjusting()) {
            repaint();
            return;
        }
        if (isOpaque()) {
            Color co = graphics0.getColor();
            graphics0.setColor(getBackground());
            Rectangle clip = DasDevicePosition.toRectangle(getRow(), getColumn());
            int dy = getRow().top() - this.getY();
            graphics0.fillRect(0, dy, clip.width + 1, clip.height + dy);
            graphics0.setColor(co);
        }
        if (!getCanvas().isPrintingThread() && !EventQueue.isDispatchThread()) {
            throw new RuntimeException("not event thread: " + Thread.currentThread().getName());
        }
        logger.finest("entering DasPlot.paintComponent");
        if (getCanvas().isPrintingThread()) {
            logger.fine("* printing thread *");
        }
        int x = getColumn().getDMinimum();
        int y = getRow().getDMinimum();
        int xSize = getColumn().getDMaximum() - x;
        int ySize = getRow().getDMaximum() - y;
        Shape saveClip;
        if (getCanvas().isPrintingThread()) {
            saveClip = graphics0.getClip();
            graphics0.setClip(null);
        } else {
            saveClip = null;
        }
        logger.log(Level.FINEST, "DasPlot clip={0} @ {1},{2}", new Object[] { graphics0.getClip(), getX(), getY() });
        Rectangle clip = graphics0.getClipBounds();
        if (clip != null && (clip.y + getY()) >= (y + ySize)) {
            logger.finer("returning because clip indicates nothing to be done.");
            return;
        }
        boolean disableImageCache = false;
        Graphics2D graphics = (Graphics2D) graphics0.create();
        Rectangle clip0 = graphics.getClipBounds();
        Rectangle plotClip = DasDevicePosition.toRectangle(getRow(), getColumn());
        plotClip.height += 2;
        plotClip.height += titleHeight;
        plotClip.width += 2;
        plotClip.translate(-x, -y);
        if (clip != null) plotClip = plotClip.intersection(clip);
        graphics.setClip(plotClip);
        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphics.translate(-getX(), -getY());
        boolean useCacheImage = cacheImageValid && !getCanvas().isPrintingThread() && !disableImageCache;
        if (useCacheImage) {
            Graphics2D atGraphics = (Graphics2D) graphics.create();
            AffineTransform at = getAffineTransform(xAxis, yAxis);
            if (at == null || (preview == false && !isIdentity(at))) {
                atGraphics.drawImage(cacheImage, cacheImageBounds.x, cacheImageBounds.y, cacheImageBounds.width, cacheImageBounds.height, this);
                paintInvalidScreen(atGraphics, at);
            } else {
                String atDesc;
                if (!at.isIdentity()) {
                    atDesc = GraphUtil.getATScaleTranslateString(at);
                    logger.log(Level.FINEST, " using cacheImage w/AT {0}", atDesc);
                    atGraphics.transform(at);
                } else {
                    atDesc = "identity";
                    logger.log(Level.FINEST, " using cacheImage {0} {1} {2}", new Object[] { cacheImageBounds, xmemento, ymemento });
                }
                if (cacheImageBounds.width != cacheImage.getWidth()) {
                    System.err.println(" cbw: " + cacheImageBounds.width + "  ciw:" + cacheImage.getWidth());
                }
                atGraphics.drawImage(cacheImage, cacheImageBounds.x, cacheImageBounds.y, cacheImageBounds.width, cacheImageBounds.height, this);
            }
            atGraphics.dispose();
        } else {
            synchronized (this) {
                Graphics2D plotGraphics;
                if (getCanvas().isPrintingThread() || disableImageCache) {
                    plotGraphics = (Graphics2D) graphics.create(x - 1, y - 1, xSize + 2, ySize + 2);
                    resetCacheImageBounds(true);
                    logger.finest(" printing thread, drawing");
                } else {
                    resetCacheImageBounds(false);
                    cacheImage = new BufferedImage(cacheImageBounds.width, cacheImageBounds.height, BufferedImage.TYPE_4BYTE_ABGR);
                    plotGraphics = (Graphics2D) cacheImage.getGraphics();
                    plotGraphics.setBackground(getBackground());
                    plotGraphics.setColor(getForeground());
                    plotGraphics.setRenderingHints(org.das2.DasProperties.getRenderingHints());
                    if (overSize) {
                        plotGraphics.translate(x - cacheImageBounds.x - 1, y - cacheImageBounds.y - 1);
                    }
                    logger.finest(" rebuilding cacheImage");
                }
                plotGraphics.translate(-x + 1, -y + 1);
                Renderer[] rends = getRenderers();
                if (rends.length > 0) {
                    Memento xmem = getXAxis().getMemento();
                    Memento ymem = getYAxis().getMemento();
                    for (Renderer r : rends) {
                        boolean dirt = false;
                        if (r.getXmemento() == null || !r.getXmemento().equals(xmem)) dirt = true;
                        if (r.getYmemento() == null || !r.getYmemento().equals(ymem)) dirt = true;
                        if (dirt) {
                            try {
                                r.updatePlotImage(getXAxis(), getYAxis(), new NullProgressMonitor());
                            } catch (DasException ex) {
                                Logger.getLogger(DasPlot.class.getName()).log(Level.SEVERE, null, ex);
                                ex.printStackTrace();
                            }
                            dirt = false;
                        }
                    }
                }
                drawCacheImage(plotGraphics);
            }
            if (!disableImageCache && !getCanvas().isPrintingThread()) {
                cacheImageValid = true;
                graphics.drawImage(cacheImage, cacheImageBounds.x, cacheImageBounds.y, cacheImageBounds.width, cacheImageBounds.height, this);
                xmemento = xAxis.getMemento();
                ymemento = yAxis.getMemento();
                logger.log(Level.FINEST, "recalc cacheImage, xmemento={0} ymemento={1}", new Object[] { xmemento, ymemento });
            }
        }
        graphics.setColor(getForeground());
        graphics.drawRect(x - 1, y - 1, xSize + 1, ySize + 1);
        if (clip0 != null) clip0.translate(getX(), getY());
        graphics.setClip(clip0);
        if (plotTitle != null && plotTitle.length() != 0) {
            GrannyTextRenderer gtr = new GrannyTextRenderer();
            gtr.setAlignment(GrannyTextRenderer.CENTER_ALIGNMENT);
            gtr.setString(graphics, plotTitle);
            int titleWidth = (int) gtr.getWidth();
            int titleX = x + (xSize - titleWidth) / 2;
            int titleY = y - (int) gtr.getDescent() - (int) gtr.getAscent() / 2;
            gtr.draw(graphics, (float) titleX, (float) titleY);
        }
        graphics.setClip(null);
        if (messages.size() > 0) {
            drawMessages(graphics);
        }
        if (legendElements.size() > 0) {
            drawLegend(graphics);
        }
        graphics.dispose();
        getDasMouseInputAdapter().paint(graphics0);
        if (saveClip != null) {
            graphics0.setClip(saveClip);
        }
    }
