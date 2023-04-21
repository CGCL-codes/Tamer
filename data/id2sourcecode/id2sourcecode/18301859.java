    public void run() {
        Thread me = Thread.currentThread();
        while (getSize().width <= 0) {
            try {
                runView.sleep(inactiveSleepTime);
            } catch (InterruptedException e) {
                if (parent.parent.debug) {
                    System.err.println("viewpanel.run.runview.sleep " + e);
                }
                return;
            }
        }
        backBufferGraphics = null;
        Graphics2D lensG2D = null;
        Dimension oldSize = getSize();
        while (runView == me) {
            loopStartTime = System.currentTimeMillis();
            if (notBlank) {
                if (active) {
                    if (shouldRepaint()) {
                        try {
                            shouldRepaint(false);
                            updateMouseOnly = false;
                            size = this.getSize();
                            viewW = size.width;
                            viewH = size.height;
                            if (size.width != oldSize.width || size.height != oldSize.height || backBufferW != size.width || backBufferH != size.height) {
                                backBuffer = null;
                                if (backBufferGraphics != null) {
                                    backBufferGraphics.dispose();
                                    backBufferGraphics = null;
                                }
                                if (lens != null) {
                                    lens.resetMagnificationBuffer();
                                    if (lensG2D != null) {
                                        lensG2D.dispose();
                                        lensG2D = null;
                                    }
                                }
                                if (parent.parent.debug) {
                                    System.out.println("Resizing JPanel: (" + oldSize.width + "x" + oldSize.height + ") -> (" + size.width + "x" + size.height + ")");
                                }
                                oldSize = size;
                                updateAntialias = true;
                                updateFont = true;
                            }
                            if (backBuffer == null) {
                                gconf = getGraphicsConfiguration();
                                backBuffer = gconf.createCompatibleImage(size.width, size.height);
                                backBufferW = backBuffer.getWidth();
                                backBufferH = backBuffer.getHeight();
                                if (backBufferGraphics != null) {
                                    backBufferGraphics.dispose();
                                    backBufferGraphics = null;
                                }
                            }
                            if (backBufferGraphics == null) {
                                backBufferGraphics = backBuffer.createGraphics();
                                updateAntialias = true;
                                updateFont = true;
                            }
                            if (lens != null) {
                                lensG2D = lens.getMagnificationGraphics();
                                lensG2D.setFont(VirtualSpaceManager.mainFont);
                                if (antialias) {
                                    lensG2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                                } else {
                                    lensG2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
                                }
                            }
                            if (updateFont) {
                                backBufferGraphics.setFont(VirtualSpaceManager.mainFont);
                                if (lensG2D != null) {
                                    lensG2D.setFont(VirtualSpaceManager.mainFont);
                                }
                                updateFont = false;
                            }
                            if (updateAntialias) {
                                if (antialias) {
                                    backBufferGraphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                                    if (lensG2D != null) {
                                        lensG2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                                    }
                                } else {
                                    backBufferGraphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
                                    if (lensG2D != null) {
                                        lensG2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
                                    }
                                }
                                updateAntialias = false;
                            }
                            stableRefToBackBufferGraphics = backBufferGraphics;
                            standardStroke = stableRefToBackBufferGraphics.getStroke();
                            standardTransform = stableRefToBackBufferGraphics.getTransform();
                            synchronized (this) {
                                stableRefToBackBufferGraphics.setPaintMode();
                                stableRefToBackBufferGraphics.setBackground(backColor);
                                stableRefToBackBufferGraphics.clearRect(0, 0, getWidth(), getHeight());
                                if (parent.painters[Java2DPainter.BACKGROUND] != null) {
                                    parent.painters[Java2DPainter.BACKGROUND].paint(stableRefToBackBufferGraphics, size.width, size.height);
                                }
                                if (lens != null) {
                                    synchronized (lens) {
                                        lensG2D.setPaintMode();
                                        lensG2D.setBackground(backColor);
                                        lensG2D.clearRect(0, 0, lens.mbw, lens.mbh);
                                        for (int nbcam = 0; nbcam < cams.length; nbcam++) {
                                            if ((cams[nbcam] != null) && (cams[nbcam].enabled) && ((cams[nbcam].eager) || (cams[nbcam].shouldRepaint()))) {
                                                camIndex = cams[nbcam].getIndex();
                                                drawnGlyphs = cams[nbcam].parentSpace.getDrawnGlyphs(camIndex);
                                                synchronized (drawnGlyphs) {
                                                    drawnGlyphs.removeAllElements();
                                                    uncoef = (float) ((cams[nbcam].focal + cams[nbcam].altitude) / cams[nbcam].focal);
                                                    viewWC = (long) (cams[nbcam].posx - (viewW / 2 - visibilityPadding[0]) * uncoef);
                                                    viewNC = (long) (cams[nbcam].posy + (viewH / 2 - visibilityPadding[1]) * uncoef);
                                                    viewEC = (long) (cams[nbcam].posx + (viewW / 2 - visibilityPadding[2]) * uncoef);
                                                    viewSC = (long) (cams[nbcam].posy - (viewH / 2 - visibilityPadding[3]) * uncoef);
                                                    lviewWC = (long) (cams[nbcam].posx + (lens.lx - lens.lensWidth / 2) * uncoef);
                                                    lviewNC = (long) (cams[nbcam].posy + (-lens.ly + lens.lensHeight / 2) * uncoef);
                                                    lviewEC = (long) (cams[nbcam].posx + (lens.lx + lens.lensWidth / 2) * uncoef);
                                                    lviewSC = (long) (cams[nbcam].posy + (-lens.ly - lens.lensHeight / 2) * uncoef);
                                                    lensVx = (lviewWC + lviewEC) / 2;
                                                    lensVy = (lviewSC + lviewNC) / 2;
                                                    gll = cams[nbcam].parentSpace.getDrawingList();
                                                    for (int i = 0; i < gll.length; i++) {
                                                        if (gll[i] != null) {
                                                            synchronized (gll[i]) {
                                                                if (gll[i].visibleInRegion(viewWC, viewNC, viewEC, viewSC, camIndex)) {
                                                                    gll[i].project(cams[nbcam], size);
                                                                    if (gll[i].isVisible()) {
                                                                        gll[i].draw(stableRefToBackBufferGraphics, size.width, size.height, cams[nbcam].getIndex(), standardStroke, standardTransform, 0, 0);
                                                                    }
                                                                    if (gll[i].visibleInRegion(lviewWC, lviewNC, lviewEC, lviewSC, camIndex)) {
                                                                        gll[i].projectForLens(cams[nbcam], lens.mbw, lens.mbh, lens.getMaximumMagnification(), lensVx, lensVy);
                                                                        if (gll[i].isVisibleThroughLens()) {
                                                                            gll[i].drawForLens(lensG2D, lens.mbw, lens.mbh, cams[nbcam].getIndex(), standardStroke, standardTransform, 0, 0);
                                                                        }
                                                                    }
                                                                    cams[nbcam].parentSpace.drewGlyph(gll[i], camIndex);
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                        if (parent.painters[Java2DPainter.FOREGROUND] != null) {
                                            parent.painters[Java2DPainter.FOREGROUND].paint(stableRefToBackBufferGraphics, size.width, size.height);
                                        }
                                        try {
                                            lens.transform(backBuffer);
                                            lens.drawBoundary(stableRefToBackBufferGraphics);
                                        } catch (ArrayIndexOutOfBoundsException ex) {
                                            if (VirtualSpaceManager.debugModeON()) {
                                                ex.printStackTrace();
                                            }
                                        } catch (NullPointerException ex2) {
                                            if (VirtualSpaceManager.debugModeON()) {
                                                ex2.printStackTrace();
                                            }
                                        }
                                        if (parent.painters[Java2DPainter.AFTER_LENSES] != null) {
                                            parent.painters[Java2DPainter.AFTER_LENSES].paint(stableRefToBackBufferGraphics, size.width, size.height);
                                        }
                                        for (int i = 0; i < parent.portals.length; i++) {
                                            parent.portals[i].paint(stableRefToBackBufferGraphics, size.width, size.height);
                                        }
                                        if (parent.painters[Java2DPainter.AFTER_PORTALS] != null) {
                                            parent.painters[Java2DPainter.AFTER_PORTALS].paint(stableRefToBackBufferGraphics, size.width, size.height);
                                        }
                                    }
                                } else {
                                    for (int nbcam = 0; nbcam < cams.length; nbcam++) {
                                        if ((cams[nbcam] != null) && (cams[nbcam].enabled) && ((cams[nbcam].eager) || (cams[nbcam].shouldRepaint()))) {
                                            camIndex = cams[nbcam].getIndex();
                                            drawnGlyphs = cams[nbcam].parentSpace.getDrawnGlyphs(camIndex);
                                            synchronized (drawnGlyphs) {
                                                drawnGlyphs.removeAllElements();
                                                uncoef = (float) ((cams[nbcam].focal + cams[nbcam].altitude) / cams[nbcam].focal);
                                                viewWC = (long) (cams[nbcam].posx - (viewW / 2 - visibilityPadding[0]) * uncoef);
                                                viewNC = (long) (cams[nbcam].posy + (viewH / 2 - visibilityPadding[1]) * uncoef);
                                                viewEC = (long) (cams[nbcam].posx + (viewW / 2 - visibilityPadding[2]) * uncoef);
                                                viewSC = (long) (cams[nbcam].posy - (viewH / 2 - visibilityPadding[3]) * uncoef);
                                                gll = cams[nbcam].parentSpace.getDrawingList();
                                                for (int i = 0; i < gll.length; i++) {
                                                    if (gll[i] != null) {
                                                        synchronized (gll[i]) {
                                                            if (gll[i].visibleInRegion(viewWC, viewNC, viewEC, viewSC, camIndex)) {
                                                                gll[i].project(cams[nbcam], size);
                                                                if (gll[i].isVisible()) {
                                                                    gll[i].draw(stableRefToBackBufferGraphics, size.width, size.height, camIndex, standardStroke, standardTransform, 0, 0);
                                                                }
                                                                cams[nbcam].parentSpace.drewGlyph(gll[i], camIndex);
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                    if (parent.painters[Java2DPainter.FOREGROUND] != null) {
                                        parent.painters[Java2DPainter.FOREGROUND].paint(stableRefToBackBufferGraphics, size.width, size.height);
                                    }
                                    if (parent.painters[Java2DPainter.AFTER_LENSES] != null) {
                                        parent.painters[Java2DPainter.AFTER_LENSES].paint(stableRefToBackBufferGraphics, size.width, size.height);
                                    }
                                    for (int i = 0; i < parent.portals.length; i++) {
                                        parent.portals[i].paint(stableRefToBackBufferGraphics, size.width, size.height);
                                    }
                                    if (parent.painters[Java2DPainter.AFTER_PORTALS] != null) {
                                        parent.painters[Java2DPainter.AFTER_PORTALS].paint(stableRefToBackBufferGraphics, size.width, size.height);
                                    }
                                }
                                if (inside) {
                                    try {
                                        parent.mouse.unProject(cams[activeLayer], this);
                                        if (computeListAtEachRepaint && parent.mouse.isSensitive()) {
                                            parent.mouse.computeMouseOverList(evHs[activeLayer], cams[activeLayer], this.lens);
                                        }
                                    } catch (NullPointerException ex) {
                                        if (parent.parent.debug) {
                                            System.err.println("viewpanel.run.drawdrag " + ex);
                                        }
                                    }
                                    stableRefToBackBufferGraphics.setColor(parent.mouse.hcolor);
                                    if (drawDrag) {
                                        stableRefToBackBufferGraphics.drawLine(origDragx, origDragy, parent.mouse.mx, parent.mouse.my);
                                    }
                                    if (drawRect) {
                                        stableRefToBackBufferGraphics.drawRect(Math.min(origDragx, parent.mouse.mx), Math.min(origDragy, parent.mouse.my), Math.max(origDragx, parent.mouse.mx) - Math.min(origDragx, parent.mouse.mx), Math.max(origDragy, parent.mouse.my) - Math.min(origDragy, parent.mouse.my));
                                    }
                                    if (drawOval) {
                                        if (circleOnly) {
                                            stableRefToBackBufferGraphics.drawOval(origDragx - Math.abs(origDragx - parent.mouse.mx), origDragy - Math.abs(origDragx - parent.mouse.mx), 2 * Math.abs(origDragx - parent.mouse.mx), 2 * Math.abs(origDragx - parent.mouse.mx));
                                        } else {
                                            stableRefToBackBufferGraphics.drawOval(origDragx - Math.abs(origDragx - parent.mouse.mx), origDragy - Math.abs(origDragy - parent.mouse.my), 2 * Math.abs(origDragx - parent.mouse.mx), 2 * Math.abs(origDragy - parent.mouse.my));
                                        }
                                    }
                                    if (drawVTMcursor) {
                                        synchronized (this) {
                                            stableRefToBackBufferGraphics.setXORMode(backColor);
                                            parent.mouse.draw(stableRefToBackBufferGraphics);
                                            oldX = parent.mouse.mx;
                                            oldY = parent.mouse.my;
                                        }
                                    }
                                }
                                if (stableRefToBackBufferGraphics == backBufferGraphics) {
                                    repaint();
                                }
                                loopTotalTime = System.currentTimeMillis() - loopStartTime;
                                timeToSleep = frameTime - loopTotalTime;
                            }
                        } catch (NullPointerException ex0) {
                            if (parent.parent.debug) {
                                System.err.println("viewpanel.run (probably due to backBuffer.createGraphics()) " + ex0);
                                ex0.printStackTrace();
                            }
                        }
                        try {
                            runView.sleep((timeToSleep > minimumSleepTime) ? timeToSleep : minimumSleepTime);
                        } catch (InterruptedException e) {
                            if (parent.parent.debug) {
                                System.err.println("viewpanel.run.runview.sleep2 " + e);
                            }
                            return;
                        }
                    } else if (updateMouseOnly) {
                        updateMouseOnly = false;
                        try {
                            parent.mouse.unProject(cams[activeLayer], this);
                            if (computeListAtEachRepaint && parent.mouse.isSensitive()) {
                                parent.mouse.computeMouseOverList(evHs[activeLayer], cams[activeLayer], this.lens);
                            }
                        } catch (NullPointerException ex) {
                            if (parent.parent.debug) {
                                System.err.println("viewpanel.run.drawdrag " + ex);
                            }
                        }
                        if (drawVTMcursor) {
                            synchronized (this) {
                                try {
                                    stableRefToBackBufferGraphics.setXORMode(backColor);
                                    stableRefToBackBufferGraphics.setColor(parent.mouse.color);
                                    stableRefToBackBufferGraphics.drawLine(oldX - parent.mouse.size, oldY, oldX + parent.mouse.size, oldY);
                                    stableRefToBackBufferGraphics.drawLine(oldX, oldY - parent.mouse.size, oldX, oldY + parent.mouse.size);
                                    stableRefToBackBufferGraphics.drawLine(parent.mouse.mx - parent.mouse.size, parent.mouse.my, parent.mouse.mx + parent.mouse.size, parent.mouse.my);
                                    stableRefToBackBufferGraphics.drawLine(parent.mouse.mx, parent.mouse.my - parent.mouse.size, parent.mouse.mx, parent.mouse.my + parent.mouse.size);
                                    oldX = parent.mouse.mx;
                                    oldY = parent.mouse.my;
                                } catch (NullPointerException ex47) {
                                    if (parent.parent.debug) {
                                        System.err.println("viewpanel.run.runview.drawVTMcursor " + ex47);
                                    }
                                }
                            }
                        }
                        repaint();
                        loopTotalTime = System.currentTimeMillis() - loopStartTime;
                        timeToSleep = frameTime - loopTotalTime;
                        try {
                            runView.sleep((timeToSleep > minimumSleepTime) ? timeToSleep : minimumSleepTime);
                        } catch (InterruptedException e) {
                            if (parent.parent.debug) {
                                System.err.println("viewpanel.run.runview.sleep3 " + e);
                            }
                            return;
                        }
                    } else {
                        try {
                            runView.sleep(frameTime + noRepaintAdditionalTime);
                        } catch (InterruptedException e) {
                            if (parent.parent.debug) {
                                System.err.println("viewpanel.run.runview.sleep4 " + e);
                            }
                            return;
                        }
                    }
                } else {
                    try {
                        runView.sleep(inactiveSleepTime);
                    } catch (InterruptedException e) {
                        if (parent.parent.debug) {
                            System.err.println("viewpanel.run.runview.sleep5 " + e);
                        }
                        return;
                    }
                }
            } else {
                size = this.getSize();
                viewW = size.width;
                viewH = size.height;
                if (size.width != oldSize.width || size.height != oldSize.height || backBufferW != size.width || backBufferH != size.height) {
                    backBuffer = null;
                    if (backBufferGraphics != null) {
                        backBufferGraphics.dispose();
                        backBufferGraphics = null;
                    }
                    if (lens != null) {
                        lens.resetMagnificationBuffer();
                        if (lensG2D != null) {
                            lensG2D.dispose();
                            lensG2D = null;
                        }
                    }
                    if (parent.parent.debug) {
                        System.out.println("Resizing JPanel: (" + oldSize.width + "x" + oldSize.height + ") -> (" + size.width + "x" + size.height + ")");
                    }
                    oldSize = size;
                    updateAntialias = true;
                    updateFont = true;
                }
                if (backBuffer == null) {
                    gconf = getGraphicsConfiguration();
                    backBuffer = gconf.createCompatibleImage(size.width, size.height);
                    backBufferW = backBuffer.getWidth();
                    backBufferH = backBuffer.getHeight();
                    if (backBufferGraphics != null) {
                        backBufferGraphics.dispose();
                        backBufferGraphics = null;
                    }
                }
                if (backBufferGraphics == null) {
                    backBufferGraphics = backBuffer.createGraphics();
                    updateAntialias = true;
                    updateFont = true;
                }
                if (lens != null) {
                    lensG2D = lens.getMagnificationGraphics();
                    lensG2D.setFont(VirtualSpaceManager.mainFont);
                    if (antialias) {
                        lensG2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    } else {
                        lensG2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
                    }
                }
                if (updateFont) {
                    backBufferGraphics.setFont(VirtualSpaceManager.mainFont);
                    if (lensG2D != null) {
                        lensG2D.setFont(VirtualSpaceManager.mainFont);
                    }
                    updateFont = false;
                }
                if (updateAntialias) {
                    if (antialias) {
                        backBufferGraphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                        if (lensG2D != null) {
                            lensG2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                        }
                    } else {
                        backBufferGraphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
                        if (lensG2D != null) {
                            lensG2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
                        }
                    }
                    updateAntialias = false;
                }
                stableRefToBackBufferGraphics = backBufferGraphics;
                standardStroke = stableRefToBackBufferGraphics.getStroke();
                standardTransform = stableRefToBackBufferGraphics.getTransform();
                stableRefToBackBufferGraphics.setPaintMode();
                stableRefToBackBufferGraphics.setColor(blankColor);
                stableRefToBackBufferGraphics.fillRect(0, 0, getWidth(), getHeight());
                if (parent.painters[Java2DPainter.AFTER_PORTALS] != null) {
                    try {
                        parent.painters[Java2DPainter.AFTER_PORTALS].paint(stableRefToBackBufferGraphics, size.width, size.height);
                    } catch (ClassCastException ex) {
                        if (VirtualSpaceManager.debugModeON()) {
                            System.err.println("Failed to draw AFTER_PORTALS in blank mode");
                        }
                    }
                }
                repaint();
                try {
                    runView.sleep(blankSleepTime);
                } catch (InterruptedException e) {
                    if (parent.parent.debug) {
                        System.err.println("viewpanel.run.runview.sleep5 " + e);
                    }
                    return;
                }
            }
        }
        if (stableRefToBackBufferGraphics != null) {
            stableRefToBackBufferGraphics.dispose();
        }
    }
