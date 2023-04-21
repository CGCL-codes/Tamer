    private void svd(float[][] mat, float[] s, float[][] u, float[][] v, float progStop, boolean noProg) {
        final int m = mat.length;
        final int n = mat[0].length;
        final int nu = Math.min(m, n);
        final int ns = Math.min(m + 1, n);
        final boolean wantu = u != null;
        final boolean wantv = v != null;
        if ((s.length != ns) || (wantu && ((u.length != m) || (u[0].length != nu))) || (wantv && ((v.length != n) || (v[0].length != n)))) throw new IllegalArgumentException();
        final float[] e = new float[n];
        final float[] work = new float[m];
        final float eps = (float) Math.pow(2.0, -48.0);
        final float tiny = (float) Math.pow(2.0, -120.0);
        final int nct = Math.min(m - 1, n);
        final int nrt = Math.max(0, Math.min(n - 2, m));
        final int nk = Math.max(nct, nrt);
        final int np = Math.min(n, m + 1);
        final float progOff = getProgression();
        final float progW = (progStop - progOff) / (((long) nk * (nk + 1) / 2) + ((long) np * (np + 1) / 2) + (wantu ? ((long) nct * (nct + 1) / 2) : 0) + (wantv ? n : 0));
        long progC = 0;
        for (int k = 0; (k < nk) && (noProg || threadRunning); k++) {
            if (k < nct) {
                s[k] = 0;
                for (int i = k; i < m; i++) {
                    s[k] = hypot(s[k], mat[i][k]);
                }
                if (s[k] != 0.0f) {
                    if (mat[k][k] < 0.0f) {
                        s[k] = -s[k];
                    }
                    for (int i = k; i < m; i++) {
                        mat[i][k] /= s[k];
                    }
                    mat[k][k] += 1.0f;
                }
                s[k] = -s[k];
            }
            for (int j = k + 1; j < n; j++) {
                if ((k < nct) && (s[k] != 0.0f)) {
                    float t = 0;
                    for (int i = k; i < m; i++) {
                        t += mat[i][k] * mat[i][j];
                    }
                    t = -t / mat[k][k];
                    for (int i = k; i < m; i++) {
                        mat[i][j] += t * mat[i][k];
                    }
                }
                e[j] = mat[k][j];
            }
            if (wantu && (k < nct)) {
                for (int i = k; i < m; i++) {
                    u[i][k] = mat[i][k];
                }
            }
            if (k < nrt) {
                e[k] = 0;
                for (int i = k + 1; i < n; i++) {
                    e[k] = hypot(e[k], e[i]);
                }
                if (e[k] != 0.0f) {
                    if (e[k + 1] < 0.0f) {
                        e[k] = -e[k];
                    }
                    for (int i = k + 1; i < n; i++) {
                        e[i] /= e[k];
                    }
                    e[k + 1] += 1.0f;
                }
                e[k] = -e[k];
                if (((k + 1) < m) && (e[k] != 0.0f)) {
                    for (int i = k + 1; i < m; i++) {
                        work[i] = 0.0f;
                    }
                    for (int j = k + 1; j < n; j++) {
                        for (int i = k + 1; i < m; i++) {
                            work[i] += e[j] * mat[i][j];
                        }
                    }
                    for (int j = k + 1; j < n; j++) {
                        final float t = -e[j] / e[k + 1];
                        for (int i = k + 1; i < m; i++) {
                            mat[i][j] += t * work[i];
                        }
                    }
                }
                if (wantv) {
                    for (int i = k + 1; i < n; i++) {
                        v[i][k] = e[i];
                    }
                }
            }
            progC += (nk - k);
            setProgression(progC * progW + progOff);
        }
        if (!(noProg || threadRunning)) return;
        int p = np;
        if (nct < n) {
            s[nct] = mat[nct][nct];
        }
        if (m < p) {
            s[p - 1] = 0.0f;
        }
        if ((nrt + 1) < p) {
            e[nrt] = mat[nrt][p - 1];
        }
        e[p - 1] = 0.0f;
        if (wantu) {
            for (int j = nct; j < nu; j++) {
                for (int i = 0; i < m; i++) {
                    u[i][j] = 0.0f;
                }
                u[j][j] = 1.0f;
            }
            for (int k = nct - 1; k >= 0; k--) {
                if (s[k] != 0.0f) {
                    for (int j = k + 1; j < nu; j++) {
                        float t = 0;
                        for (int i = k; i < m; i++) {
                            t += u[i][k] * u[i][j];
                        }
                        t = -t / u[k][k];
                        for (int i = k; i < m; i++) {
                            u[i][j] += t * u[i][k];
                        }
                    }
                    for (int i = k; i < m; i++) {
                        u[i][k] = -u[i][k];
                    }
                    u[k][k] = 1.0f + u[k][k];
                    for (int i = 0; i < k - 1; i++) {
                        u[i][k] = 0.0f;
                    }
                } else {
                    for (int i = 0; i < m; i++) {
                        u[i][k] = 0.0f;
                    }
                    u[k][k] = 1.0f;
                }
                progC += (nct - k);
                setProgression(progC * progW + progOff);
            }
        }
        if (wantv) {
            for (int k = n - 1; k >= 0; k--) {
                if ((k < nrt) && (e[k] != 0.0f)) {
                    for (int j = k + 1; j < nu; j++) {
                        float t = 0;
                        for (int i = k + 1; i < n; i++) {
                            t += v[i][k] * v[i][j];
                        }
                        t = -t / v[k + 1][k];
                        for (int i = k + 1; i < n; i++) {
                            v[i][j] += t * v[i][k];
                        }
                    }
                }
                for (int i = 0; i < n; i++) {
                    v[i][k] = 0.0f;
                }
                v[k][k] = 1.0f;
                progC++;
                setProgression(progC * progW + progOff);
            }
        }
        final int pp = p - 1;
        int iter = 0;
        while ((p > 0) && (noProg || threadRunning)) {
            int k, kase;
            for (k = p - 2; k >= -1; k--) {
                if (k == -1) break;
                if (Math.abs(e[k]) <= tiny + eps * (Math.abs(s[k]) + Math.abs(s[k + 1]))) {
                    e[k] = 0.0f;
                    break;
                }
            }
            if (k == p - 2) {
                kase = 4;
            } else {
                int ks;
                for (ks = p - 1; ks >= k; ks--) {
                    if (ks == k) break;
                    final float t = (ks != p ? Math.abs(e[ks]) : 0.0f) + (ks != k + 1 ? Math.abs(e[ks - 1]) : 0.0f);
                    if (Math.abs(s[ks]) <= tiny + eps * t) {
                        s[ks] = 0.0f;
                        break;
                    }
                }
                if (ks == k) {
                    kase = 3;
                } else if (ks == p - 1) {
                    kase = 1;
                } else {
                    kase = 2;
                    k = ks;
                }
            }
            k++;
            switch(kase) {
                case 1:
                    {
                        float f = e[p - 2];
                        e[p - 2] = 0.0f;
                        for (int j = p - 2; j >= k; j--) {
                            final float t = hypot(s[j], f);
                            final float cs = s[j] / t;
                            final float sn = f / t;
                            s[j] = t;
                            if (j != k) {
                                f = -sn * e[j - 1];
                                e[j - 1] = cs * e[j - 1];
                            }
                            if (wantv) {
                                for (int i = 0; i < n; i++) {
                                    final float tt = cs * v[i][j] + sn * v[i][p - 1];
                                    v[i][p - 1] = -sn * v[i][j] + cs * v[i][p - 1];
                                    v[i][j] = tt;
                                }
                            }
                        }
                    }
                    break;
                case 2:
                    {
                        float f = e[k - 1];
                        e[k - 1] = 0.0f;
                        for (int j = k; j < p; j++) {
                            final float t = hypot(s[j], f);
                            final float cs = s[j] / t;
                            final float sn = f / t;
                            s[j] = t;
                            f = -sn * e[j];
                            e[j] = cs * e[j];
                            if (wantu) {
                                for (int i = 0; i < m; i++) {
                                    final float tt = cs * u[i][j] + sn * u[i][k - 1];
                                    u[i][k - 1] = -sn * u[i][j] + cs * u[i][k - 1];
                                    u[i][j] = tt;
                                }
                            }
                        }
                    }
                    break;
                case 3:
                    {
                        final float scale = Math.max(Math.max(Math.max(Math.max(Math.abs(s[p - 1]), Math.abs(s[p - 2])), Math.abs(e[p - 2])), Math.abs(s[k])), Math.abs(e[k]));
                        final float sp = s[p - 1] / scale;
                        final float spm1 = s[p - 2] / scale;
                        final float epm1 = e[p - 2] / scale;
                        final float sk = s[k] / scale;
                        final float ek = e[k] / scale;
                        final float b = ((spm1 + sp) * (spm1 - sp) + epm1 * epm1) / 2.0f;
                        final float c = (sp * epm1) * (sp * epm1);
                        final float shift;
                        if ((b != 0.0f) || (c != 0.0f)) {
                            final float t;
                            if (b >= 0.0f) {
                                t = (float) Math.sqrt(b * b + c);
                            } else {
                                t = (float) -Math.sqrt(b * b + c);
                            }
                            shift = c / (b + t);
                        } else {
                            shift = 0.0f;
                        }
                        float f = (sk + sp) * (sk - sp) + shift;
                        float g = sk * ek;
                        for (int j = k; j < (p - 1); j++) {
                            float t = hypot(f, g);
                            float cs = f / t;
                            float sn = g / t;
                            if (j != k) {
                                e[j - 1] = t;
                            }
                            f = cs * s[j] + sn * e[j];
                            e[j] = cs * e[j] - sn * s[j];
                            g = sn * s[j + 1];
                            s[j + 1] = cs * s[j + 1];
                            if (wantv) {
                                for (int i = 0; i < n; i++) {
                                    final float tt = cs * v[i][j] + sn * v[i][j + 1];
                                    v[i][j + 1] = -sn * v[i][j] + cs * v[i][j + 1];
                                    v[i][j] = tt;
                                }
                            }
                            t = hypot(f, g);
                            cs = f / t;
                            sn = g / t;
                            s[j] = t;
                            f = cs * e[j] + sn * s[j + 1];
                            s[j + 1] = -sn * e[j] + cs * s[j + 1];
                            g = sn * e[j + 1];
                            e[j + 1] = cs * e[j + 1];
                            if (wantu && (j < (m - 1))) {
                                for (int i = 0; i < m; i++) {
                                    final float tt = cs * u[i][j] + sn * u[i][j + 1];
                                    u[i][j + 1] = -sn * u[i][j] + cs * u[i][j + 1];
                                    u[i][j] = tt;
                                }
                            }
                        }
                        e[p - 2] = f;
                        iter++;
                    }
                    break;
                case 4:
                    {
                        if (s[k] <= 0.0f) {
                            s[k] = (s[k] < 0.0f ? -s[k] : 0.0f);
                            if (wantv) {
                                for (int i = 0; i <= pp; i++) {
                                    v[i][k] = -v[i][k];
                                }
                            }
                        }
                        while (k < pp) {
                            if (s[k] >= s[k + 1]) break;
                            float t = s[k];
                            s[k] = s[k + 1];
                            s[k + 1] = t;
                            if (wantv && (k < (n - 1))) {
                                for (int i = 0; i < n; i++) {
                                    t = v[i][k + 1];
                                    v[i][k + 1] = v[i][k];
                                    v[i][k] = t;
                                }
                            }
                            if (wantu && (k < (m - 1))) {
                                for (int i = 0; i < m; i++) {
                                    t = u[i][k + 1];
                                    u[i][k + 1] = u[i][k];
                                    u[i][k] = t;
                                }
                            }
                            k++;
                        }
                        iter = 0;
                        progC += p;
                        p--;
                        setProgression(progC * progW + progOff);
                    }
                    break;
            }
        }
    }
