    public DelaunayFast(float[][] samples) throws VisADException {
        if (samples.length < 2 || samples.length > 3) {
            throw new VisADException("DelaunayFast: dimension must be 2 or 3");
        }
        if (samples.length == 3) {
            throw new UnimplementedException("DelaunayFast: " + "only two dimensions for now");
        }
        int numpts = Math.min(samples[0].length, samples[1].length);
        if (numpts < 3) {
            throw new VisADException("DelaunayFast: triangulation is " + "futile with less than 3 samples");
        }
        float[][] samp = new float[2][numpts];
        System.arraycopy(samples[0], 0, samp[0], 0, numpts);
        System.arraycopy(samples[1], 0, samp[1], 0, numpts);
        float[] samp0 = samp[0];
        float[] samp1 = samp[1];
        double cosrot = Math.cos(ROTATE);
        double sinrot = Math.sin(ROTATE);
        for (int i = 0; i < numpts; i++) {
            double x = samp0[i];
            double y = samp1[i];
            samp0[i] = (float) (x * cosrot - y * sinrot);
            samp1[i] = (float) (y * cosrot + x * sinrot);
        }
        int ntris = 0;
        int tsize = (int) (2f / 3f * numpts) + 10;
        int[][][] tris = new int[tsize][3][];
        int tp = 0;
        int[] nverts = new int[numpts];
        for (int i = 0; i < numpts; i++) nverts[i] = 0;
        int ssize = 20;
        int[] ss = new int[ssize + 2];
        int[] se = new int[ssize + 2];
        boolean[] vh = new boolean[ssize + 2];
        boolean[] mp = new boolean[ssize + 2];
        int sp = 0;
        int hsize = 10;
        int[][] hs = new int[hsize + 2][];
        int hsp = 0;
        int[] indices = new int[numpts];
        for (int i = 0; i < numpts; i++) indices[i] = i;
        sp++;
        ss[0] = 0;
        se[0] = numpts - 1;
        vh[0] = false;
        mp[0] = false;
        int css;
        int cse;
        boolean cvh;
        boolean cmp;
        while (sp != 0) {
            if (hsp > hsize) {
                hsize += hsize;
                int newhs[][] = new int[hsize + 2][];
                System.arraycopy(hs, 0, newhs, 0, hs.length);
                hs = newhs;
            }
            if (sp > ssize) {
                ssize += ssize;
                int[] newss = new int[ssize + 2];
                int[] newse = new int[ssize + 2];
                boolean[] newvh = new boolean[ssize + 2];
                boolean[] newmp = new boolean[ssize + 2];
                System.arraycopy(ss, 0, newss, 0, ss.length);
                System.arraycopy(se, 0, newse, 0, se.length);
                System.arraycopy(vh, 0, newvh, 0, vh.length);
                System.arraycopy(mp, 0, newmp, 0, mp.length);
                ss = newss;
                se = newse;
                vh = newvh;
                mp = newmp;
            }
            sp--;
            css = ss[sp];
            cse = se[sp];
            cvh = vh[sp];
            cmp = mp[sp];
            if (!cmp) {
                if (cse - css >= 3) {
                    qsort(indices, samp, cvh ? 0 : 1, css, cse);
                    ss[sp] = css;
                    se[sp] = cse;
                    vh[sp] = cvh;
                    mp[sp] = true;
                    sp++;
                    int mid = (css + cse) / 2;
                    ss[sp] = css;
                    se[sp] = mid;
                    vh[sp] = !cvh;
                    mp[sp] = false;
                    sp++;
                    ss[sp] = mid + 1;
                    se[sp] = cse;
                    vh[sp] = !cvh;
                    mp[sp] = false;
                    sp++;
                } else {
                    int[] hull;
                    if (cse - css + 1 == 3) {
                        hull = new int[3];
                        hull[0] = indices[css];
                        hull[1] = indices[css + 1];
                        hull[2] = indices[cse];
                        float a0x = samp0[hull[0]];
                        float a0y = samp1[hull[0]];
                        if ((samp0[hull[1]] - a0x) * (samp1[hull[2]] - a0y) - (samp1[hull[1]] - a0y) * (samp0[hull[2]] - a0x) > 0) {
                            hull[1] = indices[cse];
                            hull[2] = indices[css + 1];
                        }
                        tris[tp][0] = new int[1];
                        tris[tp][1] = new int[1];
                        tris[tp][2] = new int[1];
                        tris[tp][0][0] = hull[0];
                        tris[tp][1][0] = hull[1];
                        tris[tp][2][0] = hull[2];
                        tp++;
                        ntris++;
                        nverts[indices[css]]++;
                        nverts[indices[cse]]++;
                        nverts[indices[css + 1]]++;
                    } else {
                        hull = new int[2];
                        hull[0] = indices[css];
                        hull[1] = indices[cse];
                    }
                    hs[hsp++] = hull;
                }
            } else {
                int coord = cvh ? 1 : 0;
                int[] hull1, hull2;
                hsp -= 2;
                hull2 = cvh ? hs[hsp + 1] : hs[hsp];
                hull1 = cvh ? hs[hsp] : hs[hsp + 1];
                hs[hsp + 1] = null;
                hs[hsp] = null;
                int upp1 = 0;
                int upp2 = 0;
                int low1 = 0;
                int low2 = 0;
                for (int i = 1; i < hull1.length; i++) {
                    if (samp[coord][hull1[i]] > samp[coord][hull1[upp1]]) upp1 = i;
                    if (samp[coord][hull1[i]] < samp[coord][hull1[low1]]) low1 = i;
                }
                for (int i = 1; i < hull2.length; i++) {
                    if (samp[coord][hull2[i]] > samp[coord][hull2[upp2]]) upp2 = i;
                    if (samp[coord][hull2[i]] < samp[coord][hull2[low2]]) low2 = i;
                }
                for (int t = 0; t < 3; t++) {
                    int bob = (upp1 + 1) % hull1.length;
                    float ax = samp0[hull2[upp2]];
                    float ay = samp1[hull2[upp2]];
                    float bamx = samp0[hull1[bob]] - ax;
                    float bamy = samp1[hull1[bob]] - ay;
                    float camx = samp0[hull1[upp1]] - ax;
                    float camy = samp1[hull1[upp1]] - ay;
                    float u = (cvh) ? (float) (bamy / Math.sqrt(bamx * bamx + bamy * bamy)) : (float) (bamx / Math.sqrt(bamx * bamx + bamy * bamy));
                    float v = (cvh) ? (float) (camy / Math.sqrt(camx * camx + camy * camy)) : (float) (camx / Math.sqrt(camx * camx + camy * camy));
                    boolean plus_dir = (u < v);
                    if (!plus_dir) {
                        bob = upp1;
                        u = 0;
                        v = 1;
                    }
                    while (u < v) {
                        upp1 = bob;
                        bob = plus_dir ? (upp1 + 1) % hull1.length : (upp1 + hull1.length - 1) % hull1.length;
                        bamx = samp0[hull1[bob]] - ax;
                        bamy = samp1[hull1[bob]] - ay;
                        camx = samp0[hull1[upp1]] - ax;
                        camy = samp1[hull1[upp1]] - ay;
                        u = (cvh) ? (float) (bamy / Math.sqrt(bamx * bamx + bamy * bamy)) : (float) (bamx / Math.sqrt(bamx * bamx + bamy * bamy));
                        v = (cvh) ? (float) (camy / Math.sqrt(camx * camx + camy * camy)) : (float) (camx / Math.sqrt(camx * camx + camy * camy));
                    }
                    bob = (upp2 + 1) % hull2.length;
                    ax = samp0[hull1[upp1]];
                    ay = samp1[hull1[upp1]];
                    bamx = samp0[hull2[bob]] - ax;
                    bamy = samp1[hull2[bob]] - ay;
                    camx = samp0[hull2[upp2]] - ax;
                    camy = samp1[hull2[upp2]] - ay;
                    u = (cvh) ? (float) (bamy / Math.sqrt(bamx * bamx + bamy * bamy)) : (float) (bamx / Math.sqrt(bamx * bamx + bamy * bamy));
                    v = (cvh) ? (float) (camy / Math.sqrt(camx * camx + camy * camy)) : (float) (camx / Math.sqrt(camx * camx + camy * camy));
                    plus_dir = (u < v);
                    if (!plus_dir) {
                        bob = upp2;
                        u = 0;
                        v = 1;
                    }
                    while (u < v) {
                        upp2 = bob;
                        bob = plus_dir ? (upp2 + 1) % hull2.length : (upp2 + hull2.length - 1) % hull2.length;
                        bamx = samp0[hull2[bob]] - ax;
                        bamy = samp1[hull2[bob]] - ay;
                        camx = samp0[hull2[upp2]] - ax;
                        camy = samp1[hull2[upp2]] - ay;
                        u = (cvh) ? (float) (bamy / Math.sqrt(bamx * bamx + bamy * bamy)) : (float) (bamx / Math.sqrt(bamx * bamx + bamy * bamy));
                        v = (cvh) ? (float) (camy / Math.sqrt(camx * camx + camy * camy)) : (float) (camx / Math.sqrt(camx * camx + camy * camy));
                    }
                    bob = (low1 + 1) % hull1.length;
                    ax = samp0[hull2[low2]];
                    ay = samp1[hull2[low2]];
                    bamx = samp0[hull1[bob]] - ax;
                    bamy = samp1[hull1[bob]] - ay;
                    camx = samp0[hull1[low1]] - ax;
                    camy = samp1[hull1[low1]] - ay;
                    u = (cvh) ? (float) (bamy / Math.sqrt(bamx * bamx + bamy * bamy)) : (float) (bamx / Math.sqrt(bamx * bamx + bamy * bamy));
                    v = (cvh) ? (float) (camy / Math.sqrt(camx * camx + camy * camy)) : (float) (camx / Math.sqrt(camx * camx + camy * camy));
                    plus_dir = (u > v);
                    if (!plus_dir) {
                        bob = low1;
                        u = 1;
                        v = 0;
                    }
                    while (u > v) {
                        low1 = bob;
                        bob = plus_dir ? (low1 + 1) % hull1.length : (low1 + hull1.length - 1) % hull1.length;
                        bamx = samp0[hull1[bob]] - ax;
                        bamy = samp1[hull1[bob]] - ay;
                        camx = samp0[hull1[low1]] - ax;
                        camy = samp1[hull1[low1]] - ay;
                        u = (cvh) ? (float) (bamy / Math.sqrt(bamx * bamx + bamy * bamy)) : (float) (bamx / Math.sqrt(bamx * bamx + bamy * bamy));
                        v = (cvh) ? (float) (camy / Math.sqrt(camx * camx + camy * camy)) : (float) (camx / Math.sqrt(camx * camx + camy * camy));
                    }
                    bob = (low2 + 1) % hull2.length;
                    ax = samp0[hull1[low1]];
                    ay = samp1[hull1[low1]];
                    bamx = samp0[hull2[bob]] - ax;
                    bamy = samp1[hull2[bob]] - ay;
                    camx = samp0[hull2[low2]] - ax;
                    camy = samp1[hull2[low2]] - ay;
                    u = (cvh) ? (float) (bamy / Math.sqrt(bamx * bamx + bamy * bamy)) : (float) (bamx / Math.sqrt(bamx * bamx + bamy * bamy));
                    v = (cvh) ? (float) (camy / Math.sqrt(camx * camx + camy * camy)) : (float) (camx / Math.sqrt(camx * camx + camy * camy));
                    plus_dir = (u > v);
                    if (!plus_dir) {
                        bob = low2;
                        u = 1;
                        v = 0;
                    }
                    while (u > v) {
                        low2 = bob;
                        bob = plus_dir ? (low2 + 1) % hull2.length : (low2 + hull2.length - 1) % hull2.length;
                        bamx = samp0[hull2[bob]] - ax;
                        bamy = samp1[hull2[bob]] - ay;
                        camx = samp0[hull2[low2]] - ax;
                        camy = samp1[hull2[low2]] - ay;
                        u = (cvh) ? (float) (bamy / Math.sqrt(bamx * bamx + bamy * bamy)) : (float) (bamx / Math.sqrt(bamx * bamx + bamy * bamy));
                        v = (cvh) ? (float) (camy / Math.sqrt(camx * camx + camy * camy)) : (float) (camx / Math.sqrt(camx * camx + camy * camy));
                    }
                }
                int nih1, nih2;
                int noh1, noh2;
                int h1ups, h2ups;
                if (low1 == upp1) {
                    nih1 = hull1.length;
                    noh1 = 1;
                    h1ups = 0;
                } else {
                    nih1 = low1 - upp1 + 1;
                    if (nih1 <= 0) nih1 += hull1.length;
                    noh1 = hull1.length - nih1 + 2;
                    h1ups = 1;
                }
                if (low2 == upp2) {
                    nih2 = hull2.length;
                    noh2 = 1;
                    h2ups = 0;
                } else {
                    nih2 = upp2 - low2 + 1;
                    if (nih2 <= 0) nih2 += hull2.length;
                    noh2 = hull2.length - nih2 + 2;
                    h2ups = 1;
                }
                int[] hull = new int[noh1 + noh2];
                int hullnum = 0;
                int spot;
                for (spot = low1; spot != upp1; hullnum++, spot = (spot + 1) % hull1.length) {
                    hull[hullnum] = hull1[spot];
                }
                hull[hullnum++] = hull1[upp1];
                for (spot = upp2; spot != low2; hullnum++, spot = (spot + 1) % hull2.length) {
                    hull[hullnum] = hull2[spot];
                }
                hull[hullnum++] = hull2[low2];
                hs[hsp++] = hull;
                int base1 = low1;
                int base2 = low2;
                int oneUp1 = (base1 + hull1.length - 1) % hull1.length;
                int oneUp2 = (base2 + 1) % hull2.length;
                int ntd = (noh1 == 1 || noh2 == 1) ? nih1 + nih2 - 1 : nih1 + nih2 - 2;
                tris[tp][0] = new int[ntd];
                tris[tp][1] = new int[ntd];
                tris[tp][2] = new int[ntd];
                for (int t = 0; t < ntd; t++) {
                    if (h1ups == nih1) {
                        oneUp2 = (base2 + 1) % hull2.length;
                        tris[tp][0][t] = hull2[base2];
                        tris[tp][1][t] = hull1[base1];
                        tris[tp][2][t] = hull2[oneUp2];
                        ntris++;
                        nverts[hull1[base1]]++;
                        nverts[hull2[base2]]++;
                        nverts[hull2[oneUp2]]++;
                        base2 = oneUp2;
                        h2ups++;
                    } else if (h2ups == nih2) {
                        oneUp1 = (base1 + hull1.length - 1) % hull1.length;
                        tris[tp][0][t] = hull2[base2];
                        tris[tp][1][t] = hull1[base1];
                        tris[tp][2][t] = hull1[oneUp1];
                        ntris++;
                        nverts[hull1[base1]]++;
                        nverts[hull2[base2]]++;
                        nverts[hull1[oneUp1]]++;
                        base1 = oneUp1;
                        h1ups++;
                    } else {
                        boolean d;
                        int hb1 = hull1[base1];
                        int ho1 = hull1[oneUp1];
                        int hb2 = hull2[base2];
                        int ho2 = hull2[oneUp2];
                        float ax = samp0[ho2];
                        float ay = samp1[ho2];
                        float bx = samp0[hb2];
                        float by = samp1[hb2];
                        float cx = samp0[ho1];
                        float cy = samp1[ho1];
                        float dx = samp0[hb1];
                        float dy = samp1[hb1];
                        float abx = ax - bx;
                        float aby = ay - by;
                        float acx = ax - cx;
                        float acy = ay - cy;
                        float dbx = dx - bx;
                        float dby = dy - by;
                        float dcx = dx - cx;
                        float dcy = dy - cy;
                        float Q = abx * acx + aby * acy;
                        float R = dbx * abx + dby * aby;
                        float S = acx * dcx + acy * dcy;
                        float T = dbx * dcx + dby * dcy;
                        boolean QD = abx * acy - aby * acx >= 0;
                        boolean RD = dbx * aby - dby * abx >= 0;
                        boolean SD = acx * dcy - acy * dcx >= 0;
                        boolean TD = dcx * dby - dcy * dbx >= 0;
                        boolean sig = (QD ? 1 : 0) + (RD ? 1 : 0) + (SD ? 1 : 0) + (TD ? 1 : 0) < 2;
                        if (QD == sig) d = true; else if (RD == sig) d = false; else if (SD == sig) d = false; else if (TD == sig) d = true; else if (Q < 0 && T < 0 || R > 0 && S > 0) d = true; else if (R < 0 && S < 0 || Q > 0 && T > 0) d = false; else if ((Q < 0 ? Q : T) < (R < 0 ? R : S)) d = true; else d = false;
                        if (d) {
                            tris[tp][0][t] = hull2[base2];
                            tris[tp][1][t] = hull1[base1];
                            tris[tp][2][t] = hull2[oneUp2];
                            ntris++;
                            nverts[hull1[base1]]++;
                            nverts[hull2[base2]]++;
                            nverts[hull2[oneUp2]]++;
                            base2 = oneUp2;
                            h2ups++;
                            oneUp2 = (base2 + 1) % hull2.length;
                        } else {
                            tris[tp][0][t] = hull2[base2];
                            tris[tp][1][t] = hull1[base1];
                            tris[tp][2][t] = hull1[oneUp1];
                            ntris++;
                            nverts[hull1[base1]]++;
                            nverts[hull2[base2]]++;
                            nverts[hull1[oneUp1]]++;
                            base1 = oneUp1;
                            h1ups++;
                            oneUp1 = (base1 + hull1.length - 1) % hull1.length;
                        }
                    }
                }
                tp++;
            }
        }
        Tri = new int[ntris][3];
        int tr = 0;
        for (int i = 0; i < tp; i++) {
            for (int j = 0; j < tris[i][0].length; j++) {
                Tri[tr][0] = tris[i][0][j];
                Tri[tr][1] = tris[i][1][j];
                Tri[tr][2] = tris[i][2][j];
                tr++;
            }
        }
        Vertices = new int[numpts][];
        for (int i = 0; i < numpts; i++) {
            Vertices[i] = new int[nverts[i]];
            nverts[i] = 0;
        }
        int a, b, c;
        for (int i = 0; i < ntris; i++) {
            a = Tri[i][0];
            b = Tri[i][1];
            c = Tri[i][2];
            Vertices[a][nverts[a]++] = i;
            Vertices[b][nverts[b]++] = i;
            Vertices[c][nverts[c]++] = i;
        }
        finish_triang(samples);
    }
