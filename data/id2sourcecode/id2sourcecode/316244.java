    public void mouseReleaseAction(MouseEvent mevt, main_canvas theCanvas) {
        _prof.prof.cnt[11034]++;
        _prof.prof.cnt[11035]++;
        int endX = (int) (mevt.getX());
        _prof.prof.cnt[11036]++;
        int endY = (int) (mevt.getY());
        {
            _prof.prof.cnt[11037]++;
            if (endX != x && endY != y) {
                _prof.prof.cnt[11038]++;
                dragged = true;
            }
        }
        {
            _prof.prof.cnt[11039]++;
            if (!dragged) {
                _prof.prof.cnt[11040]++;
                double currentZoom = theCanvas.getZoom();
                {
                    _prof.prof.cnt[11041]++;
                    if (SwingUtilities.isLeftMouseButton(mevt)) {
                        {
                            _prof.prof.cnt[11042]++;
                            if (currentZoom < 8.0) {
                                {
                                    _prof.prof.cnt[11043]++;
                                    theCanvas.setZoom(currentZoom * 2.0);
                                }
                                {
                                    _prof.prof.cnt[11044]++;
                                    theCanvas.setOldZoom(currentZoom);
                                }
                            }
                        }
                    } else {
                        {
                            _prof.prof.cnt[11045]++;
                            if (currentZoom > 1.0) {
                                {
                                    _prof.prof.cnt[11046]++;
                                    theCanvas.setZoom(currentZoom * 0.5);
                                }
                                {
                                    _prof.prof.cnt[11047]++;
                                    theCanvas.setOldZoom(currentZoom);
                                }
                            }
                        }
                    }
                }
                {
                    _prof.prof.cnt[11048]++;
                    theZoom = theCanvas.getZoom();
                }
                _prof.prof.cnt[11049]++;
                Point focus = new Point((int) (x * theZoom), (int) (y * theZoom));
                {
                    _prof.prof.cnt[11050]++;
                    theCanvas.pictureScrollPane.getViewport().setExtentSize(new Dimension(theCanvas.getBufferedImage().getWidth(), theCanvas.getBufferedImage().getHeight()));
                }
                {
                    _prof.prof.cnt[11051]++;
                    theCanvas.pictureScrollPane.getViewport().setViewSize(new Dimension((int) theZoom, (int) theZoom));
                }
                {
                    _prof.prof.cnt[11052]++;
                    theCanvas.pictureScrollPane.getViewport().setViewPosition(theCanvas.pictureScrollPane.getViewport().toViewCoordinates(focus));
                }
                {
                    _prof.prof.cnt[11053]++;
                    System.out.println(x + " " + y + " is " + theCanvas.pictureScrollPane.getViewport().toViewCoordinates(focus));
                }
                {
                    _prof.prof.cnt[11054]++;
                    theCanvas.repaint();
                }
            } else {
                {
                    _prof.prof.cnt[11055]++;
                    theZoom = theCanvas.getZoom();
                }
                _prof.prof.cnt[11056]++;
                int centerX = (x + endX) / 2;
                _prof.prof.cnt[11057]++;
                int centerY = (y + endY) / 2;
                _prof.prof.cnt[11058]++;
                double zoomFactorX = (endX - x);
                {
                    _prof.prof.cnt[11059]++;
                    if (zoomFactorX < 0) {
                        _prof.prof.cnt[11060]++;
                        zoomFactorX = (x - endX);
                    }
                }
                _prof.prof.cnt[11061]++;
                double zoomFactorY = (endY - y);
                {
                    _prof.prof.cnt[11062]++;
                    if (zoomFactorY < 0) {
                        _prof.prof.cnt[11063]++;
                        zoomFactorY = (y - endY);
                    }
                }
                _prof.prof.cnt[11064]++;
                double width = theCanvas.pictureScrollPane.getViewport().getExtentSize().getWidth();
                _prof.prof.cnt[11065]++;
                double height = theCanvas.pictureScrollPane.getViewport().getExtentSize().getHeight();
                _prof.prof.cnt[11066]++;
                double zoomFactor = width / zoomFactorX;
                _prof.prof.cnt[11067]++;
                double check = height / zoomFactorY;
                {
                    _prof.prof.cnt[11068]++;
                    if (check < zoomFactor) {
                        _prof.prof.cnt[11069]++;
                        zoomFactor = check;
                    }
                }
                _prof.prof.cnt[11070]++;
                double currentZoom = theCanvas.getZoom();
                {
                    _prof.prof.cnt[11071]++;
                    zoomFactor = (int) zoomFactor;
                }
                {
                    _prof.prof.cnt[11072]++;
                    theCanvas.setZoom(zoomFactor);
                }
                {
                    _prof.prof.cnt[11073]++;
                    System.out.println("zoomFactor to: " + zoomFactor);
                }
                {
                    _prof.prof.cnt[11074]++;
                    theCanvas.setOldZoom(currentZoom);
                }
                {
                    _prof.prof.cnt[11075]++;
                    theZoom = theCanvas.getZoom();
                }
                _prof.prof.cnt[11076]++;
                int myX = x;
                {
                    _prof.prof.cnt[11077]++;
                    if (endX < x) {
                        _prof.prof.cnt[11078]++;
                        myX = endX;
                    }
                }
                _prof.prof.cnt[11079]++;
                int myY = y;
                {
                    _prof.prof.cnt[11080]++;
                    if (endY < y) {
                        _prof.prof.cnt[11081]++;
                        myY = endY;
                    }
                }
                _prof.prof.cnt[11082]++;
                Point focus = new Point((int) (myX * theZoom), (int) (myY * theZoom));
                {
                    _prof.prof.cnt[11083]++;
                    theCanvas.pictureScrollPane.getViewport().setExtentSize(new Dimension(theCanvas.getBufferedImage().getWidth(), theCanvas.getBufferedImage().getHeight()));
                }
                {
                    _prof.prof.cnt[11084]++;
                    theCanvas.pictureScrollPane.getViewport().setViewSize(new Dimension((int) zoomFactorX, (int) zoomFactorY));
                }
                {
                    _prof.prof.cnt[11085]++;
                    theCanvas.pictureScrollPane.getViewport().setViewPosition(theCanvas.pictureScrollPane.getViewport().toViewCoordinates(focus));
                }
                {
                    _prof.prof.cnt[11086]++;
                    System.out.println(theCanvas.pictureScrollPane.getViewport().getViewPosition().getX() + " " + theCanvas.pictureScrollPane.getViewport().getViewPosition().getY());
                }
                {
                    _prof.prof.cnt[11087]++;
                    if (SwingUtilities.isLeftMouseButton(mevt) || theCanvas.zoomFactor >= 1) {
                        {
                            _prof.prof.cnt[11088]++;
                            if (theCanvas.main_image.getWidth() * theCanvas.zoomFactor * theCanvas.main_image.getHeight() * theCanvas.zoomFactor < 10000000) {
                                _prof.prof.cnt[11089]++;
                                theCanvas.repaint();
                            } else {
                                _prof.prof.cnt[11090]++;
                                theCanvas.zoomFactor = theCanvas.oldZoomFactor;
                            }
                        }
                    }
                }
            }
        }
    }
