    private boolean readRecord(InputStream in) {
        int i, j, rdSize, rdFunc;
        int a, b, c, d, e, f, k, l, m, n;
        Color crco;
        Font fo;
        Image im;
        WmfDecDC DC = (WmfDecDC) DCstack.peek();
        Graphics g = DC.gr;
        boolean error;
        int xpoints[], ypoints[];
        byte text[];
        String s;
        Object ob;
        Graphics g2;
        try {
            rdSize = readInt32(in);
            rdFunc = readInt16(in);
            for (i = 0; i < rdSize - 3; i++) {
                params[i] = readInt16(in);
            }
        } catch (IOException ex) {
            return false;
        }
        if (debug) {
            System.out.println("RFunc: " + Integer.toString(rdFunc, 16));
        }
        switch(rdFunc) {
            case META_LINETO:
                if (debug) {
                    System.out.println("MetaLineTo");
                }
                g.setColor(DC.aktpen.getColor());
                a = DC.ytransfer(params[0]);
                b = DC.xtransfer(params[1]);
                g.drawLine(DC.aktXpos, DC.aktYpos, b, a);
                DC.aktXpos = b;
                DC.aktYpos = a;
                break;
            case META_MOVETO:
                if (debug) {
                    System.out.println("MetaMoveTo");
                }
                DC.aktYpos = DC.ytransfer(params[0]);
                DC.aktXpos = DC.xtransfer(params[1]);
                break;
            case META_ROUNDRECT:
                if (debug) {
                    System.out.println("MetaRoundRect");
                }
                e = transform(params[0], minsize);
                f = transform(params[1], minsize);
                a = DC.ytransfer(params[2]);
                b = DC.xtransfer(params[3]);
                c = DC.ytransfer(params[4]);
                d = DC.xtransfer(params[5]);
                if (a < c && b < d) {
                    i = a;
                    a = c;
                    c = i;
                    i = b;
                    b = d;
                    d = i;
                }
                g.setColor(DC.aktbrush.getColor());
                g.fillRoundRect(d, c, b - d - 1, a - c - 1, f, e);
                g.setColor(DC.aktpen.getColor());
                g.drawRoundRect(d, c, b - d - 1, a - c - 1, f, e);
                break;
            case META_RECTANGLE:
                if (debug) {
                    System.out.println("MetaRectangle");
                }
                a = DC.ytransfer(params[0]);
                b = DC.xtransfer(params[1]);
                c = DC.ytransfer(params[2]);
                d = DC.xtransfer(params[3]);
                if (a < c && b < d) {
                    i = a;
                    a = c;
                    c = i;
                    i = b;
                    b = d;
                    d = i;
                }
                if (DC.aktbrush.getImage() != null) {
                    drawOpaqePattern(g, DC.aktbrush.getImage(), d, c, b, a, fr);
                } else {
                    g.setColor(DC.aktbrush.getColor());
                    g.fillRect(d, c, b - d - 1, a - c - 1);
                }
                g.setColor(DC.aktpen.getColor());
                g.drawRect(d, c, b - d - 1, a - c - 1);
                break;
            case META_SETPIXEL:
                if (debug) {
                    System.out.println("MetaSetpixel");
                }
                crco = new Color(getLoByteVal(params[0]), getHiByteVal(params[0]), getLoByteVal(params[1]));
                g.setColor(crco);
                crco = null;
                a = DC.xtransfer(params[3]);
                b = DC.ytransfer(params[2]);
                g.drawLine(a, b, a, b);
                break;
            case META_POLYLINE:
            case META_POLYGON:
                if (debug) {
                    System.out.println(((rdFunc == META_POLYGON) ? "MetaPolygon: " : "MetaPolyLine: ") + params[0]);
                }
                xpoints = new int[params[0]];
                ypoints = new int[params[0]];
                for (i = 0; i < params[0]; i++) {
                    xpoints[i] = DC.xtransfer(params[i * 2 + 1]);
                    ypoints[i] = DC.ytransfer(params[i * 2 + 2]);
                    if (debug) {
                        System.out.println(Integer.toString(xpoints[i], 16) + " " + Integer.toString(ypoints[i], 16));
                    }
                }
                if (rdFunc == META_POLYGON) {
                    g.setColor(DC.aktbrush.getColor());
                    g.fillPolygon((int[]) xpoints, (int[]) ypoints, params[0]);
                    g.setColor(DC.aktpen.getColor());
                    g.drawPolygon((int[]) xpoints, (int[]) ypoints, params[0]);
                } else {
                    g.setColor(DC.aktpen.getColor());
                    g.drawPolyline((int[]) xpoints, (int[]) ypoints, params[0]);
                }
                xpoints = null;
                ypoints = null;
                break;
            case META_POLYPOLYGON:
                if (debug) {
                    System.out.println("MetaPolyPolygon: " + params[0]);
                }
                for (i = 0; i < params[0]; i++) {
                    xpoints = new int[params[i + 1]];
                    ypoints = new int[params[i + 1]];
                    if (debug) {
                        System.out.println("Polygon #" + i + " Pts=" + params[i + 1]);
                    }
                    b = params[0] + 1;
                    for (c = 0; c < i; c++) {
                        b += params[c + 1] * 2;
                    }
                    for (a = 0; a < params[i + 1]; a++) {
                        xpoints[a] = DC.xtransfer(params[b + a * 2]);
                        ypoints[a] = DC.ytransfer(params[b + a * 2 + 1]);
                        if (debug) {
                            System.out.println(Integer.toString(xpoints[a], 16) + " " + Integer.toString(ypoints[a], 16));
                        }
                    }
                    g.setColor(DC.aktbrush.getColor());
                    g.fillPolygon((int[]) xpoints, (int[]) ypoints, params[i + 1]);
                    g.drawPolygon((int[]) xpoints, (int[]) ypoints, params[i + 1]);
                }
                break;
            case META_ELLIPSE:
                if (debug) {
                    System.out.println("MetaEllipse");
                }
                a = DC.ytransfer(params[0]);
                b = DC.xtransfer(params[1]);
                c = DC.ytransfer(params[2]);
                d = DC.xtransfer(params[3]);
                g.setColor(DC.aktpen.getColor());
                g.drawOval(d, c, b - d, a - c);
                g.setColor(DC.aktbrush.getColor());
                g.fillOval(d, c, b - d, a - c);
                break;
            case META_ARC:
            case META_PIE:
            case META_CHORD:
                if (debug) {
                    switch(rdFunc) {
                        case META_ARC:
                            System.out.println("MetaArc");
                            break;
                        case META_PIE:
                            System.out.println("MetaPie");
                            break;
                        case META_CHORD:
                            System.out.println("MetaChord");
                            break;
                    }
                }
                a = DC.ytransfer(params[0]);
                b = DC.xtransfer(params[1]);
                c = DC.ytransfer(params[2]);
                d = DC.xtransfer(params[3]);
                e = DC.ytransfer(params[4]);
                f = DC.xtransfer(params[5]);
                k = DC.ytransfer(params[6]);
                l = DC.xtransfer(params[7]);
                g.setColor(DC.aktpen.getColor());
                int xm = l + (f - l) / 2;
                int ym = k + (e - k) / 2;
                if (rdFunc == META_PIE) {
                    g.drawLine(d, c, xm, ym);
                    g.drawLine(b, a, xm, ym);
                }
                if (rdFunc == META_CHORD) {
                    g.drawLine(d, c, b, a);
                }
                int beg = arcus(d - xm, c - ym);
                int arc = arcus(b - xm, a - ym) - beg;
                if (arc < 0) {
                    arc += 360;
                }
                if (debug) {
                    System.out.println("Beg=" + beg + " Arc=" + arc);
                }
                g.drawArc(l, k, f - l, e - k, beg, arc);
                break;
            case META_DELETEOBJECT:
                if (debug) {
                    System.out.println("MetaDeleteObject:" + params[0]);
                }
                gdiObj[params[0]] = null;
                break;
            case META_SELECTPALETTE:
                if (debug) {
                    System.out.println("MetaSelectPalette:" + params[0] + " = " + gdiObj[params[0]]);
                }
                if (gdiObj[params[0]].getMagic() == WmfDecObj.M_PALETTE) {
                    DC.aktpal = gdiObj[params[0]];
                } else {
                    System.out.println(" ---- internal ERROR in MetaSelectPalette -----");
                }
                break;
            case META_SELECTCLIPREGION:
                if (debug) {
                    System.out.println("MetaSelectClipRegion:" + params[0] + " = " + gdiObj[params[0]]);
                }
                if (gdiObj[params[0]].getMagic() == WmfDecObj.M_CLIP) {
                    DC.aktclip = gdiObj[params[0]];
                    g.clipRect(DC.aktclip.getRect().x, DC.aktclip.getRect().y, DC.aktclip.getRect().width, DC.aktclip.getRect().height);
                } else {
                    System.out.println(" ---- internal ERROR in MetaSelectClipregion -----");
                }
                break;
            case META_SELECTOBJECT:
                if (debug) {
                    System.out.println("MetaSelectObject:" + params[0] + " = " + gdiObj[params[0]]);
                }
                switch(gdiObj[params[0]].getMagic()) {
                    case WmfDecObj.M_PEN:
                        DC.aktpen = gdiObj[params[0]];
                        break;
                    case WmfDecObj.M_FONT:
                        DC.aktfont = gdiObj[params[0]];
                        break;
                    case WmfDecObj.M_BRUSH:
                        DC.aktbrush = gdiObj[params[0]];
                        break;
                    case WmfDecObj.M_PALETTE:
                        DC.aktpal = gdiObj[params[0]];
                        break;
                    case WmfDecObj.M_BITMAP:
                        DC.aktbmp = gdiObj[params[0]];
                        break;
                    case WmfDecObj.M_CLIP:
                        DC.aktclip = gdiObj[params[0]];
                        if (debug) {
                            System.out.println("Select clipping rect");
                            g.drawRect(DC.aktclip.getRect().x, DC.aktclip.getRect().y, DC.aktclip.getRect().width, DC.aktclip.getRect().height);
                        }
                        g.clipRect(DC.aktclip.getRect().x, DC.aktclip.getRect().y, DC.aktclip.getRect().width, DC.aktclip.getRect().height);
                        break;
                }
                break;
            case META_CREATEPENINDIRECT:
                if (debug) {
                    System.out.println("MetaCreatePenIndirect");
                }
                error = false;
                switch(params[0]) {
                    case PS_NULL:
                        crco = null;
                        System.out.println("MetaCreatePenIndirect: PS_NULL");
                        break;
                    case PS_DASH:
                    case PS_DOT:
                    case PS_DASHDOT:
                    case PS_DASHDOTDOT:
                        System.out.println("MetaCreatePenIndirect: line attribute " + params[0] + " ignored");
                    case PS_INSIDEFRAME:
                    case PS_SOLID:
                        crco = new Color(getLoByteVal(params[3]), getHiByteVal(params[3]), getLoByteVal(params[4]));
                        break;
                    default:
                        crco = Color.black;
                        error = true;
                        break;
                }
                if (!error) {
                    add_handle(new WmfDecObj(PS_SOLID, crco));
                    if (debug) {
                        System.out.println(crco);
                    }
                    crco = null;
                    a = params[1];
                    b = params[2];
                }
                if (debug || error) {
                    for (i = 0; i < rdSize - 3; i++) {
                        if (i < 16) {
                            System.out.print(Integer.toString(params[i], 16) + " ");
                        }
                    }
                    System.out.println();
                }
                break;
            case META_CREATEBRUSHINDIRECT:
                if (debug) {
                    System.out.println("MetaCreateBrushIndirect: Style_00_Object=" + params[0]);
                    showparams(params, rdSize, rdFunc);
                }
                switch(params[0]) {
                    case 1:
                        crco = DC.aktbackgnd;
                        add_handle(new WmfDecObj(crco, WmfDecObj.M_BRUSH));
                        if (debug) {
                            System.out.println(crco);
                        }
                        break;
                    case 0:
                        crco = new Color(getLoByteVal(params[1]), getHiByteVal(params[1]), getLoByteVal(params[2]));
                        add_handle(new WmfDecObj(crco, WmfDecObj.M_BRUSH));
                        if (debug) {
                            System.out.println(crco);
                        }
                        crco = null;
                        break;
                    case 2:
                        crco = new Color(getLoByteVal(params[1]), getHiByteVal(params[1]), getLoByteVal(params[2]));
                        add_handle(new WmfDecObj((int) params[3], crco, DC.aktbackgnd, fr));
                        if (debug) {
                            System.out.println(crco);
                        }
                        crco = null;
                        break;
                    case 3:
                    case 4:
                    case 5:
                        crco = Color.gray;
                        add_handle(new WmfDecObj(crco, WmfDecObj.M_BRUSH));
                        System.out.println("pattern substitution used.");
                        break;
                    default:
                        System.out.println("(bad parameter!)");
                }
                break;
            case META_CREATEREGION:
                if (debug) {
                    System.out.println("MetaCreateRegion");
                    System.out.println("params[5] sub records=" + params[5]);
                    for (i = 0; i < rdSize - 3; i++) {
                        System.out.print(Integer.toString(params[i], 10) + " ");
                    }
                    System.out.println();
                }
                add_handle(new WmfDecObj(DC.xtransfer(params[7]), DC.ytransfer(params[8]), DC.xtransfer(params[9]), DC.xtransfer(params[10])));
                break;
            case META_INTERSECTCLIPRECT:
                System.out.println("MetaIntersectClipRect is experimental");
                n = DC.ytransfer(params[0]);
                m = DC.xtransfer(params[1]);
                l = DC.ytransfer(params[2]);
                k = DC.xtransfer(params[3]);
                g.clipRect(k, l, m - k, n - l);
                break;
            case META_CREATEFONTINDIRECT:
                text = new byte[80];
                for (j = i = 0; i < rdSize - 3 - 9; i++) {
                    if ((text[2 * i] = (byte) getLoByteVal(params[i + 9])) == 0) {
                        break;
                    } else {
                        j++;
                    }
                    if ((text[2 * i + 1] = (byte) getHiByteVal(params[i + 9])) == 0) {
                        break;
                    } else {
                        j++;
                    }
                }
                s = new String(text, 0, 0, j);
                if (debug) {
                    System.out.println("MetaCreateFontIndirect: " + params[0] + " " + params[1] + " " + s);
                }
                if (s.startsWith("Times")) {
                    s = "TimesRoman";
                } else if (s.startsWith("Arial")) {
                    s = "Helvetica";
                } else if (s.startsWith("Courier")) {
                    s = "Courier";
                } else if (s.startsWith("MS")) {
                    s = "Dialog";
                } else if (s.startsWith("WingDings")) {
                    s = "ZapfDingbats";
                }
                b = params[1];
                c = params[2];
                d = params[3];
                e = params[4];
                f = params[5];
                k = params[6];
                l = params[7];
                i = params[8];
                a = transform(params[0], minsize);
                fo = new Font(s, (e > 500 ? Font.BOLD : Font.PLAIN) + (getLoByteVal(f) > 0 ? Font.ITALIC : 0), a);
                if (debug) {
                    System.out.println(fo);
                }
                add_handle(new WmfDecObj(fo, getHiByteVal(f), d));
                fo = null;
                text = null;
                break;
            case META_CREATEPALETTE:
                if (debug) {
                    System.out.println("MetaCreatePalette");
                }
                crco = Color.black;
                add_handle(new WmfDecObj(crco, WmfDecObj.M_PALETTE));
                break;
            case META_REALIZEPALETTE:
                if (debug) {
                    showparams(params, rdSize, rdFunc);
                }
                System.out.println("MetaRealizePalette");
                break;
            case META_SETROP2:
                if (debug) {
                    System.out.println("MetaSetRop2: ROP code=" + Integer.toString((i = params[0]), 16));
                }
                break;
            case META_SETPOLYFILLMODE:
                if (debug) {
                    System.out.println("MetaSetPolyFillmode:" + params[0]);
                }
                break;
            case META_SETSTRETCHBLTMODE:
                if (debug) {
                    System.out.println("MetaSetStretchBltMode:" + params[0]);
                }
                break;
            case META_INVERTREGION:
                if (debug) {
                    showparams(params, rdSize, rdFunc);
                }
                System.out.println("MetaInvertRegion:" + params[0]);
                break;
            case META_SETWINDOWEXT:
                DC.winextY = params[0];
                DC.winextX = params[1];
                if (debug) {
                    System.out.println("MetaSetWindowExt:  X:" + DC.winextX + "  Y:" + DC.winextY);
                }
                break;
            case META_SETWINDOWORG:
                DC.winorgY = params[0];
                DC.winorgX = params[1];
                if (debug) {
                    System.out.println("MetaSetWindowOrg:  X:" + DC.winorgX + "  Y:" + DC.winorgY);
                }
                break;
            case META_SETTEXTCOLOR:
                DC.akttextc = new Color(getLoByteVal(params[0]), getHiByteVal(params[0]), getLoByteVal(params[1]));
                if (debug) {
                    System.out.println("MetaSetTextColor: " + DC.akttextc);
                }
                break;
            case META_EXTTEXTOUT:
            case META_TEXTOUT:
                if (rdFunc == META_EXTTEXTOUT) {
                    a = params[2];
                    b = DC.ytransfer(params[0]);
                    c = DC.xtransfer(params[1]);
                    d = params[3];
                    if (debug) {
                        System.out.println("ExtTextOut:option =" + Integer.toString(d, 16));
                    }
                    k = DC.xtransfer(params[4]);
                    l = DC.ytransfer(params[5]);
                    m = DC.xtransfer(params[6]);
                    n = DC.ytransfer(params[7]);
                    if (debug) {
                        System.out.println("TextAlign=" + DC.akttextalign);
                        System.out.println("x  =" + c + "\ty  =" + b);
                        System.out.println("rx =" + k + "\try =" + l);
                        System.out.println("rw =" + (m - k) + "\trh =" + (n - l));
                    }
                    e = d == 0 ? 3 : 7;
                } else {
                    a = params[0];
                    b = DC.ytransfer(params[(a + 1) / 2 + 1]);
                    c = DC.xtransfer(params[(a + 1) / 2 + 2]);
                    d = e = 0;
                    k = l = m = n = 0;
                }
                if ((d & ETO_OPAQUE) != 0) {
                    g.setColor(DC.aktbackgnd);
                    g.fillRect(k, l, m - k - 1, n - l - 1);
                    if (debug) {
                        System.out.println("ExtTextOut: using OPAQUE style");
                    }
                }
                if ((d & ETO_GRAYED) != 0) {
                    g.setColor(Color.lightGray);
                } else {
                    g.setColor(DC.akttextc);
                }
                if ((d & ETO_CLIPPED) != 0) {
                    g2 = g.create();
                    g2.clipRect(k, l, m - k - 1, n - l - 1);
                    g = g2;
                    if (debug) {
                        System.out.println("ExtTextOut: using clipping rect");
                    }
                } else {
                    g2 = null;
                }
                g.setFont(DC.aktfont.getFont());
                FontMetrics fm = g.getFontMetrics();
                text = new byte[a];
                for (i = 0; i < a; i++) {
                    if (i % 2 == 0) {
                        text[i] = (byte) getLoByteVal(params[e + i / 2 + 1]);
                    } else {
                        text[i] = (byte) getHiByteVal(params[e + i / 2 + 1]);
                    }
                }
                s = new String(text, 0);
                if (DC.aktfont.getFontOrientation() != 0) {
                    System.out.println("non horizontal text is not supported: " + s);
                } else {
                    if (DC.akttextalign == TA_TOP) {
                        b += DC.aktfont.getFont().getSize();
                    }
                    g.drawString(s, c, b);
                    if (DC.aktfont.isUnderlined()) {
                        g.drawLine(c, b + 2, c + fm.stringWidth(s), b + 2);
                    }
                }
                if (debug) {
                    System.out.println((rdFunc == META_EXTTEXTOUT ? "MetaExtTextOut: " : "MetaTextOut: ") + (new String(text, 0)) + " (len=" + a + ") x=" + c + " y=" + b);
                }
                text = null;
                if (g2 != null) {
                    g2.dispose();
                }
                break;
            case META_SETMAPMODE:
                if (debug) {
                    showparams(params, rdSize, rdFunc);
                }
                System.out.println("MetaSetMapMode: " + params[0] + " (ignored)");
                break;
            case META_SETBKCOLOR:
                if (debug) {
                    System.out.println("MetaSetBkColor");
                }
                DC.aktbackgnd = new Color(getLoByteVal(params[0]), getHiByteVal(params[0]), getLoByteVal(params[1]));
                break;
            case META_SETTEXTJUSTIFICATION:
                if (debug) {
                    showparams(params, rdSize, rdFunc);
                }
                if (debug || params[0] != 0 || params[1] != 0) {
                    System.out.println("MetaSetTextJustification: " + params[0] + " " + params[1]);
                }
                break;
            case META_SETBKMODE:
                if (debug) {
                    System.out.println("MetaSetBkMode:" + (params[0] == 1 ? "TRANSPARENT" : "OPAQUE"));
                }
                DC.aktbkmode = params[0];
                break;
            case META_SETTEXTALIGN:
                if (debug) {
                    System.out.println("MetaSetTextalign: " + params[0]);
                }
                DC.akttextalign = params[0];
                break;
            case META_SAVEDC:
                if (debug) {
                    System.out.println("MetaSaveDC");
                }
                try {
                    DC = (WmfDecDC) DCstack.push(DC.clone());
                    DC.slevel++;
                    DC.gr = g.create();
                } catch (Exception ex) {
                    System.out.println(" ---- internal ERROR in MetaSaveDC -----");
                }
                break;
            case META_RESTOREDC:
                if (debug) {
                    System.out.println("MetaRestoreDC" + params[0]);
                }
                switch(params[0]) {
                    case -1:
                        g.dispose();
                        DCstack.pop();
                        DC = (WmfDecDC) DCstack.peek();
                        break;
                    default:
                        while (DC.slevel > params[0] && !DCstack.empty()) {
                            g.dispose();
                            DC = (WmfDecDC) DCstack.pop();
                            g = DC.gr;
                        }
                        break;
                }
                break;
            case META_PATBLT:
                e = (params[1] << 16) + params[0];
                if (debug) {
                    System.out.println("MetaPatBlt: ROP code=" + Integer.toString(e, 16));
                    System.out.println(DC.aktbrush.getImage());
                }
                a = DC.ytransfer(params[2]);
                b = DC.xtransfer(params[3]);
                c = DC.ytransfer(params[4]);
                d = DC.xtransfer(params[5]);
                switch(e) {
                    case WHITENESS:
                        g.setColor(Color.white);
                        g.fillRect(d, c, b, a);
                        break;
                    case BLACKNESS:
                        g.setColor(Color.black);
                        g.fillRect(d, c, b, a);
                        break;
                    case PATCOPY:
                        if ((im = DC.aktbrush.getImage()) != null) {
                            drawOpaqePattern(g, im, d, c, d + b, c + a, fr);
                        } else {
                            g.setColor(DC.aktbrush.getColor());
                            g.fillRect(d, c, b, a);
                        }
                        break;
                    case PATINVERT:
                    case DSTINVERT:
                    default:
                        System.out.println("unsupported ROP code:" + Integer.toString(e, 16));
                }
                break;
            case META_STRETCHBLT:
                if (debug) {
                    System.out.println("MetaStretchBlt:" + rdSize);
                }
                e = (params[1] << 16) + params[0];
                a = DC.ytransfer(params[6]);
                b = DC.xtransfer(params[7]);
                c = DC.ytransfer(params[8]);
                d = DC.xtransfer(params[9]);
                switch(e) {
                    case WHITENESS:
                        g.setColor(Color.white);
                        g.fillRect(d, c, b, a);
                        break;
                    case BLACKNESS:
                        g.setColor(Color.black);
                        g.fillRect(d, c, b, a);
                        break;
                    case SRCCOPY:
                        im = OldBitmapImage(10, params, fr);
                        if (im != null) {
                            g.drawImage(im, d, c, b, a, fr);
                            im = null;
                        } else if (drawCross_if_error) {
                            g.setColor(Color.black);
                            g.drawLine(0, 0, DC.xtransfer(params[7]), DC.ytransfer(params[6]));
                            g.drawLine(DC.xtransfer(params[7]), 0, 0, DC.ytransfer(params[6]));
                        }
                        break;
                    default:
                        System.out.println("unsupported ROP code:" + Integer.toString(e, 16));
                }
                break;
            case META_DIBCREATEPATTERNBRUSH:
                if (debug) {
                    System.out.println("MetaDibCreatePatternBrush:" + params[0]);
                }
                im = DIBBitmapImage(2, params, fr);
                if (im != null) {
                    add_handle(new WmfDecObj(im));
                } else {
                    System.out.println("Error in MetaDibCreatePatternBrush");
                }
                break;
            case META_DIBBITBLT:
            case META_STRETCHDIB:
            case META_DIBSTRETCHBLT:
                k = 0;
                switch(rdFunc) {
                    case META_DIBBITBLT:
                        k = -2;
                        if (debug) {
                            System.out.println("MetaDibBitBlt");
                        }
                        break;
                    case META_STRETCHDIB:
                        k = 1;
                        if (debug) {
                            System.out.println("MetaStretchDib");
                        }
                        break;
                    case META_DIBSTRETCHBLT:
                        k = 0;
                        if (debug) {
                            System.out.println("MetaDibStretchBlt");
                        }
                        break;
                }
                a = DC.ytransfer(params[6 + k]);
                b = DC.xtransfer(params[7 + k]);
                c = DC.ytransfer(params[8 + k]);
                d = DC.xtransfer(params[9 + k]);
                e = (params[1] << 16) + params[0];
                if (debug) {
                    System.out.println("dest X= " + d);
                    System.out.println("dest Y= " + c);
                    System.out.println("width = " + b);
                    System.out.println("height= " + a);
                }
                switch(e) {
                    case WHITENESS:
                        g.setColor(Color.white);
                        g.fillRect(d, c, b, a);
                        break;
                    case BLACKNESS:
                        g.setColor(Color.black);
                        g.fillRect(d, c, b, a);
                        break;
                    case SRCCOPY:
                        im = DIBBitmapImage(10 + k, params, fr);
                        if (im != null) {
                            g.drawImage(im, d, c, b, a, fr);
                            im = null;
                        } else if (drawCross_if_error) {
                            g.setColor(Color.black);
                            g.drawLine(d, c, d + b, c + a);
                            g.drawLine(d + b, c, d, c + a);
                        }
                        break;
                    default:
                        System.out.println("unsupported ROP code:" + Integer.toString(e, 16));
                }
                break;
            case META_ESCAPE:
                switch(params[0]) {
                    case MFCOMMENT:
                        if (debug) {
                            text = new byte[params[1]];
                            for (i = 0; i < params[1]; i++) {
                                if (i % 2 == 0) {
                                    text[i] = (byte) getLoByteVal(params[i / 2 + 2]);
                                } else {
                                    text[i] = (byte) getHiByteVal(params[i / 2 + 2]);
                                }
                                if (text[i] == 0) {
                                    break;
                                }
                            }
                            s = new String(text, 0);
                            System.out.println("MetaEscape/MFCOMMENT: " + s);
                        }
                        break;
                    default:
                        if (debug) {
                            System.out.println("MetaEscape #" + params[0] + " " + ((params[1] + 1) >>> 2) + " Words");
                        }
                }
                break;
            case 0:
                return false;
            default:
                showparams(params, rdSize, rdFunc);
                break;
        }
        return true;
    }
