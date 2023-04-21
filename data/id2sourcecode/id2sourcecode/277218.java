    public void main(int width, int height) {
        Pixstore image = new Pixstore(width, height);
        Render rend = new Render(this, image);
        rend.setIntersect(false);
        Pixstore texture = new Pixstore(this, "pp0.jpg", image);
        Vector objects = new Vector();
        RenderObject face = new RenderObject();
        Vector muscles = new Vector();
        Vector frames = new Vector();
        Vector fvs = new Vector();
        try {
            InputStream s = Animate.class.getResourceAsStream("pp.fm");
            InputStreamReader si = new InputStreamReader(s);
            BufferedReader br = new BufferedReader(si);
            String st = br.readLine();
            st = br.readLine();
            NumberFormat df = NumberFormat.getInstance(new Locale("en", "IE", ""));
            while (st != null) {
                st = br.readLine();
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
                    FaceVert f = new FaceVert(new Vec(xn.floatValue(), yn.floatValue(), zn.floatValue()));
                    fvs.addElement(f);
                    face.addVert(f.v);
                } else break;
            }
            while (st != null) {
                if (st.startsWith("Tri {")) {
                    st = st.substring(5, st.length() - 1);
                    int i1 = st.indexOf(',');
                    int i2 = st.indexOf(',', i1 + 1);
                    int i3 = st.indexOf(' ', i2 + 1);
                    if (i3 > 0) {
                        String xc = st.substring(0, i1);
                        String yc = st.substring(i1 + 1, i2);
                        String zc = st.substring(i2 + 1, i3);
                        Vertex av = face.getVert(df.parse(xc).intValue());
                        Vertex bv = face.getVert(df.parse(yc).intValue());
                        Vertex cv = face.getVert(df.parse(zc).intValue());
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
                        TexTri t = new TexTri(av, bv, cv, texture, new Vec2(tx1, ty1), new Vec2(tx2, ty2), new Vec2(tx3, ty3));
                        t.setShade(false);
                        face.addTri(t);
                    }
                    st = br.readLine();
                } else break;
            }
            while (st != null) {
                if (st.startsWith("MusclePoint {")) {
                    st = st.substring(14, st.length() - 1);
                    int i1 = st.indexOf(' ');
                    String name = st.substring(0, i1);
                    int i2 = st.indexOf(' ', i1 + 1);
                    i2 = st.indexOf(' ', i2 + 1);
                    FaceMusclePoint mp = new FaceMusclePoint();
                    i1 = st.indexOf(',', i2 + 1);
                    mp.point.x = df.parse(st.substring(i2 + 1, i1)).floatValue();
                    i2 = st.indexOf(',', i1 + 1);
                    mp.point.y = df.parse(st.substring(i1 + 1, i2)).floatValue();
                    i1 = st.indexOf(' ', i2 + 1);
                    mp.point.z = df.parse(st.substring(i2 + 1, i1)).floatValue();
                    while (i1 > 0) {
                        MuscleSpot ms = new MuscleSpot();
                        i2 = st.indexOf(',', i1 + 1);
                        ms.v = face.getVert(df.parse(st.substring(i1 + 1, i2)).intValue());
                        i1 = st.indexOf(' ', i2 + 1);
                        if (i1 > 0) {
                            ms.frac = df.parse(st.substring(i2 + 1, i1)).floatValue();
                        } else {
                            ms.frac = df.parse(st.substring(i2 + 1)).floatValue();
                        }
                        mp.muscleSpots.addElement(ms);
                    }
                    st = br.readLine();
                    muscles.addElement(mp);
                } else if (st.startsWith("MuscleParallel {")) {
                    st = st.substring(16, st.length() - 1);
                    int i1 = st.indexOf(' ');
                    String name = st.substring(0, i1);
                    int i2 = st.indexOf(' ', i1 + 1);
                    i2 = st.indexOf(' ', i2 + 1);
                    FaceMuscleParallel mp = new FaceMuscleParallel();
                    i1 = st.indexOf(',', i2 + 1);
                    mp.par.x = df.parse(st.substring(i2 + 1, i1)).floatValue();
                    i2 = st.indexOf(',', i1 + 1);
                    mp.par.y = df.parse(st.substring(i1 + 1, i2)).floatValue();
                    i1 = st.indexOf(' ', i2 + 1);
                    mp.par.z = df.parse(st.substring(i2 + 1, i1)).floatValue();
                    while (i1 > 0) {
                        MuscleSpot ms = new MuscleSpot();
                        i2 = st.indexOf(',', i1 + 1);
                        ms.v = face.getVert(df.parse(st.substring(i1 + 1, i2)).intValue());
                        i1 = st.indexOf(' ', i2 + 1);
                        if (i1 > 0) {
                            ms.frac = df.parse(st.substring(i2 + 1, i1)).floatValue();
                        } else {
                            ms.frac = df.parse(st.substring(i2 + 1)).floatValue();
                        }
                        mp.muscleSpots.addElement(ms);
                    }
                    st = br.readLine();
                    muscles.addElement(mp);
                } else break;
            }
            s.close();
            s = Animate.class.getResourceAsStream("dem2.fr");
            si = new InputStreamReader(s);
            br = new BufferedReader(si);
            st = br.readLine();
            while (st != null) {
                Frame fr = new Frame();
                if (st.startsWith("Frame {")) {
                    st = st.substring(st.indexOf(' ', 8) + 1, st.length() - 1);
                    int i1 = 0;
                    int i2 = st.indexOf(',', i1 + 1);
                    fr.pos.x = df.parse(st.substring(i1, i2).toUpperCase()).floatValue();
                    i1 = st.indexOf(',', i2 + 1);
                    fr.pos.y = df.parse(st.substring(i2 + 1, i1).toUpperCase()).floatValue();
                    i2 = st.indexOf(' ', i1 + 1);
                    fr.pos.z = df.parse(st.substring(i1 + 1, i2).toUpperCase()).floatValue();
                    i1 = st.indexOf(',', i2 + 1);
                    fr.z1 = df.parse(st.substring(i2 + 1, i1).toUpperCase()).floatValue();
                    i2 = st.indexOf(',', i1 + 1);
                    fr.y = df.parse(st.substring(i1 + 1, i2).toUpperCase()).floatValue();
                    fr.z2 = df.parse(st.substring(i2 + 1).toUpperCase()).floatValue();
                    st = br.readLine();
                    if (st.startsWith("  Muscles {")) {
                        st = st.substring(11, st.length() - 1);
                        fr.tenses = new float[muscles.size()];
                        i1 = 0;
                        for (int i = 0; i < muscles.size(); ++i) {
                            i2 = st.indexOf(' ', i1 + 1);
                            fr.tenses[i] = df.parse(st.substring(i1, i2).toUpperCase()).floatValue();
                            i1 = i2 + 1;
                        }
                        fr.tenses[3] = 0;
                        fr.tenses[5] = 0;
                        fr.tenses[6] = 0;
                        fr.tenses[7] = 0;
                        fr.tenses[8] = 0;
                        frames.addElement(fr);
                    }
                    st = br.readLine();
                    st = br.readLine();
                }
            }
            s.close();
        } catch (java.io.IOException i) {
            System.exit(0);
        } catch (java.text.ParseException i) {
            System.exit(0);
        }
        objects.addElement(face);
        long timeStart = System.currentTimeMillis();
        long totFrames = 0;
        Thread thisThread = Thread.currentThread();
        int lastX = 0;
        int lastY = 0;
        int fr = 0;
        Matrix rot = new Matrix();
        Matrix delta = new Matrix();
        rot.setRotationXyzProgressive(-0.9f, -0.3f, 0.1f);
        boolean firstPress = true;
        while (_thread == thisThread) {
            if (getMouseButton(RIGHTBUTTON)) {
                int nx = getMouseX();
                int ny = getMouseY();
                int xr = width >> 1;
                int yr = height >> 1;
                if (firstPress) {
                    lastX = nx;
                    lastY = ny;
                }
                float rx = (float) (nx - xr) / xr;
                float ry = (float) (ny - yr) / yr;
                nx -= lastX;
                ny -= lastY;
                float zr = nx * ry - ny * rx;
                lastX += nx;
                lastY += ny;
                firstPress = false;
                delta.setRotationXyzProgressive(-ny * (1 - Math.abs(rx)) * .03f, nx * (1 - Math.abs(ry)) * .03f, zr * .03f);
                Matrix n = Matrix.mul(rot, delta);
                Vec dir = new Vec(0, 0, 1);
                Vec nn = Matrix.mul(n, dir);
                dir.set(0.1f, -0.8f, 0.6f);
                if (Vec.dot(nn, dir) > 0.1) {
                    rot = n;
                }
            } else {
                firstPress = true;
            }
            for (int i = 0; i < face.getVertNum(); ++i) {
                FaceVert fv = (FaceVert) fvs.elementAt(i);
                fv.v.setPos(new Vec(fv.origPos.x, fv.origPos.y, fv.origPos.z));
            }
            Frame fram = (Frame) frames.elementAt(fr);
            face.setOffset(fram.pos);
            delta.setRotationZyzProgressive(fram.z1, fram.y, fram.z2);
            face.setRot(Matrix.mul(rot, delta));
            for (int i = 0; i < muscles.size(); ++i) {
                FaceMuscle fm = (FaceMuscle) muscles.elementAt(i);
                fm.tense = fram.tenses[i];
                fm.applyOffsets();
            }
            ++fr;
            if (fr == frames.size()) fr = 0;
            rend.draw(objects, image);
            update(image.pix);
            ++totFrames;
            long unt = timeStart + 40 * totFrames;
            long t2 = System.currentTimeMillis();
            long diff = t2 - unt;
            if (diff < 40) {
                try {
                    thisThread.sleep(40 - diff);
                } catch (java.lang.InterruptedException r) {
                }
            }
        }
    }
