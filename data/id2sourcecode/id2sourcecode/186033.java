    public void main(int width, int height) {
        Pixstore image = new Pixstore(width, height);
        Render rend = new Render(this, image);
        rend.setBackgroundCol(0x808080);
        rend.setIntersect(false);
        rend.setLightDir(origLight);
        rend.setAntiAlias(false);
        Pixstore texture = null;
        {
            String imageFile = getParameter("IMAGE_FILE");
            if (imageFile == null) {
                System.err.println("Please supply an image filename..");
                System.err.println("<PARAM NAME=\"IMAGE_FILE\" VALUE=\"teapot.jpg\">");
            } else {
                texture = new Pixstore(this, imageFile, image);
            }
        }
        {
            String backgroundCol = getParameter("BACKGROUND_COL");
            if (backgroundCol != null) {
                int bc = 0;
                try {
                    bc = Integer.decode(backgroundCol).intValue();
                } catch (NumberFormatException e) {
                    System.err.println("Error with integer number " + backgroundCol);
                }
                rend.setBackgroundCol(bc);
            }
            String wds = getParameter("PIECES_X");
            if (wds != null) {
                int bc = 0;
                try {
                    bc = Integer.decode(wds).intValue();
                } catch (NumberFormatException e) {
                    System.err.println("Error with integer number " + wds);
                }
                wid = bc;
            }
            wds = getParameter("PIECES_Y");
            if (wds != null) {
                int bc = 0;
                try {
                    bc = Integer.decode(wds).intValue();
                } catch (NumberFormatException e) {
                    System.err.println("Error with integer number " + wds);
                }
                ht = bc;
            }
        }
        String basename = getParameter("PIECES_BASENAME");
        Pixstore textureBright = null;
        {
            int xr = texture.getWidth();
            int yr = texture.getHeight();
            textureBright = new Pixstore(xr, yr);
            texBri(texture, textureBright, 30f, 1.2f);
        }
        Vector objects = new Vector();
        Matrix rot = new Matrix();
        rot.setRotationXyzProgressive(0, 0, 0);
        JigPiece all[] = new JigPiece[wid * ht];
        try {
            for (int y = 0; y < ht; ++y) {
                for (int x = 0; x < wid; ++x) {
                    int i = x + wid * y;
                    String j = java.lang.Integer.toString(i);
                    InputStream s = Animate.class.getResourceAsStream(basename + j + ".fm");
                    InputStreamReader si = new InputStreamReader(s);
                    BufferedReader br = new BufferedReader(si);
                    String st = "";
                    JigPiece fa = new JigPiece();
                    all[i] = fa;
                    Jig face = new Jig();
                    fa.j = face;
                    NumberFormat df = NumberFormat.getInstance(new Locale("en", "IE", ""));
                    st = br.readLine();
                    while (st != null) {
                        if (st.startsWith("Spot {")) {
                            st = st.substring(st.indexOf(' ', 6) + 1, st.length() - 1);
                            int i1 = st.indexOf(',');
                            int i2 = st.indexOf(',', i1 + 1);
                            String xc = st.substring(0, i1);
                            String yc = st.substring(i1 + 1, i2);
                            String zc = st.substring(i2 + 1);
                            Number xn = df.parse(xc);
                            Number yn = df.parse(yc);
                            Number zn = df.parse(zc);
                            Vertex f = new Vertex(new Vec((xn.floatValue() - 400) * 0.001f, (yn.floatValue() - 400) * 0.001f, zn.floatValue() * .0001f));
                            face.addVert(f);
                        } else if (st.startsWith("Norm {")) {
                            st = st.substring(st.indexOf(' ', 6) + 1, st.length() - 1);
                            int i1 = st.indexOf(',');
                            int i2 = st.indexOf(',', i1 + 1);
                            String xc = st.substring(0, i1);
                            String yc = st.substring(i1 + 1, i2);
                            String zc = st.substring(i2 + 1);
                            Number xn = df.parse(xc);
                            Number yn = df.parse(yc);
                            Number zn = df.parse(zc);
                            Vec vv = new Vec(-xn.floatValue(), -yn.floatValue(), -zn.floatValue());
                            vv.makeUnitVec();
                            VertexNorm f = new VertexNorm(vv);
                            face.addVertNorm(f);
                        } else if (st.startsWith("TriPhong {")) {
                            st = st.substring(10, st.length() - 1);
                            int i1 = st.indexOf(',');
                            int i2 = st.indexOf(',', i1 + 1);
                            int i3 = st.indexOf(' ', i2 + 1);
                            int i4 = st.indexOf(',', i3 + 1);
                            int i5 = st.indexOf(',', i4 + 1);
                            int i6 = st.indexOf(' ', i5 + 1);
                            if (i6 > 0) {
                                String xc = st.substring(0, i1);
                                String yc = st.substring(i1 + 1, i2);
                                String zc = st.substring(i2 + 1, i3);
                                Vertex av = face.getVert(df.parse(xc).intValue());
                                Vertex bv = face.getVert(df.parse(yc).intValue());
                                Vertex cv = face.getVert(df.parse(zc).intValue());
                                xc = st.substring(i3 + 1, i4);
                                yc = st.substring(i4 + 1, i5);
                                zc = st.substring(i5 + 1, i6);
                                VertexNorm an = face.getVertNorm(df.parse(xc).intValue());
                                VertexNorm bn = face.getVertNorm(df.parse(yc).intValue());
                                VertexNorm cn = face.getVertNorm(df.parse(zc).intValue());
                                if (an == null) System.out.println(xc);
                                if (bn == null) System.out.println(yc);
                                if (cn == null) System.out.println(zc);
                                i3 = i6;
                                i3 += 3;
                                i1 = st.indexOf(',', i3 + 1);
                                i2 = st.indexOf(' ', i1 + 1);
                                float tx1 = df.parse(st.substring(i3, i1)).floatValue();
                                float ty1 = df.parse(st.substring(i1 + 1, i2)).floatValue();
                                i3 = i2 + 1;
                                i1 = st.indexOf(',', i3 + 1);
                                i2 = st.indexOf(' ', i1 + 1);
                                float tx2 = df.parse(st.substring(i3, i1)).floatValue();
                                float ty2 = df.parse(st.substring(i1 + 1, i2)).floatValue();
                                i3 = i2 + 1;
                                i1 = st.indexOf(',', i3 + 1);
                                float tx3 = df.parse(st.substring(i3, i1)).floatValue();
                                float ty3 = df.parse(st.substring(i1 + 1)).floatValue();
                                boolean hd;
                                {
                                    float d1 = av.getPos().z - bv.getPos().z;
                                    float d2 = av.getPos().z - cv.getPos().z;
                                    float sm = 0.00002f;
                                    hd = (d1 > sm || d1 < -sm || d2 > sm || d2 < -sm);
                                }
                                Tri t;
                                if (!hd) {
                                    TexTri tt = new TexTri(av, bv, cv, texture, new Vec2(tx1, ty1), new Vec2(tx2, ty2), new Vec2(tx3, ty3));
                                    tt.setShade(false);
                                    t = tt;
                                } else {
                                    t = new PhongTexTri(av, bv, cv, an, bn, cn, textureBright, new Vec2(tx1, ty1), new Vec2(tx2, ty2), new Vec2(tx3, ty3));
                                }
                                face.addTri(t);
                            }
                        }
                        st = br.readLine();
                    }
                    s.close();
                    face.optimise();
                    Vec cen = face.getCentre();
                    fa.origPos = cen;
                    face.ht = 4.0f - (0.001f * i);
                    face.offsetAll(new Vec(-cen.x, -cen.y, 0.0f), 1.00f);
                    if (true) {
                        face.setOffset(new Vec(((float) Math.random() - 0.5f) * 0.7f, ((float) Math.random() - 0.5f) * 0.7f, face.ht));
                        face.orien = (int) (Math.random() * 3.5);
                    } else {
                        face.setOffset(new Vec(cen.x, cen.y, face.ht));
                    }
                    rot.setRotationXyzProgressive(0, 0, 1.5707f * face.orien);
                    face.setRot(rot);
                    objects.addElement(face);
                }
            }
        } catch (java.io.IOException i) {
            System.exit(0);
        } catch (java.text.ParseException i) {
            System.exit(0);
        }
        long timeStart = System.currentTimeMillis();
        Thread thisThread = Thread.currentThread();
        int lastX = 0;
        int lastY = 0;
        int fr = 0;
        Jig moving = null;
        float movPlaneDist = 5;
        int refreshCount = 0;
        int rotating = 0;
        boolean lastRotateButtonPressed = false;
        Vector rotators = new Vector();
        Vector redrawJigs = new Vector();
        int completedAnim = 0;
        while (_thread == thisThread) {
            boolean leftButtonPressed = getMouseButton(LEFTBUTTON);
            boolean midButtonPressed = getMouseButton(MIDDLEBUTTON);
            boolean rightButtonPressed = getMouseButton(RIGHTBUTTON);
            if (completedAnim > 0) {
                leftButtonPressed = false;
                midButtonPressed = false;
                rightButtonPressed = false;
                ++completedAnim;
            }
            redrawJigs.removeAllElements();
            for (int i = 0; i < rotators.size(); ++i) {
                Rotator ro = (Rotator) rotators.elementAt(i);
                if (!ro.perform(all, objects)) {
                    rotators.removeElementAt(i);
                    --i;
                }
                redrawJigs.addElement(ro.moving);
                drawn = false;
            }
            if (moving != null) {
                if (!lastRotateButtonPressed && (rightButtonPressed || midButtonPressed)) {
                    addRotator(moving, rotators, objects, all, redrawJigs, new Vec(0, 0, 0), rightButtonPressed);
                    drawn = false;
                }
                if (!leftButtonPressed) {
                    Vec p = moving.getOffset();
                    moving.setOffset(new Vec(p.x, p.y, moving.ht));
                    dropPiece(moving, all, objects);
                    moving = null;
                    drawn = false;
                } else {
                    int nx = getMouseX();
                    int ny = getMouseY();
                    if (nx != lastX || ny != lastY) {
                        float movHt = moving.ht - 0.05f;
                        Vec boardNorm = new Vec(0, 0, 1);
                        Vec lineDir = rend.getPointerVec(nx, ny);
                        float k2 = Vec.dot(boardNorm, lineDir);
                        float distAlongLine = movHt / k2;
                        Vec p = Vec.mul(lineDir, distAlongLine);
                        final float lim = 5;
                        if (p.x < -lim) p.x = -lim; else if (p.x > lim) p.x = lim;
                        if (p.y < -lim) p.y = -lim; else if (p.y > lim) p.y = lim;
                        moving.setOffset(Vec.sub(new Vec(p.x, p.y, movHt), moving.mouseOffs));
                        lastX = nx;
                        lastY = ny;
                        drawn = false;
                    }
                }
            } else if (leftButtonPressed || rightButtonPressed || midButtonPressed) {
                int nx = getMouseX();
                int ny = getMouseY();
                lastX = nx;
                lastY = ny;
                Render.PointedAt pa = rend.getObjectPointedAt(objects, nx, ny);
                if (pa != null && pa.ro.getClass().equals(Jig.class)) {
                    Jig ji = (Jig) pa.ro;
                    if (!lastRotateButtonPressed && (rightButtonPressed || midButtonPressed)) {
                        addRotator(ji, rotators, objects, all, redrawJigs, Vec.sub(pa.pos, ji.getOffset()), rightButtonPressed);
                        drawn = false;
                    } else {
                        moving = ji;
                        moving.mouseOffs = Vec.sub(pa.pos, moving.getOffset());
                        drawn = false;
                    }
                } else if (rotators.size() == 1 && !lastRotateButtonPressed && (rightButtonPressed || midButtonPressed)) {
                    addRotator(((Rotator) rotators.elementAt(0)).moving, rotators, objects, all, redrawJigs, new Vec(0, 0, 0), rightButtonPressed);
                    drawn = false;
                }
            }
            lastRotateButtonPressed = rightButtonPressed || midButtonPressed;
            if (moving != null && redrawJigs.indexOf(moving, 0) < 0) {
                redrawJigs.addElement(moving);
            }
            if (completedAnim > 0) {
                Jig d = (Jig) objects.elementAt(0);
                if (completed(d, rend, texture, textureBright, completedAnim - 2)) {
                    drawn = false;
                    redrawJigs.addElement(d);
                }
            } else if (moving == null && rotators.size() == 0 && objects.size() == 1) {
                Jig d = (Jig) objects.elementAt(0);
                if (d.orien != 0) {
                    addRotator(d, rotators, objects, all, redrawJigs, new Vec(0, 0, 0), true);
                } else {
                    completedAnim = 1;
                    Vec cen = d.getCentre();
                    cen.z = 0.0f;
                    d.offsetAll(new Vec(-cen.x, -cen.y, 0.0f), 1.00f);
                    d.setOffset(Vec.add(d.getOffset(), cen));
                }
            }
            if (!drawn || (!rend.getAntiAlias() && !(rightButtonPressed || midButtonPressed) && !leftButtonPressed)) {
                if (redrawJigs.size() > 0) {
                    if (rend.getAntiAlias()) {
                        rend.setAntiAlias(false);
                        rend.draw(objects, image);
                    } else {
                        rend.redraw(objects, redrawJigs, image);
                    }
                    refreshCount = 15;
                } else {
                    rend.setAntiAlias(true);
                    rend.draw(objects, image);
                    refreshCount = 15;
                }
                drawn = true;
            }
            if (((++refreshCount) & 15) == 0) {
                update(image.pix);
            }
            final int refreshPeriod = 30;
            long unt = timeStart + refreshPeriod;
            long t2 = System.currentTimeMillis();
            long diff = t2 - unt;
            timeStart = t2;
            if (diff < 0) {
                try {
                    thisThread.sleep(-diff);
                    timeStart -= diff;
                } catch (java.lang.InterruptedException r) {
                }
            }
        }
    }
