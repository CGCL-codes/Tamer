    private final void startClipper(OpenGLStatesCache statesCache, ClipperInfo info, View view) {
        final Clipper clipper = (info == null) ? null : info.getClipper();
        if ((clipper != null) && (clipper.isEnabled())) {
            if (clipper.getId() == activeClipperId) return;
            activeClipperId = clipper.getId();
            if (lastClipperId != clipper.getId()) {
                GL11.glPushMatrix();
                GL11.glLoadMatrix(_SG_PrivilegedAccess.getFloatBuffer(view.getModelViewTransform(false), false));
                if (!clipper.isWorldCoordinateSystemUsed()) {
                    final Transform3D modelView = info.getModelView();
                    if (modelView != null) {
                        GL11.glMultMatrix(_SG_PrivilegedAccess.getFloatBuffer(modelView, true));
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
                        GL11.glClipPlane(glI, planeBuffer);
                    }
                    if (!statesCache.enabled || !statesCache.clipPlaneEnabled[i]) {
                        GL11.glEnable(glI);
                        statesCache.clipPlaneEnabled[i] = true;
                    }
                } else {
                    if (!statesCache.enabled || statesCache.clipPlaneEnabled[i]) {
                        GL11.glDisable(glI);
                        statesCache.clipPlaneEnabled[i] = false;
                    }
                }
            }
            if (lastClipperId != clipper.getId()) {
                GL11.glPopMatrix();
                lastClipperId = clipper.getId();
            }
        } else {
            finishClipper(statesCache);
        }
    }
