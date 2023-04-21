    public void paint(Graphics g) {
        try {
            updateCursorOnly = false;
            size = panel.getSize();
            boolean backbufferResize = false;
            if (size.width != oldSize.width || size.height != oldSize.height) {
                if (VirtualSpaceManager.debugModeON()) {
                    System.out.println("Resizing JPanel: (" + oldSize.width + "x" + oldSize.height + ") -> (" + size.width + "x" + size.height + ")");
                }
                oldSize = size;
                updateAntialias = true;
                updateFont = true;
                backbufferResize = true;
            }
            boolean drawLens = (lens != null);
            updateOffscreenBuffer(backbufferResize);
            standardStroke = stableRefToBackBufferGraphics.getStroke();
            standardTransform = stableRefToBackBufferGraphics.getTransform();
            if (drawLens) {
                lensG2D.setPaintMode();
                lensG2D.setBackground(backColor);
                lensG2D.clearRect(0, 0, lens.mbw, lens.mbh);
            }
            if (updateFont) {
                stableRefToBackBufferGraphics.setFont(VText.getMainFont());
                if (drawLens) {
                    for (int i = 0; i < backBufferGraphics.length; i++) {
                        backBufferGraphics[i].setFont(VText.getMainFont());
                    }
                    if (lensG2D != null) {
                        lensG2D.setFont(VText.getMainFont());
                    }
                }
                updateFont = false;
            }
            if (updateAntialias) {
                Object hint = antialias ? RenderingHints.VALUE_ANTIALIAS_ON : RenderingHints.VALUE_ANTIALIAS_OFF;
                stableRefToBackBufferGraphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, hint);
                if (drawLens) {
                    for (int i = 0; i < backBufferGraphics.length; i++) {
                        backBufferGraphics[i].setRenderingHint(RenderingHints.KEY_ANTIALIASING, hint);
                    }
                    if (lensG2D != null) {
                        lensG2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, hint);
                    }
                }
                updateAntialias = false;
            }
            if (notBlank) {
                stableRefToBackBufferGraphics.setPaintMode();
                stableRefToBackBufferGraphics.setBackground(backColor);
                stableRefToBackBufferGraphics.clearRect(0, 0, panel.getWidth(), panel.getHeight());
                backgroundHook();
                try {
                    for (int nbcam = 0; nbcam < cams.length; nbcam++) {
                        Camera camera = cams[nbcam];
                        if ((camera != null) && (camera.enabled) && ((camera.eager) || (camera.shouldRepaint()))) {
                            camIndex = cams[nbcam].getIndex();
                            drawnGlyphs = cams[nbcam].parentSpace.getDrawnGlyphs(camIndex);
                            drawnGlyphs.removeAllElements();
                            double uncoef = (cams[nbcam].focal + cams[nbcam].altitude) / cams[nbcam].focal;
                            double viewW = size.width;
                            double viewH = size.height;
                            double viewWC = cams[nbcam].vx - (viewW / 2 - visibilityPadding[0]) * uncoef;
                            double viewNC = cams[nbcam].vy + (viewH / 2 - visibilityPadding[1]) * uncoef;
                            double viewEC = cams[nbcam].vx + (viewW / 2 - visibilityPadding[2]) * uncoef;
                            double viewSC = cams[nbcam].vy - (viewH / 2 - visibilityPadding[3]) * uncoef;
                            double lviewWC = 0;
                            double lviewNC = 0;
                            double lviewEC = 0;
                            double lviewSC = 0;
                            double lensVx = 0;
                            double lensVy = 0;
                            if (drawLens) {
                                lviewWC = cams[nbcam].vx + (lens.lx - lens.lensWidth / 2) * uncoef;
                                lviewNC = cams[nbcam].vy + (-lens.ly + lens.lensHeight / 2) * uncoef;
                                lviewEC = cams[nbcam].vx + (lens.lx + lens.lensWidth / 2) * uncoef;
                                lviewSC = cams[nbcam].vy + (-lens.ly - lens.lensHeight / 2) * uncoef;
                                lensVx = (lviewWC + lviewEC) / 2d;
                                lensVy = (lviewSC + lviewNC) / 2d;
                            }
                            gll = cams[nbcam].parentSpace.getDrawingList();
                            for (int i = 0; i < gll.length; i++) {
                                if (gll[i].visibleInViewport(viewWC, viewNC, viewEC, viewSC, camera)) {
                                    gll[i].project(camera, size);
                                    if (gll[i].isVisible()) {
                                        gll[i].draw(backBufferGraphics[0], size.width, size.height, camera.getIndex(), standardStroke, standardTransform, 0, 0);
                                    }
                                    camera.parentSpace.drewGlyph(gll[i], camIndex);
                                }
                            }
                            if (drawLens) {
                                double sub = 0.2;
                                lviewWC = (long) (camera.vx + (lens.lx - lens.getRadius() * sub) * uncoef);
                                lviewNC = (long) (camera.vy + (-lens.ly + lens.getRadius() * sub) * uncoef);
                                lviewEC = (long) (camera.vx + (lens.lx + lens.getRadius() * sub) * uncoef);
                                lviewSC = (long) (camera.vy + (-lens.ly - lens.getRadius() * sub) * uncoef);
                                lensVx = (lviewWC + lviewEC) / 2;
                                lensVy = (lviewSC + lviewNC) / 2;
                                gll = cams[nbcam].parentSpace.getDrawingList();
                                for (int i = 0; i < gll.length; i++) {
                                    if (gll[i].visibleInViewport(lviewWC, lviewNC, lviewEC, lviewSC, camera)) {
                                        gll[i].projectForLens(camera, lens.mbw, lens.mbh, lens.getMaximumMagnification(), lensVx, lensVy);
                                        if (gll[i].isVisibleThroughLens()) {
                                            gll[i].drawForLens(lensG2D, lens.mbw, lens.mbh, camera.getIndex(), standardStroke, standardTransform, 0, 0);
                                        }
                                    }
                                }
                            }
                            gll = null;
                        }
                    }
                } catch (NullPointerException ex) {
                    if (VirtualSpaceManager.debugModeON()) {
                        System.err.println("GLViewPanel.run.paint " + ex);
                    }
                }
                foregroundHook();
                drawBackBuffer(g);
                if (drawLens) {
                    drawLens(g);
                }
                if (cursor_inside) {
                    try {
                        parent.mouse.unProject(cams[activeLayer], this);
                        if (parent.mouse.isSensitive()) {
                            parent.mouse.getPicker().computePickedGlyphList(evHs[activeLayer], cams[activeLayer], this);
                        }
                    } catch (NullPointerException ex) {
                        if (VirtualSpaceManager.debugModeON()) {
                            System.err.println("viewpanel.run.drawdrag " + ex);
                        }
                    }
                }
                synchronized (this) {
                    lastButOneRepaint = lastRepaint;
                    lastRepaint = System.currentTimeMillis();
                    delay = lastRepaint - lastButOneRepaint;
                }
            } else {
                stableRefToBackBufferGraphics.setPaintMode();
                stableRefToBackBufferGraphics.setColor(blankColor);
                stableRefToBackBufferGraphics.fillRect(0, 0, panel.getWidth(), panel.getHeight());
                portalsHook();
            }
        } catch (NullPointerException ex0) {
            if (VirtualSpaceManager.debugModeON()) {
                ex0.printStackTrace();
            }
        }
        if (repaintListener != null) {
            repaintListener.viewRepainted(this.parent);
        }
    }
