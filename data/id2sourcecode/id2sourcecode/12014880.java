    public Shape initOutline(char c) {
        GeneralPath gp = new GeneralPath(GeneralPath.WIND_EVEN_ODD);
        if ((this.getWidth() == 0) || (this.getHeight() == 0)) {
            return gp;
        }
        Shape shape = null;
        long ptr;
        ptr = LinuxNativeFont.getGlyphOutline(this.pFont, c);
        if (ptr == 0) {
            return gp;
        }
        Xft.FT_Outline outline = xft.createFT_Outline(ptr);
        int n_contours = outline.get_n_contours();
        if (n_contours == 0) {
            LinuxNativeFont.freeGlyphOutline(ptr);
            return gp;
        }
        Xft.FT_Vector pPoints = outline.get_points();
        long pPointsPtr = pPoints.lock();
        pPoints.unlock();
        int size = outline.get_n_points();
        float points[] = LinuxNativeFont.getPointsFromFTVector(pPointsPtr, size);
        Int16Pointer pContours = outline.get_contours();
        Int8Pointer pTags = outline.get_tags();
        int index = 0;
        int tag;
        float x_start;
        float y_start;
        float x_finish;
        float y_finish;
        for (int i = 0; i < n_contours; i++) {
            short end = pContours.get(i);
            x_start = points[index * 2];
            y_start = points[index * 2 + 1];
            x_finish = points[end * 2];
            y_finish = points[end * 2 + 1];
            tag = pTags.get(index);
            if (tag == LinuxNativeFontWrapper.FT_CURVE_TAG_CONIC) {
                tag = pTags.get(end);
                if ((tag & LinuxNativeFontWrapper.FT_CURVE_TAG_ON) == 0) {
                    x_start = x_finish;
                    y_start = y_finish;
                    end--;
                } else {
                    x_start = (x_start + x_finish) / 2;
                    y_start = (y_start + y_finish) / 2;
                    x_finish = x_start;
                    y_finish = y_start;
                    index--;
                }
            }
            gp.moveTo(x_start, y_start);
            while (index < end) {
                index++;
                tag = pTags.get(index);
                switch((tag & 3)) {
                    case (LinuxNativeFontWrapper.FT_CURVE_TAG_ON):
                        float x = points[index * 2];
                        float y = points[index * 2 + 1];
                        gp.lineTo(x, y);
                        break;
                    case (LinuxNativeFontWrapper.FT_CURVE_TAG_CONIC):
                        float x1 = points[index * 2];
                        float y1 = points[index * 2 + 1];
                        float x2;
                        float y2;
                        while (index < end) {
                            index++;
                            tag = pTags.get(index);
                            x2 = points[index * 2];
                            y2 = points[index * 2 + 1];
                            if ((tag & LinuxNativeFontWrapper.FT_CURVE_TAG_ON) != 0) {
                                gp.quadTo(x1, y1, x2, y2);
                                break;
                            } else {
                                gp.quadTo(x1, y1, (x1 + x2) / 2, (y1 + y2) / 2);
                                x1 = x2;
                                y1 = y2;
                            }
                        }
                        if ((index == end) && ((tag & LinuxNativeFontWrapper.FT_CURVE_TAG_ON) == 0)) {
                            gp.quadTo(x1, y1, x_start, y_start);
                        }
                        break;
                    case (LinuxNativeFontWrapper.FT_CURVE_TAG_CUBIC):
                        x1 = points[index * 2];
                        y1 = points[index * 2 + 1];
                        index++;
                        x2 = points[index * 2];
                        y2 = points[index * 2 + 1];
                        if (index < end) {
                            index++;
                            float x3 = points[index * 2];
                            float y3 = points[index * 2 + 1];
                            gp.curveTo(x1, y1, x2, y2, x3, y3);
                        } else {
                            gp.curveTo(x1, y1, x2, y2, x_start, y_start);
                        }
                        break;
                    default:
                        LinuxNativeFont.freeGlyphOutline(ptr);
                        return new GeneralPath(GeneralPath.WIND_EVEN_ODD);
                }
            }
            gp.lineTo(x_start, y_start);
            index++;
        }
        shape = gp;
        LinuxNativeFont.freeGlyphOutline(ptr);
        return shape;
    }
