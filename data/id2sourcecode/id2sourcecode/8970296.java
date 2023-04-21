    public void readAll() throws IOException, DocumentException {
        if (in.readInt() != 0x9AC6CDD7) {
            throw new DocumentException("Not a placeable windows metafile");
        }
        in.readWord();
        left = in.readShort();
        top = in.readShort();
        right = in.readShort();
        bottom = in.readShort();
        inch = in.readWord();
        state.setScalingX((float) (right - left) / (float) inch * 72f);
        state.setScalingY((float) (bottom - top) / (float) inch * 72f);
        state.setOffsetWx(left);
        state.setOffsetWy(top);
        state.setExtentWx(right - left);
        state.setExtentWy(bottom - top);
        in.readInt();
        in.readWord();
        in.skip(18);
        int tsize;
        int function;
        cb.setLineCap(1);
        cb.setLineJoin(1);
        for (; ; ) {
            int lenMarker = in.getLength();
            tsize = in.readInt();
            if (tsize < 3) break;
            function = in.readWord();
            switch(function) {
                case 0:
                    break;
                case META_CREATEPALETTE:
                case META_CREATEREGION:
                case META_DIBCREATEPATTERNBRUSH:
                    state.addMetaObject(new MetaObject());
                    break;
                case META_CREATEPENINDIRECT:
                    {
                        MetaPen pen = new MetaPen();
                        pen.init(in);
                        state.addMetaObject(pen);
                        break;
                    }
                case META_CREATEBRUSHINDIRECT:
                    {
                        MetaBrush brush = new MetaBrush();
                        brush.init(in);
                        state.addMetaObject(brush);
                        break;
                    }
                case META_CREATEFONTINDIRECT:
                    {
                        MetaFont font = new MetaFont();
                        font.init(in);
                        state.addMetaObject(font);
                        break;
                    }
                case META_SELECTOBJECT:
                    {
                        int idx = in.readWord();
                        state.selectMetaObject(idx, cb);
                        break;
                    }
                case META_DELETEOBJECT:
                    {
                        int idx = in.readWord();
                        state.deleteMetaObject(idx);
                        break;
                    }
                case META_SAVEDC:
                    state.saveState(cb);
                    break;
                case META_RESTOREDC:
                    {
                        int idx = in.readShort();
                        state.restoreState(idx, cb);
                        break;
                    }
                case META_SETWINDOWORG:
                    state.setOffsetWy(in.readShort());
                    state.setOffsetWx(in.readShort());
                    break;
                case META_SETWINDOWEXT:
                    state.setExtentWy(in.readShort());
                    state.setExtentWx(in.readShort());
                    break;
                case META_MOVETO:
                    {
                        int y = in.readShort();
                        Point p = new Point(in.readShort(), y);
                        state.setCurrentPoint(p);
                        break;
                    }
                case META_LINETO:
                    {
                        int y = in.readShort();
                        int x = in.readShort();
                        Point p = state.getCurrentPoint();
                        cb.moveTo(state.transformX(p.x), state.transformY(p.y));
                        cb.lineTo(state.transformX(x), state.transformY(y));
                        cb.stroke();
                        state.setCurrentPoint(new Point(x, y));
                        break;
                    }
                case META_POLYLINE:
                    {
                        state.setLineJoinPolygon(cb);
                        int len = in.readWord();
                        int x = in.readShort();
                        int y = in.readShort();
                        cb.moveTo(state.transformX(x), state.transformY(y));
                        for (int k = 1; k < len; ++k) {
                            x = in.readShort();
                            y = in.readShort();
                            cb.lineTo(state.transformX(x), state.transformY(y));
                        }
                        cb.stroke();
                        break;
                    }
                case META_POLYGON:
                    {
                        if (isNullStrokeFill(false)) break;
                        int len = in.readWord();
                        int sx = in.readShort();
                        int sy = in.readShort();
                        cb.moveTo(state.transformX(sx), state.transformY(sy));
                        for (int k = 1; k < len; ++k) {
                            int x = in.readShort();
                            int y = in.readShort();
                            cb.lineTo(state.transformX(x), state.transformY(y));
                        }
                        cb.lineTo(state.transformX(sx), state.transformY(sy));
                        strokeAndFill();
                        break;
                    }
                case META_POLYPOLYGON:
                    {
                        if (isNullStrokeFill(false)) break;
                        int numPoly = in.readWord();
                        int lens[] = new int[numPoly];
                        for (int k = 0; k < lens.length; ++k) lens[k] = in.readWord();
                        for (int j = 0; j < lens.length; ++j) {
                            int len = lens[j];
                            int sx = in.readShort();
                            int sy = in.readShort();
                            cb.moveTo(state.transformX(sx), state.transformY(sy));
                            for (int k = 1; k < len; ++k) {
                                int x = in.readShort();
                                int y = in.readShort();
                                cb.lineTo(state.transformX(x), state.transformY(y));
                            }
                            cb.lineTo(state.transformX(sx), state.transformY(sy));
                        }
                        strokeAndFill();
                        break;
                    }
                case META_ELLIPSE:
                    {
                        if (isNullStrokeFill(state.getLineNeutral())) break;
                        int b = in.readShort();
                        int r = in.readShort();
                        int t = in.readShort();
                        int l = in.readShort();
                        cb.arc(state.transformX(l), state.transformY(b), state.transformX(r), state.transformY(t), 0, 360);
                        strokeAndFill();
                        break;
                    }
                case META_ARC:
                    {
                        if (isNullStrokeFill(state.getLineNeutral())) break;
                        float yend = state.transformY(in.readShort());
                        float xend = state.transformX(in.readShort());
                        float ystart = state.transformY(in.readShort());
                        float xstart = state.transformX(in.readShort());
                        float b = state.transformY(in.readShort());
                        float r = state.transformX(in.readShort());
                        float t = state.transformY(in.readShort());
                        float l = state.transformX(in.readShort());
                        float cx = (r + l) / 2;
                        float cy = (t + b) / 2;
                        float arc1 = getArc(cx, cy, xstart, ystart);
                        float arc2 = getArc(cx, cy, xend, yend);
                        arc2 -= arc1;
                        if (arc2 <= 0) arc2 += 360;
                        cb.arc(l, b, r, t, arc1, arc2);
                        cb.stroke();
                        break;
                    }
                case META_PIE:
                    {
                        if (isNullStrokeFill(state.getLineNeutral())) break;
                        float yend = state.transformY(in.readShort());
                        float xend = state.transformX(in.readShort());
                        float ystart = state.transformY(in.readShort());
                        float xstart = state.transformX(in.readShort());
                        float b = state.transformY(in.readShort());
                        float r = state.transformX(in.readShort());
                        float t = state.transformY(in.readShort());
                        float l = state.transformX(in.readShort());
                        float cx = (r + l) / 2;
                        float cy = (t + b) / 2;
                        float arc1 = getArc(cx, cy, xstart, ystart);
                        float arc2 = getArc(cx, cy, xend, yend);
                        arc2 -= arc1;
                        if (arc2 <= 0) arc2 += 360;
                        ArrayList ar = PdfContentByte.bezierArc(l, b, r, t, arc1, arc2);
                        if (ar.size() == 0) break;
                        float pt[] = (float[]) ar.get(0);
                        cb.moveTo(cx, cy);
                        cb.lineTo(pt[0], pt[1]);
                        for (int k = 0; k < ar.size(); ++k) {
                            pt = (float[]) ar.get(k);
                            cb.curveTo(pt[2], pt[3], pt[4], pt[5], pt[6], pt[7]);
                        }
                        cb.lineTo(cx, cy);
                        strokeAndFill();
                        break;
                    }
                case META_CHORD:
                    {
                        if (isNullStrokeFill(state.getLineNeutral())) break;
                        float yend = state.transformY(in.readShort());
                        float xend = state.transformX(in.readShort());
                        float ystart = state.transformY(in.readShort());
                        float xstart = state.transformX(in.readShort());
                        float b = state.transformY(in.readShort());
                        float r = state.transformX(in.readShort());
                        float t = state.transformY(in.readShort());
                        float l = state.transformX(in.readShort());
                        float cx = (r + l) / 2;
                        float cy = (t + b) / 2;
                        float arc1 = getArc(cx, cy, xstart, ystart);
                        float arc2 = getArc(cx, cy, xend, yend);
                        arc2 -= arc1;
                        if (arc2 <= 0) arc2 += 360;
                        ArrayList ar = PdfContentByte.bezierArc(l, b, r, t, arc1, arc2);
                        if (ar.size() == 0) break;
                        float pt[] = (float[]) ar.get(0);
                        cx = pt[0];
                        cy = pt[1];
                        cb.moveTo(cx, cy);
                        for (int k = 0; k < ar.size(); ++k) {
                            pt = (float[]) ar.get(k);
                            cb.curveTo(pt[2], pt[3], pt[4], pt[5], pt[6], pt[7]);
                        }
                        cb.lineTo(cx, cy);
                        strokeAndFill();
                        break;
                    }
                case META_RECTANGLE:
                    {
                        if (isNullStrokeFill(true)) break;
                        float b = state.transformY(in.readShort());
                        float r = state.transformX(in.readShort());
                        float t = state.transformY(in.readShort());
                        float l = state.transformX(in.readShort());
                        cb.rectangle(l, b, r - l, t - b);
                        strokeAndFill();
                        break;
                    }
                case META_ROUNDRECT:
                    {
                        if (isNullStrokeFill(true)) break;
                        float h = state.transformY(0) - state.transformY(in.readShort());
                        float w = state.transformX(in.readShort()) - state.transformX(0);
                        float b = state.transformY(in.readShort());
                        float r = state.transformX(in.readShort());
                        float t = state.transformY(in.readShort());
                        float l = state.transformX(in.readShort());
                        cb.roundRectangle(l, b, r - l, t - b, (h + w) / 4);
                        strokeAndFill();
                        break;
                    }
                case META_INTERSECTCLIPRECT:
                    {
                        float b = state.transformY(in.readShort());
                        float r = state.transformX(in.readShort());
                        float t = state.transformY(in.readShort());
                        float l = state.transformX(in.readShort());
                        cb.rectangle(l, b, r - l, t - b);
                        cb.eoClip();
                        cb.newPath();
                        break;
                    }
                case META_EXTTEXTOUT:
                    {
                        int y = in.readShort();
                        int x = in.readShort();
                        int count = in.readWord();
                        int flag = in.readWord();
                        int x1 = 0;
                        int y1 = 0;
                        int x2 = 0;
                        int y2 = 0;
                        if ((flag & (MetaFont.ETO_CLIPPED | MetaFont.ETO_OPAQUE)) != 0) {
                            x1 = in.readShort();
                            y1 = in.readShort();
                            x2 = in.readShort();
                            y2 = in.readShort();
                        }
                        byte text[] = new byte[count];
                        int k;
                        for (k = 0; k < count; ++k) {
                            byte c = (byte) in.readByte();
                            if (c == 0) break;
                            text[k] = c;
                        }
                        String s;
                        try {
                            s = new String(text, 0, k, "Cp1252");
                        } catch (UnsupportedEncodingException e) {
                            s = new String(text, 0, k);
                        }
                        outputText(x, y, flag, x1, y1, x2, y2, s);
                        break;
                    }
                case META_TEXTOUT:
                    {
                        int count = in.readWord();
                        byte text[] = new byte[count];
                        int k;
                        for (k = 0; k < count; ++k) {
                            byte c = (byte) in.readByte();
                            if (c == 0) break;
                            text[k] = c;
                        }
                        String s;
                        try {
                            s = new String(text, 0, k, "Cp1252");
                        } catch (UnsupportedEncodingException e) {
                            s = new String(text, 0, k);
                        }
                        count = (count + 1) & 0xfffe;
                        in.skip(count - k);
                        int y = in.readShort();
                        int x = in.readShort();
                        outputText(x, y, 0, 0, 0, 0, 0, s);
                        break;
                    }
                case META_SETBKCOLOR:
                    state.setCurrentBackgroundColor(in.readColor());
                    break;
                case META_SETTEXTCOLOR:
                    state.setCurrentTextColor(in.readColor());
                    break;
                case META_SETTEXTALIGN:
                    state.setTextAlign(in.readWord());
                    break;
                case META_SETBKMODE:
                    state.setBackgroundMode(in.readWord());
                    break;
                case META_SETPOLYFILLMODE:
                    state.setPolyFillMode(in.readWord());
                    break;
                case META_SETPIXEL:
                    {
                        Color color = in.readColor();
                        int y = in.readShort();
                        int x = in.readShort();
                        cb.saveState();
                        cb.setColorFill(color);
                        cb.rectangle(state.transformX(x), state.transformY(y), .2f, .2f);
                        cb.fill();
                        cb.restoreState();
                        break;
                    }
                case META_DIBSTRETCHBLT:
                case META_STRETCHDIB:
                    {
                        int rop = in.readInt();
                        if (function == META_STRETCHDIB) {
                            in.readWord();
                        }
                        int srcHeight = in.readShort();
                        int srcWidth = in.readShort();
                        int ySrc = in.readShort();
                        int xSrc = in.readShort();
                        float destHeight = state.transformY(in.readShort()) - state.transformY(0);
                        float destWidth = state.transformX(in.readShort()) - state.transformX(0);
                        float yDest = state.transformY(in.readShort());
                        float xDest = state.transformX(in.readShort());
                        byte b[] = new byte[(tsize * 2) - (in.getLength() - lenMarker)];
                        for (int k = 0; k < b.length; ++k) b[k] = (byte) in.readByte();
                        try {
                            ByteArrayInputStream inb = new ByteArrayInputStream(b);
                            Image bmp = BmpImage.getImage(inb, true, b.length);
                            cb.saveState();
                            cb.rectangle(xDest, yDest, destWidth, destHeight);
                            cb.clip();
                            cb.newPath();
                            bmp.scaleAbsolute(destWidth * bmp.width() / srcWidth, -destHeight * bmp.height() / srcHeight);
                            bmp.setAbsolutePosition(xDest - destWidth * xSrc / srcWidth, yDest + destHeight * ySrc / srcHeight - bmp.scaledHeight());
                            cb.addImage(bmp);
                            cb.restoreState();
                        } catch (Exception e) {
                        }
                        break;
                    }
            }
            in.skip((tsize * 2) - (in.getLength() - lenMarker));
        }
    }
