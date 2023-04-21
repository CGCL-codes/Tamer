    Roi getRoi() {
        IJ.showStatus("Converting threshold to selection");
        boolean[] prevRow, thisRow;
        ArrayList polygons = new ArrayList();
        Outline[] outline;
        int progressInc = Math.max(h / 50, 1);
        prevRow = new boolean[w + 2];
        thisRow = new boolean[w + 2];
        outline = new Outline[w + 1];
        for (int y = 0; y <= h; y++) {
            boolean[] b = prevRow;
            prevRow = thisRow;
            thisRow = b;
            for (int x = 0; x <= w; x++) {
                if (y < h && x < w) thisRow[x + 1] = selected(x, y); else thisRow[x + 1] = false;
                if (thisRow[x + 1]) {
                    if (!prevRow[x + 1]) {
                        if (outline[x] == null) {
                            if (outline[x + 1] == null) {
                                outline[x + 1] = outline[x] = new Outline();
                                outline[x].push(x + 1, y);
                                outline[x].push(x, y);
                            } else {
                                outline[x] = outline[x + 1];
                                outline[x + 1] = null;
                                outline[x].push(x, y);
                            }
                        } else {
                            if (outline[x + 1] == null) {
                                outline[x + 1] = outline[x];
                                outline[x] = null;
                                outline[x + 1].shift(x + 1, y);
                            } else if (outline[x + 1] == outline[x]) {
                                polygons.add(outline[x].getPolygon());
                                outline[x] = outline[x + 1] = null;
                            } else {
                                outline[x].shift(outline[x + 1]);
                                for (int x1 = 0; x1 <= w; x1++) if (x1 != x + 1 && outline[x1] == outline[x + 1]) {
                                    outline[x1] = outline[x];
                                    outline[x] = outline[x + 1] = null;
                                    break;
                                }
                                if (outline[x] != null) throw new RuntimeException("assertion failed");
                            }
                        }
                    }
                    if (!thisRow[x]) {
                        if (outline[x] == null) throw new RuntimeException("assertion failed!");
                        outline[x].push(x, y + 1);
                    }
                } else {
                    if (prevRow[x + 1]) {
                        if (outline[x] == null) {
                            if (outline[x + 1] == null) {
                                outline[x] = outline[x + 1] = new Outline();
                                outline[x].push(x, y);
                                outline[x].push(x + 1, y);
                            } else {
                                outline[x] = outline[x + 1];
                                outline[x + 1] = null;
                                outline[x].shift(x, y);
                            }
                        } else if (outline[x + 1] == null) {
                            outline[x + 1] = outline[x];
                            outline[x] = null;
                            outline[x + 1].push(x + 1, y);
                        } else if (outline[x + 1] == outline[x]) {
                            polygons.add(outline[x].getPolygon());
                            outline[x] = outline[x + 1] = null;
                        } else {
                            outline[x].push(outline[x + 1]);
                            for (int x1 = 0; x1 <= w; x1++) if (x1 != x + 1 && outline[x1] == outline[x + 1]) {
                                outline[x1] = outline[x];
                                outline[x] = outline[x + 1] = null;
                                break;
                            }
                            if (outline[x] != null) throw new RuntimeException("assertion failed");
                        }
                    }
                    if (thisRow[x]) {
                        if (outline[x] == null) throw new RuntimeException("assertion failed");
                        outline[x].shift(x, y + 1);
                    }
                }
            }
            if ((y & progressInc) == 0) IJ.showProgress(y + 1, h + 1);
        }
        GeneralPath path = new GeneralPath(GeneralPath.WIND_EVEN_ODD);
        for (int i = 0; i < polygons.size(); i++) path.append((Polygon) polygons.get(i), false);
        ShapeRoi shape = new ShapeRoi(path);
        Roi roi = shape != null ? shape.shapeToRoi() : null;
        IJ.showProgress(1, 1);
        if (roi != null) return roi; else return shape;
    }
