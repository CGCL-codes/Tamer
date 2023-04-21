    int buildStream(Map page, FixedIClip clipp, AffineTransform ctm, CompositeInputStream in, List ocrimgs) throws IOException, ParseException {
        PDFReader pdfr = pdfr_;
        Object[] ops = new Object[6];
        int opsi = 0;
        GraphicsState gs = new GraphicsState();
        List gsstack = new ArrayList(10);
        AffineTransform Tm = new AffineTransform(), Tlm = new AffineTransform(), tmpat = new AffineTransform();
        double Tc = 0.0, Tw = 0.0, Tz = 100.0, TL = 0.0, Ts = 0.0;
        int Tr = 0;
        GeneralPath path = new GeneralPath();
        Color color = Color.BLACK, fcolor = Color.BLACK;
        ColorSpace fCS = ColorSpace.getInstance(ColorSpace.CS_GRAY), sCS = ColorSpace.getInstance(ColorSpace.CS_GRAY);
        float[] cscomp = new float[4];
        List markedseq = new ArrayList(5);
        double curx = 0.0, cury = 0.0;
        Map resources = (Map) pdfr.getObject(page.get("Resources") != null ? page.get("Resources") : new HashMap(1)), xores = (Map) pdfr.getObject(resources.get("XObject")), fontres = (Map) pdfr.getObject(resources.get("Font")), csres = (Map) pdfr.getObject(resources.get("ColorSpace")), patres = (Map) pdfr.getObject(resources.get("Pattern")), shres = (Map) pdfr.getObject(resources.get("Shading")), propres = (Map) pdfr.getObject(resources.get("Properties"));
        Document doc = clipp.getDocument();
        Layer scratchLayer = doc != null ? doc.getLayer(Layer.SCRATCH) : null;
        Point2D srcpt = new Point2D.Double(), transpt = new Point2D.Double();
        double[] d = new double[6];
        INode textp = null;
        FixedIHBox linep = null;
        double baseline = Double.MIN_VALUE;
        FontPDF tf = null;
        Object[] Tja = new Object[1];
        double spacew = 0.0, concatthreshold = 0.0;
        Rectangle2D maxr = null;
        double lastX = 0.0, totalW = 0.0;
        boolean fconcat = false;
        SpanPDF fontspan = null, sspan = null, fillspan = null, Trspan = null;
        StrokeSpan strokespan = null;
        Node lastleaf = clipp;
        boolean fnewfont = true, fnewline = true;
        boolean fstroke = false, ffill = false;
        boolean fvalidpath = false;
        Color newcolor = color, newfcolor = fcolor;
        int newTr = Tr;
        Map fontdict = gs.fontdict = null;
        double pointsize = gs.pointsize = 1.0;
        float linewidth = gs.linewidth = Context.DEFAULT_STROKE.getLineWidth();
        int linecap = gs.linecap = Context.DEFAULT_STROKE.getEndCap(), linejoin = gs.linejoin = Context.DEFAULT_STROKE.getLineJoin();
        float miterlimit = gs.miterlimit = Context.DEFAULT_STROKE.getMiterLimit();
        float[] dasharray = gs.dasharray = Context.DEFAULT_STROKE.getDashArray();
        float dashphase = gs.dashphase = Context.DEFAULT_STROKE.getDashPhase();
        Rectangle pathrect = null;
        Line2D pathline = null;
        boolean fshowshape = (getHints() & MediaAdaptor.HINT_NO_SHAPE) == 0;
        int cmdcnt = 0, leafcnt = 0, spancnt = 0, vspancnt = 0, concatcnt = 0;
        int pathcnt = 0, pathlen = 0;
        int[] pathlens = new int[5000];
        long start = System.currentTimeMillis();
        PDFReader.eatSpace(in);
        for (int c, peek = -1; (c = in.peek()) != -1; ) {
            if (OP[c]) {
                if (opsi >= 6) throw new ParseException("too many operands: " + ops[0] + " " + ops[1] + " ... " + ops[5] + " + more");
                ops[opsi++] = PDFReader.readObject(in);
            } else {
                c = in.read();
                int c2 = in.read(), c3 = -1, c2c3;
                if (c2 == -1 || WSDL[c2] || c == '%') {
                    peek = c2;
                    c2c3 = ' ';
                } else if ((c3 = in.read()) == -1 || WSDL[c3]) {
                    peek = c3;
                    c2c3 = c2;
                } else {
                    c2c3 = (c2 << 8) + c3;
                    peek = in.read();
                    if (peek != -1 && !WSDL[peek]) {
                        if (c == 'e' && c2 == 'n' && c3 == 'd' && peek == 's') break; else throw new ParseException("bad command or no trailing whitespace " + (char) c + (char) c2 + (char) c3 + " + " + peek);
                    }
                }
                cmdcnt++;
                if (DEBUG) {
                    StringBuffer scmd = new StringBuffer(3);
                    scmd.append((char) c);
                    if (c2c3 != ' ') {
                        scmd.append((char) c2);
                        if (c2c3 != c2) scmd.append((char) c3);
                    }
                    Integer arity = (Integer) streamcmds_.get(scmd.toString());
                    boolean ok = arity != null && (arity.intValue() == opsi || (arity.intValue() == Integer.MAX_VALUE && opsi > 0));
                    if (!ok) {
                        System.out.print((arity == null ? "unknown command" : ("bad arity " + opsi + " not " + arity)) + ": |" + scmd + "| [" + c + " " + c2 + "] ");
                        for (int i = 0; i < opsi; i++) System.out.println("\t" + ops[i]);
                        if (DEBUG) assert false;
                        return cmdcnt;
                    }
                }
                if (c != '%') while (peek != -1 && WHITESPACE[peek]) peek = in.read();
                in.unread(peek);
                switch(c) {
                    case 'B':
                        if (c2c3 == ' ') {
                            path.setWindingRule(GeneralPath.WIND_NON_ZERO);
                            ffill = fstroke = true;
                        } else if (c2c3 == '*') {
                            path.setWindingRule(GeneralPath.WIND_EVEN_ODD);
                            ffill = fstroke = true;
                        } else if (c2c3 == 'T') {
                            if (clipp.size() > 0 && textp == clipp.getLastChild() && Math.abs(ctm.getTranslateX() - Tm.getTranslateX()) < 5.0 && Math.abs(ctm.getTranslateX() - Tm.getTranslateX()) < 0.001) {
                            } else {
                                textp = new FixedI("text", null, clipp);
                                linep = new FixedIHBox("line", null, textp);
                                fconcat = false;
                                baseline = Double.MIN_VALUE;
                            }
                            Tm.setTransform(ctm);
                            Tlm.setTransform(Tm);
                        } else if (c2c3 == 'I') {
                            BufferedImage img = Images.createScaledInline(in, csres, ctm, newfcolor, pdfr);
                            lastleaf = appendImage("[inline]", clipp, img, ctm);
                            leafcnt++;
                        } else if (c2c3 == ('M' << 8) + 'C' || c2c3 == ('D' << 8) + 'C') {
                            Map attrs = null;
                            if (c2 == 'D') attrs = (Map) (ops[1].getClass() == CLASS_DICTIONARY ? ops[1] : pdfr.getObject(propres.get(ops[1])));
                            Span seq = (Span) Behavior.getInstance((String) ops[0], "multivalent.Span", attrs, scratchLayer);
                            seq.open(lastleaf);
                            markedseq.add(seq);
                        } else if (c2c3 == 'X') {
                        }
                        break;
                    case 'b':
                        if (c2c3 == ' ') {
                            assert fvalidpath : "b";
                            if (fvalidpath) {
                                if (pathrect == null) path.closePath();
                                path.setWindingRule(GeneralPath.WIND_NON_ZERO);
                                ffill = fstroke = true;
                            }
                        } else if (c2c3 == '*') {
                            assert fvalidpath : "b*";
                            if (fvalidpath) {
                                if (pathrect == null) path.closePath();
                                path.setWindingRule(GeneralPath.WIND_EVEN_ODD);
                                ffill = fstroke = true;
                            }
                        }
                        break;
                    case 'C':
                        if (c2c3 == 'S') {
                            sCS = pdfr.getColorSpace(ops[0], csres, patres);
                            assert sCS != null : "CS stroke " + pdfr.getObject(ops[0]) + " in " + csres;
                        }
                        break;
                    case 'c':
                        if (c2c3 == ' ') {
                            getDoubles(ops, d, 6);
                            ctm.transform(d, 0, d, 0, 3);
                            path.curveTo((float) d[0], (float) d[1], (float) d[2], (float) d[3], (float) (curx = d[4]), (float) (cury = d[5]));
                            pathlen += 100;
                        } else if (c2c3 == 'm') {
                            getDoubles(ops, d, 6);
                            tmpat.setTransform(d[0], d[1], d[2], d[3], d[4], d[5]);
                            if (!tmpat.isIdentity()) {
                                ctm.concatenate(tmpat);
                                if (tmpat.getType() != AffineTransform.TYPE_TRANSLATION) fnewfont = true;
                            }
                        } else if (c2c3 == 's') {
                            fCS = pdfr.getColorSpace(ops[0], csres, patres);
                            assert fCS != null : "cs fill " + pdfr.getObject(ops[0]) + " in " + csres;
                        }
                        break;
                    case 'D':
                        if (c2c3 == 'o') {
                            Leaf l = cmdDo((String) ops[0], xores, resources, ctm, newfcolor, clipp, d, ocrimgs);
                            if (l != null) {
                                lastleaf = l;
                                leafcnt++;
                            }
                        } else if (c2c3 == 'P') {
                            if (lastleaf.isLeaf()) new Mark((Leaf) lastleaf, lastleaf.size());
                        }
                        break;
                    case 'd':
                        if (c2c3 == ' ') {
                            Object[] oa = (Object[]) ops[0];
                            if (oa == OBJECT_NULL || oa.length == 0) gs.dasharray = null; else getFloats(oa, gs.dasharray = new float[oa.length], oa.length);
                            gs.dashphase = ((Number) ops[1]).floatValue();
                            fnewline = true;
                        } else if (c2c3 == '0') {
                            clipp.bbox.width = ((Number) ops[0]).intValue();
                        } else if (c2c3 == '1') {
                            clipp.bbox.width = ((Number) ops[0]).intValue();
                        }
                        break;
                    case 'E':
                        if (c2c3 == 'T') {
                            if (linep.size() == 0) {
                                if (linep != lastleaf) linep.remove(); else new FixedLeafAscii("", null, linep).getIbbox().setBounds((int) Math.round(Tm.getTranslateX()), (int) Math.round(Tm.getTranslateY()), 0, 0);
                            }
                            if (textp.size() == 0) textp.remove();
                        } else if (c2c3 == 'I') {
                            assert false;
                        } else if (c2c3 == ('M' << 8) + 'C') {
                            if (markedseq.size() > 0) {
                                Span seq = (Span) markedseq.remove(markedseq.size() - 1);
                                seq.close(lastleaf);
                            }
                        } else if (c2c3 == 'X') {
                        }
                        break;
                    case 'F':
                        assert c2c3 == ' ';
                    case 'f':
                        if (c2c3 == ' ') {
                            path.setWindingRule(GeneralPath.WIND_NON_ZERO);
                            ffill = true;
                        } else if (c2c3 == '*') {
                            path.setWindingRule(GeneralPath.WIND_EVEN_ODD);
                            ffill = true;
                        }
                        break;
                    case 'G':
                        if (c2c3 == ' ') {
                            sCS = ColorSpace.getInstance(ColorSpace.CS_GRAY);
                            float gray = ((Number) ops[0]).floatValue();
                            newcolor = (gray == 0f ? Color.BLACK : gray == 1f ? Color.WHITE : new Color(gray, gray, gray, 1f));
                        }
                        break;
                    case 'g':
                        if (c2c3 == ' ') {
                            fCS = ColorSpace.getInstance(ColorSpace.CS_GRAY);
                            float gray = ((Number) ops[0]).floatValue();
                            newfcolor = (gray == 0f ? Color.BLACK : gray == 1f ? Color.WHITE : new Color(gray, gray, gray, 1f));
                        } else if (c2c3 == 's') {
                            Map gsdicts = (Map) pdfr.getObject(resources.get("ExtGState"));
                            Map gsdict = (Map) pdfr.getObject(gsdicts.get(ops[0]));
                            cmdgs(gsdict, fontres, ctm, d, gs);
                            if (gsdict.get("Font") != null) fnewfont = true;
                            fnewline = true;
                        }
                        break;
                    case 'h':
                        if (c2c3 == ' ') {
                            assert fvalidpath : "h";
                            if (fvalidpath) {
                                if (pathrect == null) path.closePath();
                            }
                        }
                        break;
                    case 'I':
                        if (c2c3 == 'D') {
                            assert false;
                        }
                        break;
                    case 'i':
                        if (c2c3 == ' ') {
                            gs.flatness = ((Number) ops[0]).intValue();
                        }
                        break;
                    case 'J':
                        if (c2c3 == ' ') {
                            gs.linecap = ((Number) ops[0]).intValue();
                            fnewline = true;
                        }
                        break;
                    case 'j':
                        if (c2c3 == ' ') {
                            gs.linejoin = ((Number) ops[0]).intValue();
                            fnewline = true;
                        }
                        break;
                    case 'K':
                        if (c2c3 == ' ') {
                            sCS = ColorSpaceCMYK.getInstance();
                            getFloats(ops, cscomp, 4);
                            float r = cscomp[0], g = cscomp[1], b = cscomp[2], k = cscomp[3];
                            newcolor = (r == 0f && g == 0f && b == 0f && k == 0f ? Color.WHITE : r + k >= 1f && g + k >= 1f && b + k >= 1f ? Color.BLACK : new Color(sCS, cscomp, 1f));
                        }
                        break;
                    case 'k':
                        if (c2c3 == ' ') {
                            fCS = ColorSpaceCMYK.getInstance();
                            getFloats(ops, cscomp, 4);
                            float r = cscomp[0], g = cscomp[1], b = cscomp[2], k = cscomp[3];
                            newfcolor = (r == 0f && g == 0f && b == 0f && k == 0f ? Color.WHITE : r + k >= 1f && g + k >= 1f && b + k >= 1f ? Color.BLACK : new Color(fCS, cscomp, 1f));
                        }
                        break;
                    case 'l':
                        if (c2c3 == ' ') {
                            getDoubles(ops, d, 2);
                            assert pathline == null : d[0] + " " + d[1];
                            ctm.transform(d, 0, d, 0, 1);
                            if (pathlen == 1 && (((peek = in.peek()) < '0' || peek > '9') && peek != '.' && peek != '-')) pathline = new Line2D.Double(curx, cury, d[0], d[1]); else path.lineTo((float) (curx = d[0]), (float) (cury = d[1]));
                            pathlen += 1000;
                        }
                        break;
                    case 'M':
                        if (c2c3 == ' ') {
                            gs.miterlimit = ((Number) ops[0]).intValue();
                            fnewline = true;
                        } else if (c2c3 == 'P') {
                            if (lastleaf.isLeaf()) new Mark((Leaf) lastleaf, lastleaf.size());
                        }
                        break;
                    case 'm':
                        if (c2c3 == ' ') {
                            assert pathrect == null && pathline == null;
                            getDoubles(ops, d, 2);
                            ctm.transform(d, 0, d, 0, 1);
                            path.moveTo((float) (curx = d[0]), (float) (cury = d[1]));
                            pathlen++;
                            fvalidpath = true;
                        }
                        break;
                    case 'n':
                        if (c2c3 == ' ') {
                            path.reset();
                            pathlen = 0;
                            pathrect = null;
                            pathline = null;
                            fvalidpath = false;
                        }
                        break;
                    case 'Q':
                        if (c2c3 == ' ') {
                            if (gsstack.size() > 0) gs = (GraphicsState) gsstack.remove(gsstack.size() - 1);
                            fnewfont = true;
                            newTr = gs.Tr;
                            Tc = gs.Tc;
                            Tw = gs.Tw;
                            Tz = gs.Tz;
                            TL = gs.TL;
                            Ts = gs.Ts;
                            fCS = gs.fCS;
                            sCS = gs.sCS;
                            newcolor = gs.strokecolor;
                            newfcolor = gs.fillcolor;
                            fnewline = true;
                            ctm = gs.ctm;
                            if (clipp != gs.clip && clipp.size() == 0) clipp.remove();
                            clipp = gs.clip;
                            fvalidpath = false;
                        }
                        break;
                    case 'q':
                        if (c2c3 == ' ') {
                            gs.Tr = newTr;
                            gs.Tc = Tc;
                            gs.Tw = Tw;
                            gs.Tz = Tz;
                            gs.TL = TL;
                            gs.Ts = Ts;
                            gs.fCS = fCS;
                            gs.sCS = sCS;
                            gs.strokecolor = newcolor;
                            gs.fillcolor = newfcolor;
                            gs.ctm = ctm;
                            gs.clip = clipp;
                            gsstack.add(new GraphicsState(gs));
                        }
                        break;
                    case 'R':
                        if (c2c3 == 'G') {
                            sCS = ColorSpace.getInstance(ColorSpace.CS_sRGB);
                            getFloats(ops, cscomp, 3);
                            float r = cscomp[0], g = cscomp[1], b = cscomp[2];
                            newcolor = (r == 0f && g == 0f && b == 0f ? Color.BLACK : r == 1f && g == 1f && b == 1f ? Color.WHITE : new Color(r, g, b, 1f));
                        }
                        break;
                    case 'r':
                        if (c2c3 == 'e') {
                            assert pathrect == null && pathline == null;
                            getDoubles(ops, d, 4);
                            ctm.transform(d, 0, d, 0, 1);
                            ctm.deltaTransform(d, 2, d, 2, 1);
                            double x = curx = d[0], y = cury = d[1], w = d[2], h = d[3];
                            if (w < 0.0) {
                                x += w;
                                w = -w;
                            }
                            if (w < 1.0) w = 1.0;
                            if (h < 0.0) {
                                y += h;
                                h = -h;
                            }
                            if (h < 1.0) h = 1.0;
                            Rectangle r = new Rectangle((int) x, (int) (y), (int) Math.round(w), (int) Math.round(h));
                            if (!fvalidpath && (((peek = in.peek()) < '0' || peek > '9') && peek != '.' && peek != '-')) {
                                pathrect = r;
                                pathlen = 1;
                            } else {
                                path.append(r, false);
                                pathlen += 4;
                            }
                            fvalidpath = true;
                        } else if (c2c3 == 'g') {
                            fCS = ColorSpace.getInstance(ColorSpace.CS_sRGB);
                            getFloats(ops, cscomp, 3);
                            float r = cscomp[0], g = cscomp[1], b = cscomp[2];
                            newfcolor = (r == 0f && g == 0f && b == 0f ? Color.BLACK : r == 1f && g == 1f && b == 1f ? Color.WHITE : new Color(r, g, b, 1f));
                        } else if (c2c3 == 'i') {
                            gs.renderingintent = (String) ops[0];
                        }
                        break;
                    case 'S':
                        if (c2c3 == ' ') {
                            fstroke = true;
                        } else if (c2c3 == 'C' || c2c3 == ('C' << 8) + 'N') {
                            if (opsi > 0 && ops[opsi - 1].getClass() == CLASS_NAME) {
                                assert c2c3 == ('C' << 8) + 'N';
                                sCS = pdfr.getColorSpace(ops[opsi - 1], csres, patres);
                                opsi--;
                            }
                            if (opsi > 0) {
                                getFloats(ops, cscomp, Math.min(opsi, 4));
                                float[] rgb = sCS.toRGB(cscomp);
                                newcolor = new Color(rgb[0], rgb[1], rgb[2], 1f);
                            }
                        }
                        break;
                    case 's':
                        if (c2c3 == ' ') {
                            assert fvalidpath : "s";
                            if (fvalidpath) {
                                if (pathrect == null) path.closePath();
                                fstroke = true;
                            }
                        } else if (c2c3 == 'c' || c2c3 == ('c' << 8) + 'n') {
                            if (opsi > 0 && ops[opsi - 1].getClass() == CLASS_NAME) {
                                assert c2c3 == ('c' << 8) + 'n';
                                fCS = pdfr.getColorSpace(ops[opsi - 1], csres, patres);
                                opsi--;
                            }
                            if (opsi > 0) {
                                getFloats(ops, cscomp, Math.min(opsi, 4));
                                float[] rgb = fCS.toRGB(cscomp);
                                newfcolor = new Color(rgb[0], rgb[1], rgb[2], 1f);
                            }
                        } else if (c2c3 == 'h') {
                            Map shdict = (Map) pdfr.getObject(shres.get(ops[0]));
                            ColorSpace cs = pdfr.getColorSpace(shdict.get("ColorSpace"), csres, patres);
                            Object[] oa = (Object[]) pdfr.getObject(shdict.get("Bbox"));
                            Rectangle bbox = (oa != null ? PDFReader.array2Rectangle(oa, ctm, false) : clipp.getCrop());
                            FixedLeafShade l = FixedLeafShade.getInstance(shdict, cs, bbox, clipp, pdfr);
                            lastleaf = l;
                            leafcnt++;
                            l.getBbox().setBounds(l.getIbbox());
                            l.setValid(true);
                        }
                        break;
                    case '"':
                        if (c2c3 == ' ') {
                            getDoubles(ops, d, 2);
                            Tw = d[0];
                            Tc = d[1];
                            ops[0] = ops[2];
                        }
                    case '\'':
                        if (c2c3 == ' ') {
                            Tlm.translate(0.0, -TL);
                            Tm.setTransform(Tlm);
                            c2c3 = 'j';
                        }
                    case 'T':
                        if (c2c3 == 'j' || c2c3 == 'J') {
                            if (lastleaf.isStruct()) lastleaf = linep;
                            float newsize = 0f;
                            if (fnewfont) {
                                srcpt.setLocation(gs.pointsize, 0.0);
                                Tm.deltaTransform(srcpt, transpt);
                                double zx = transpt.getX(), zy = transpt.getY();
                                newsize = (float) Math.abs(zy == 0.0 ? zx : zx == 0.0 ? zy : Math.sqrt(zx * zx + zy * zy));
                                if (fontdict == gs.fontdict && Math.abs(newsize - tf.getSize2D()) < 0.0001) {
                                    fnewfont = false;
                                }
                            }
                            if (fnewfont) {
                                if (fontspan != null) {
                                    fontspan.close(lastleaf);
                                    assert !fshowshape || fontspan.isSet() : "can't add font span " + getName() + "  " + fontspan.getStart().leaf + " .. " + lastleaf;
                                    spancnt++;
                                }
                                fontdict = gs.fontdict;
                                pointsize = gs.pointsize;
                                tf = pdfr.getFont(fontdict, pointsize, newsize, page, Tm, this);
                                maxr = tf.getMaxCharBounds();
                                fontspan = (SpanPDF) Behavior.getInstance((DEBUG ? tf.getFamily() + " " + tf.getSize2D() : tf.getFamily()), "multivalent.std.adaptor.pdf.SpanPDF", null, scratchLayer);
                                fontspan.font = tf.getFont();
                                fontspan.open(lastleaf);
                                spacew = tf.measureText(' ') / Tm.getScaleX();
                                concatthreshold = spacew * Tm.getScaleX() / 4.0;
                                fnewfont = false;
                            }
                            if (!tf.canRender()) break;
                            boolean fType3 = tf instanceof FontType3;
                            if (fType3) ((FontType3) tf).setPage(page);
                            if (newTr != Tr) {
                                if (Trspan != null) {
                                    Trspan.close(lastleaf);
                                    spancnt++;
                                }
                                Tr = newTr;
                                if (Tr == 0) {
                                    Trspan = null;
                                    vspancnt++;
                                } else {
                                    Trspan = (SpanPDF) Behavior.getInstance("Tr", "multivalent.std.adaptor.pdf.SpanPDF", null, scratchLayer);
                                    Trspan.Tr = Tr;
                                    Trspan.open(lastleaf);
                                }
                            }
                            double newbaseline = Tm.getTranslateY();
                            if (newbaseline != baseline && Math.abs(baseline - newbaseline) > 0.1) {
                                if (linep.size() > 0) {
                                    linep = new FixedIHBox("line", null, textp);
                                    fconcat = false;
                                }
                                baseline = newbaseline;
                            }
                            Object[] oa;
                            if (c2c3 == 'j') {
                                oa = Tja;
                                oa[0] = ops[0];
                            } else oa = (Object[]) ops[0];
                            double sTc = Tc * Tm.getScaleX(), kern1 = (sTc >= 0.0 ? Math.floor(sTc) : Math.ceil(sTc));
                            boolean fspace1 = false;
                            boolean frot = Tm.getShearX() != 0.0;
                            FontRenderContext frc = (frot ? new FontRenderContext(Tm, true, true) : null);
                            for (int i = 0, imax = oa.length; i < imax; i++) {
                                Object o = oa[i];
                                if (o instanceof Number) {
                                    double kern = ((Number) o).doubleValue() / 1000.0 * pointsize;
                                    Tm.translate(-kern, 0.0);
                                } else {
                                    assert o.getClass() == CLASS_STRING;
                                    StringBuffer txt8 = (StringBuffer) o;
                                    String txt = tf.translate(txt8);
                                    for (int s = 0, smax = txt.length(), e; s < smax; s = e) {
                                        e = txt.indexOf(' ', s);
                                        if (e == -1) e = smax; else if (e == 0 && i == 0 && s == 0 && ALL_WS.reset(txt).matches()) {
                                            e = 1;
                                            fspace1 = true;
                                        }
                                        if (s < e) {
                                            String sub = txt.substring(s, e);
                                            double kern = kern1 * sub.length();
                                            double fw = tf.measureText(txt8, s, e) + kern;
                                            int bw = (int) Math.ceil(fw), bh = (int) Math.ceil(maxr.getHeight()), ascent = (int) Math.ceil(-maxr.getY());
                                            double dx = Tm.getTranslateX() - lastX;
                                            if (frot && false) {
                                                GlyphVector gv = tf.getFont().createGlyphVector(frc, sub);
                                                Shape txtshp = gv.getOutline();
                                                Rectangle2D r2d = gv.getVisualBounds();
                                                Rectangle r = gv.getPixelBounds(frc, (float) Tm.getTranslateX(), (float) Tm.getTranslateY());
                                                FixedLeafShape lshp = new FixedLeafShape("glyphs", null, linep, txtshp, true, true);
                                                lastleaf = lshp;
                                                leafcnt++;
                                                lshp.getIbbox().setBounds(r);
                                                lshp.getBbox().setBounds(r);
                                            } else if (fType3) {
                                                FontType3 tf3 = (FontType3) tf;
                                                FixedLeafType3 l = new FixedLeafType3(sub, null, linep, tf3);
                                                lastleaf = l;
                                                leafcnt++;
                                                int w = 0, minascent = 0, maxdescent = 0;
                                                for (int j = 0, jmax = txt.length(); j < jmax; j++) {
                                                    Node glyph = tf3.getGlyph(txt.charAt(j));
                                                    Rectangle bbox = glyph.bbox;
                                                    w += bbox.width;
                                                    minascent = Math.min(minascent, bbox.y);
                                                    maxdescent = Math.max(maxdescent, bbox.height + bbox.y);
                                                }
                                                l.getIbbox().setBounds((int) Math.round(Tm.getTranslateX()), (int) Math.round(Tm.getTranslateY() + Ts * Tm.getScaleY() + minascent), w, maxdescent - minascent);
                                                l.getBbox().setBounds(l.getIbbox());
                                                l.baseline = -minascent;
                                            } else if (fconcat && Math.abs(dx) < concatthreshold && Ts == 0.0) {
                                                FixedLeafAsciiKern l = (FixedLeafAsciiKern) linep.getLastChild();
                                                assert l == lastleaf;
                                                l.appendText(sub, (byte) kern1);
                                                l.setKernAt(l.size() - 1, (byte) (l.getKernAt(l.size() - 1) + dx));
                                                Rectangle ibbox = l.getIbbox();
                                                totalW += fw;
                                                ibbox.width = (int) Math.ceil(totalW);
                                                ibbox.height = Math.max(ibbox.height, bh);
                                                l.bbox.setSize(ibbox.width, ibbox.height);
                                                concatcnt++;
                                            } else {
                                                FixedLeafAsciiKern l = new FixedLeafAsciiKern(sub, null, linep, (byte) kern1);
                                                lastleaf = l;
                                                leafcnt++;
                                                l.getIbbox().setBounds((int) Math.round(Tm.getTranslateX()), (int) Math.round(Tm.getTranslateY() + maxr.getY() + Ts * Tm.getScaleY()), bw, bh);
                                                l.bbox.setBounds(l.getIbbox());
                                                l.baseline = ascent;
                                                totalW = fw;
                                            }
                                            lastleaf.setValid(true);
                                            tmpat.setToTranslation(fw + (sTc * sub.length() - kern), 0.0);
                                            Tm.preConcatenate(tmpat);
                                            lastX = Tm.getTranslateX();
                                            fconcat = !frot && !fType3 && sub.charAt(sub.length() - 1) < 128;
                                        }
                                        if (fspace1) {
                                            Tm.translate(Tw, 0.0);
                                            fspace1 = false;
                                            fconcat = false;
                                        }
                                        if (e < smax && txt.charAt(e) == ' ') {
                                            int spacecnt = 0;
                                            do {
                                                spacecnt++;
                                                e++;
                                            } while (e < smax && txt.charAt(e) == ' ');
                                            Tm.translate(spacecnt * (spacew + Tw + Tc), 0.0);
                                            fconcat = false;
                                        }
                                    }
                                }
                            }
                        } else if (c2c3 == 'd') {
                            getDoubles(ops, d, 2);
                            Tlm.translate(d[0], d[1]);
                            Tm.setTransform(Tlm);
                        } else if (c2c3 == 'D') {
                            getDoubles(ops, d, 2);
                            TL = -d[1];
                            Tlm.translate(d[0], d[1]);
                            Tm.setTransform(Tlm);
                        } else if (c2c3 == 'm') {
                            double m00 = Tm.getScaleX(), m01 = Tm.getShearX(), m10 = Tm.getShearY(), m11 = Tm.getScaleY();
                            getDoubles(ops, d, 6);
                            tmpat.setTransform(d[0], d[1], d[2], d[3], d[4], d[5]);
                            Tm.setTransform(ctm);
                            Tm.concatenate(tmpat);
                            Tlm.setTransform(Tm);
                            if (m00 != Tm.getScaleX() || m01 != Tm.getShearX() || m10 != Tm.getShearY() || m11 != Tm.getScaleY()) fnewfont = true;
                        } else if (c2c3 == '*') {
                            Tlm.translate(0.0, -TL);
                            Tm.setTransform(Tlm);
                        } else if (c2c3 == 'c') {
                            getDoubles(ops, d, 1);
                            Tc = d[0];
                        } else if (c2c3 == 'w') {
                            getDoubles(ops, d, 1);
                            Tw = d[0];
                        } else if (c2c3 == 'z') {
                            getDoubles(ops, d, 1);
                            Tz = d[0];
                        } else if (c2c3 == 'L') {
                            getDoubles(ops, d, 1);
                            TL = d[0];
                        } else if (c2c3 == 'f') {
                            assert fontres != null : page;
                            gs.fontdict = (Map) pdfr.getObject(fontres.get(ops[0]));
                            assert gs.fontdict != null : ops[0] + " not in " + fontres;
                            gs.pointsize = ((Number) ops[1]).doubleValue();
                            fnewfont = true;
                        } else if (c2c3 == 'r') {
                            newTr = ((Number) ops[0]).intValue();
                        } else if (c2c3 == 's') {
                            getDoubles(ops, d, 1);
                            d[1] = 0.0;
                            Tm.deltaTransform(d, 0, d, 0, 1);
                            Ts = Math.abs(d[0]);
                        }
                        break;
                    case 'v':
                        if (c2c3 == ' ') {
                            getDoubles(ops, d, 4);
                            ctm.transform(d, 0, d, 0, 2);
                            path.curveTo((float) curx, (float) cury, (float) d[0], (float) d[1], (float) (curx = d[2]), (float) (cury = d[3]));
                            pathlen += 100;
                        }
                        break;
                    case 'W':
                        if (c2c3 == '*' || c2c3 == ' ') {
                            if (fvalidpath) {
                                Rectangle bounds;
                                if (pathrect != null) {
                                    bounds = new Rectangle(pathrect.x, pathrect.y, pathrect.width + 1, pathrect.height + 1);
                                    Shape oldshape = clipp.getClip();
                                    if (!(oldshape instanceof Rectangle) || !pathrect.contains((Rectangle) oldshape)) clipp = new FixedIClip(c2c3 == '*' ? "W*" : "W", null, clipp, new Rectangle(0, 0, bounds.width, bounds.height), bounds);
                                } else if (pathline != null) {
                                    bounds = null;
                                } else {
                                    GeneralPath wpath;
                                    if (in.peek() == 'n') {
                                        wpath = path;
                                        path = new GeneralPath();
                                    } else {
                                        wpath = (GeneralPath) path.clone();
                                    }
                                    wpath.closePath();
                                    wpath.setWindingRule(c2c3 == '*' ? GeneralPath.WIND_EVEN_ODD : GeneralPath.WIND_NON_ZERO);
                                    bounds = wpath.getBounds();
                                    tmpat.setToTranslation(-bounds.x, -bounds.y);
                                    wpath.transform(tmpat);
                                    clipp = new FixedIClip(c2c3 == '*' ? "W*" : "W", null, clipp, wpath, bounds);
                                }
                            }
                        }
                        break;
                    case 'w':
                        if (c2c3 == ' ') {
                            d[0] = ((Number) ops[0]).doubleValue();
                            d[1] = 0.0;
                            ctm.deltaTransform(d, 0, d, 0, 1);
                            gs.linewidth = (float) Math.abs(d[1] == 0.0 ? d[0] : d[0] == 0.0 ? d[1] : Math.sqrt(d[0] * d[0] + d[1] * d[1]));
                            fnewline = true;
                        }
                        break;
                    case 'y':
                        if (c2c3 == ' ') {
                            getDoubles(ops, d, 4);
                            ctm.transform(d, 0, d, 0, 2);
                            path.curveTo((float) d[0], (float) d[1], (float) d[2], (float) d[3], (float) (curx = d[2]), (float) (cury = d[3]));
                            pathlen += 100;
                        }
                        break;
                    case '%':
                        while ((c = in.read()) != -1 && c != '\r' && c != '\n') {
                        }
                        PDFReader.eatSpace(in);
                        break;
                    default:
                        assert false : (char) c + " / " + c;
                        throw new ParseException("invalid command: " + (char) c + "...");
                }
                opsi = 0;
                if (fstroke || ffill) {
                    if (fnewline && ((gs.linewidth = (gs.linewidth < 1f ? 1f : gs.linewidth)) != linewidth || gs.linecap != linecap || gs.linejoin != linejoin || gs.miterlimit != miterlimit || gs.dashphase != dashphase || !Arrays.equals(gs.dasharray, dasharray))) {
                        fnewline = false;
                        if (strokespan != null) {
                            strokespan.close(lastleaf);
                            spancnt++;
                        }
                        linewidth = gs.linewidth;
                        linecap = gs.linecap;
                        linejoin = gs.linejoin;
                        miterlimit = gs.miterlimit;
                        dasharray = gs.dasharray;
                        dashphase = gs.dashphase;
                        if (dasharray != null) {
                            int dai = 0;
                            for (int i = 0, imax = dasharray.length; i < imax; i++) if (dasharray[i] > 0f) dasharray[dai++] = dasharray[i];
                            if (dai < dasharray.length) {
                                float[] newda = new float[dai];
                                System.arraycopy(dasharray, 0, newda, 0, dai);
                                dasharray = newda;
                            }
                        }
                        BasicStroke bs = new BasicStroke(linewidth, linecap, linejoin, miterlimit, dasharray, dashphase);
                        if (Context.DEFAULT_STROKE.equals(bs)) {
                            strokespan = null;
                            vspancnt++;
                        } else {
                            strokespan = (StrokeSpan) Behavior.getInstance("width" + linejoin, "multivalent.std.span.StrokeSpan", null, scratchLayer);
                            strokespan.setStroke(bs);
                            strokespan.open(lastleaf);
                        }
                    }
                    if (!fshowshape) {
                        pathrect = null;
                        pathline = null;
                        path.reset();
                    } else if (fvalidpath) {
                        Shape shape;
                        Rectangle bounds;
                        String name;
                        if (pathrect != null) {
                            assert pathlen == 1 : pathlen;
                            bounds = pathrect;
                            shape = new Rectangle(0, 0, pathrect.width, pathrect.height);
                            name = "rect";
                            pathrect = null;
                            assert pathline == null : pathline;
                        } else if (pathline != null) {
                            double x1 = pathline.getX1(), y1 = pathline.getY1(), x2 = pathline.getX2(), y2 = pathline.getY2(), xmin, ymin, w2d, h2d;
                            if (x1 <= x2) {
                                xmin = x1;
                                w2d = x2 - x1;
                            } else {
                                xmin = x2;
                                w2d = x1 - x2;
                            }
                            if (y1 <= y2) {
                                ymin = y1;
                                h2d = y2 - y1;
                            } else {
                                ymin = y2;
                                h2d = y1 - y2;
                            }
                            bounds = new Rectangle((int) Math.round(xmin), (int) Math.round(ymin), (w2d > 1.0 ? (int) Math.ceil(w2d) : 1), (h2d > 1.0 ? (int) Math.ceil(h2d) : 1));
                            shape = new Line2D.Double(x1 - xmin, y1 - ymin, x2 - xmin, y2 - ymin);
                            name = (DEBUG ? "line" + (pathcnt) : "line");
                            pathline = null;
                            path.reset();
                        } else {
                            Rectangle2D r2d = path.getBounds2D();
                            double w2d = r2d.getWidth(), h2d = r2d.getHeight();
                            bounds = new Rectangle((int) Math.round(r2d.getX()), (int) Math.round(r2d.getY()), (w2d > 1.0 ? (int) Math.ceil(w2d) : 1), (h2d > 1.0 ? (int) Math.ceil(h2d) : 1));
                            tmpat.setToTranslation(-r2d.getX(), -r2d.getY());
                            path.transform(tmpat);
                            shape = path;
                            name = (DEBUG ? "path" + pathcnt : "path");
                            path = new GeneralPath();
                        }
                        if (shape != null) {
                            FixedLeafShape l = new FixedLeafShape(name, null, clipp, shape, fstroke, ffill);
                            lastleaf = l;
                            leafcnt++;
                            l.getIbbox().setBounds(bounds);
                            l.getBbox().setBounds(bounds);
                            l.setValid(true);
                            pathlens[pathcnt] = pathlen;
                            pathlen = 0;
                            if (pathcnt + 1 < pathlens.length) pathcnt++;
                        }
                    } else {
                        if (lastleaf instanceof FixedLeafShape) {
                            FixedLeafShape l = (FixedLeafShape) lastleaf;
                            if (fstroke) l.setStroke(true); else l.setFill(true);
                        }
                    }
                    fvalidpath = false;
                    fstroke = ffill = false;
                }
                if (color != newcolor && !color.equals(newcolor)) {
                    if (sspan != null) {
                        if (sspan.close(lastleaf)) spancnt++; else sspan.destroy();
                    }
                    color = newcolor;
                    assert color != null : color;
                    if (Color.BLACK.equals(color)) {
                        sspan = null;
                        vspancnt++;
                    } else {
                        sspan = (SpanPDF) Behavior.getInstance((DEBUG ? "stroke " + Integer.toHexString(color.getRGB()) : "stroke"), "multivalent.std.adaptor.pdf.SpanPDF", null, scratchLayer);
                        sspan.stroke = color;
                        sspan.open(lastleaf);
                    }
                }
                if (fcolor != newfcolor && !fcolor.equals(newfcolor)) {
                    if (fillspan != null) {
                        if (fillspan.close(lastleaf)) spancnt++; else fillspan.destroy();
                    }
                    fcolor = newfcolor;
                    assert fcolor != null : fcolor;
                    if (Color.BLACK.equals(fcolor)) {
                        fillspan = null;
                        vspancnt++;
                    } else {
                        fillspan = (SpanPDF) Behavior.getInstance((DEBUG ? "fill " + Integer.toHexString(fcolor.getRGB()) : "fill"), "multivalent.std.adaptor.pdf.SpanPDF", null, scratchLayer);
                        fillspan.fill = fcolor;
                        fillspan.open(lastleaf);
                    }
                }
            }
        }
        spancnt += Span.closeAll(clipp);
        if (Multivalent.MONITOR && ocrimgs != null) {
            System.out.println(cmdcnt + " cmds, " + leafcnt + " leaves, " + spancnt + " spans (" + vspancnt + " saved), " + concatcnt + " concats, " + pathcnt + " paths, time=" + (System.currentTimeMillis() - start));
        }
        return cmdcnt;
    }
