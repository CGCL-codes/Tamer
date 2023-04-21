    private final void startClipper(GL gl, OpenGLStatesCache statesCache, ClipperInfo info, View view) {
        final Clipper clipper = (info == null) ? null : info.getClipper();
        if ((clipper != null) && (clipper.isEnabled())) {
            if (clipper.getId() == activeClipperId) return;
            activeClipperId = clipper.getId();
            if (lastClipperId != clipper.getId()) {
                gl.getGL2().glPushMatrix();
                gl.getGL2().glLoadMatrixf(_SG_PrivilegedAccess.getFloatBuffer(view.getModelViewTransform(false), true));
                if (!clipper.isWorldCoordinateSystemUsed()) {
                    final Transform3D modelView = info.getModelView();
                    if (modelView != null) {
                        float[] matrix = new float[16];
                        modelView.getColumnMajor(matrix);
                        gl.getGL2().glMultMatrixf(matrix, 0);
                    }
                }
            }
            for (int i = 0; i < 6; i++) {
                final int glI = translateClipPlaneIndex(i);
                if (clipper.isPlaneEnabled(i)) {
                    if (lastClipperId != clipper.getId()) {
                        planeBuffer.clear();
                        planeBuffer.put(clipper.getPlane(i).getA());
                        planeBuffer.put(clipper.getPlane(i).getB());
                        planeBuffer.put(clipper.getPlane(i).getC());
                        planeBuffer.put(clipper.getPlane(i).getD());
                        planeBuffer.rewind();
                        gl.getGL2().glClipPlane(glI, planeBuffer);
                    }
                    if (!statesCache.enabled || !statesCache.clipPlaneEnabled[i]) {
                        gl.glEnable(glI);
                        statesCache.clipPlaneEnabled[i] = true;
                    }
                } else {
                    if (!statesCache.enabled || statesCache.clipPlaneEnabled[i]) {
                        gl.glDisable(glI);
                        statesCache.clipPlaneEnabled[i] = false;
                    }
                }
            }
            if (lastClipperId != clipper.getId()) {
                gl.getGL2().glPopMatrix();
                lastClipperId = clipper.getId();
            }
        } else {
            finishClipper(gl, statesCache);
        }
    }
