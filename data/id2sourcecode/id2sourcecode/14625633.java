    public void draw(LineObject line) {
        double[] x = sx.scale(line.XData.getArray());
        double[] y = sy.scale(line.YData.getArray());
        double[] z = sz.scale(line.ZData.getArray());
        int n = Math.min(Math.min(x.length, y.length), (z.length == 0 ? Integer.MAX_VALUE : z.length));
        int[] clip = new int[n];
        int clipMask = (line.Clipping.isSet() ? 0x7F : 0x40);
        if (z.length == 0) {
            double zmid = (zmin + zmax) / 2;
            for (int i = 0; i < n; i++) clip[i] = (clipCode(x[i], y[i], zmid) & clipMask);
        } else for (int i = 0; i < n; i++) clip[i] = (clipCode(x[i], y[i], z[i]) & clipMask);
        if (line.LineStyle.isSet()) {
            setColor(line.LineColor.getColor());
            setLineStyle(line.LineStyle.getValue(), false);
            setLineWidth(line.LineWidth.floatValue());
            if (z.length == 0) {
                boolean flag = false;
                for (int i = 1; i < n; i++) {
                    if ((clip[i - 1] & clip[i]) == 64) {
                        if (!flag) {
                            flag = true;
                            gl.glBegin(GL.GL_LINE_STRIP);
                            gl.glVertex2d(x[i - 1], y[i - 1]);
                        }
                        gl.glVertex2d(x[i], y[i]);
                    } else if (flag) {
                        gl.glEnd();
                        flag = false;
                    }
                }
                if (flag) gl.glEnd();
            } else {
                boolean flag = false;
                for (int i = 1; i < n; i++) {
                    if ((clip[i - 1] & clip[i]) == 64) {
                        if (!flag) {
                            flag = true;
                            gl.glBegin(GL.GL_LINE_STRIP);
                            gl.glVertex3d(x[i - 1], y[i - 1], z[i - 1]);
                        }
                        gl.glVertex3d(x[i], y[i], z[i]);
                    } else if (flag) {
                        gl.glEnd();
                        flag = false;
                    }
                }
                if (flag) gl.glEnd();
            }
            setLineWidth(0.5f);
            setLineStyle("-", false);
        }
        if (line.Marker.isSet() && false) {
            MarkerProperty.Marker m = line.Marker.makeMarker(line.MarkerSize.doubleValue(), line.LineWidth.doubleValue());
            int w = m.w, h = m.h, xhot = m.xhot, yhot = m.yhot;
            byte[] data = m.data;
            gl.glPixelStorei(GL.GL_UNPACK_ALIGNMENT, 1);
            setColor(line.LineColor.getColor());
            if (z.length == 0) {
                for (int i = 0; i < n; i++) {
                    if (clip[i] == 64) {
                        gl.glRasterPos2d(x[i], y[i]);
                        gl.glBitmap(0, 0, 0, 0, -xhot, -yhot, null, 0);
                        gl.glBitmap(w, h, 0, 0, 0, 0, data, 0);
                    }
                }
            } else {
                for (int i = 0; i < n; i++) {
                    if (clip[i] == 64) {
                        gl.glRasterPos3d(x[i], y[i], z[i]);
                        gl.glBitmap(0, 0, 0, 0, -xhot, -yhot, null, 0);
                        gl.glBitmap(w, h, 0, 0, 0, 0, data, 0);
                    }
                }
            }
            gl.glPixelStorei(GL.GL_UNPACK_ALIGNMENT, 4);
        } else if (line.Marker.isSet() && !(line.MarkerEdgeColor.is("none") && line.MarkerFaceColor.is("none"))) {
            Color lc = null, fc = null;
            if (line.MarkerEdgeColor.isSet()) lc = line.MarkerEdgeColor.getColor(); else if (line.MarkerEdgeColor.is("auto")) lc = line.LineColor.getColor();
            if (line.MarkerFaceColor.isSet()) fc = line.MarkerFaceColor.getColor(); else if (line.MarkerFaceColor.is("auto")) fc = line.LineColor.getColor();
            drawMarkers(line.Marker, line.MarkerSize, new double[][] { x, y, z }, clip, n, lc, line.LineWidth.floatValue(), fc);
        }
    }
