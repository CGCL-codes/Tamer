    public void setup(double[] init_xyz, double[] final_xyz, int n) {
        clear();
        double[] xyz1 = new double[12];
        double[] xyz2 = new double[12];
        int i;
        int j;
        if (n != 0) {
            for (i = 0; i < 3; i++) {
                xyz1[i] = init_xyz[i];
                xyz2[i] = final_xyz[i];
            }
        } else {
            return;
        }
        if (n > 1) {
            double dist;
            double maxdist;
            int id = 1;
            maxdist = 0.0;
            for (i = 1; i < n; i++) {
                for (dist = 0.0, j = 0; j < 3; j++) {
                    dist += ((xyz1[j] - init_xyz[(3 * i) + j]) * (xyz1[j] - init_xyz[(3 * i) + j]));
                }
                dist = Math.sqrt(dist);
                if (dist > maxdist) {
                    maxdist = dist;
                    id = i;
                }
            }
            for (i = 0; i < 3; i++) {
                xyz1[3 + i] = init_xyz[(3 * id) + i];
                xyz2[3 + i] = final_xyz[(3 * id) + i];
            }
        } else {
            double[] euler = new double[3];
            double[] trans = new double[3];
            for (i = 0; i < 3; i++) {
                euler[i] = 0.0f;
                trans[i] = xyz2[i] - xyz1[i];
            }
            setupEulerTranslation(euler, trans);
            return;
        }
        if (n > 2) {
            double mag;
            double maxcross;
            double[] xx = new double[3];
            double[] yy = new double[3];
            double[] cr = new double[3];
            int ic = 1;
            for (j = 0; j < 3; j++) {
                xx[j] = xyz1[3 + j] - xyz1[j];
            }
            maxcross = 0.0f;
            for (i = 1; i < n; i++) {
                for (j = 0; j < 3; j++) {
                    yy[j] = init_xyz[(3 * i) + j] - xyz1[j];
                }
                cr[0] = (xx[1] * yy[2]) - (xx[2] * yy[1]);
                cr[1] = (-xx[0] * yy[2]) + (xx[2] * yy[0]);
                cr[2] = (xx[0] * yy[1]) - (xx[1] * yy[0]);
                mag = Math.sqrt((cr[0] * cr[0]) + (cr[1] * cr[1]) + (cr[2] * cr[2]));
                if (mag > maxcross) {
                    maxcross = mag;
                    ic = i;
                }
            }
            for (i = 0; i < 3; i++) {
                xyz1[6 + i] = init_xyz[(3 * ic) + i];
                xyz2[6 + i] = final_xyz[(3 * ic) + i];
            }
        } else {
            double[] xx = new double[3];
            double[] yy = new double[3];
            xx[0] = xyz1[3 + 0] - xyz1[0];
            xx[1] = xyz1[3 + 1] - xyz1[1];
            xx[2] = xyz1[3 + 2] - xyz1[2];
            yy[0] = xx[2];
            yy[1] = xx[0];
            yy[2] = xx[1];
            xyz1[6 + 0] = yy[0] + xyz1[0];
            xyz1[6 + 1] = yy[1] + xyz1[1];
            xyz1[6 + 2] = yy[2] + xyz1[2];
            xx[0] = xyz2[3 + 0] - xyz2[0];
            xx[1] = xyz2[3 + 1] - xyz2[1];
            xx[2] = xyz2[3 + 2] - xyz2[2];
            yy[0] = xx[2];
            yy[1] = xx[0];
            yy[2] = xx[1];
            xyz2[6 + 0] = yy[0] + xyz2[0];
            xyz2[6 + 1] = yy[1] + xyz2[1];
            xyz2[6 + 2] = yy[2] + xyz2[2];
        }
        double mag;
        double dot;
        double[] xx1 = new double[3];
        double[] yy1 = new double[3];
        double[] zz1 = new double[3];
        mag = 0.0f;
        for (i = 0; i < 3; i++) {
            xx1[i] = xyz1[3 + i] - xyz1[i];
        }
        for (i = 0; i < 3; i++) {
            mag += (xx1[i] * xx1[i]);
        }
        mag = Math.sqrt(mag);
        for (i = 0; i < 3; i++) {
            xx1[i] /= mag;
            xyz1[3 + i] = xx1[i] + xyz1[i];
        }
        dot = 0.0f;
        for (i = 0; i < 3; i++) {
            yy1[i] = xyz1[6 + i] - xyz1[i];
        }
        for (i = 0; i < 3; i++) {
            dot += (xx1[i] * yy1[i]);
        }
        for (i = 0; i < 3; i++) {
            yy1[i] -= (xx1[i] * dot);
        }
        mag = 0.0f;
        for (i = 0; i < 3; i++) {
            mag += (yy1[i] * yy1[i]);
        }
        mag = Math.sqrt(mag);
        for (i = 0; i < 3; i++) {
            yy1[i] /= mag;
            xyz1[6 + i] = yy1[i] + xyz1[i];
        }
        zz1[0] = (xx1[1] * yy1[2]) - (xx1[2] * yy1[1]);
        zz1[1] = (-xx1[0] * yy1[2]) + (xx1[2] * yy1[0]);
        zz1[2] = (xx1[0] * yy1[1]) - (xx1[1] * yy1[0]);
        for (i = 0; i < 3; i++) {
            xyz1[9 + i] = zz1[i] + xyz1[i];
        }
        double[] xx2 = new double[3];
        double[] yy2 = new double[3];
        double[] zz2 = new double[3];
        mag = 0.0f;
        for (i = 0; i < 3; i++) {
            xx2[i] = xyz2[3 + i] - xyz2[i];
        }
        for (i = 0; i < 3; i++) {
            mag += (xx2[i] * xx2[i]);
        }
        mag = Math.sqrt(mag);
        for (i = 0; i < 3; i++) {
            xx2[i] /= mag;
            xyz2[3 + i] = xx2[i] + xyz2[i];
        }
        dot = 0.0f;
        for (i = 0; i < 3; i++) {
            yy2[i] = xyz2[6 + i] - xyz2[i];
        }
        for (i = 0; i < 3; i++) {
            dot += (xx2[i] * yy2[i]);
        }
        for (i = 0; i < 3; i++) {
            yy2[i] -= (xx2[i] * dot);
        }
        mag = 0.0f;
        for (i = 0; i < 3; i++) {
            mag += (yy2[i] * yy2[i]);
        }
        mag = Math.sqrt(mag);
        for (i = 0; i < 3; i++) {
            yy2[i] /= mag;
            xyz2[6 + i] = yy2[i] + xyz2[i];
        }
        zz2[0] = (xx2[1] * yy2[2]) - (xx2[2] * yy2[1]);
        zz2[1] = (-xx2[0] * yy2[2]) + (xx2[2] * yy2[0]);
        zz2[2] = (xx2[0] * yy2[1]) - (xx2[1] * yy2[0]);
        for (i = 0; i < 3; i++) {
            xyz2[9 + i] = zz2[i] + xyz2[i];
        }
        CoordinateTransformation cti = new CoordinateTransformation();
        CoordinateTransformation ctf = new CoordinateTransformation();
        cti.setup(xyz1);
        ctf.setup(xyz2);
        cti.invert();
        this.set(cti.add(ctf));
    }
