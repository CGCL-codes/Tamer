    private final int drawBin(OpenGLStatesCache statesCache, OpenGLCapabilities glCaps, RenderOptions options, boolean isScissorEnabled, boolean isClipperEnabled, RenderBin bin, View view, long frameId, int nameOffset, long nanoTime, long nanoStep, RenderMode renderMode) {
        final CanvasPeer canvasPeer = getCanvasPeer();
        int triangles = 0;
        final int n = bin.size();
        for (int i = 0; i < n; i++) {
            RenderAtom<?> atom = bin.getAtom(i);
            try {
                if (renderMode == RenderMode.PICKING) GL11.glPushName(nameOffset + i);
                if (isScissorEnabled) startScissors(statesCache, atom.getScissorRect());
                if (isClipperEnabled) startClipper(statesCache, atom.getClipper(), view);
                triangles += this.renderAtom(atom, null, canvasPeer, glCaps, statesCache, view, options, nanoTime, nanoStep, renderMode, frameId);
            } catch (Throwable e) {
                e.printStackTrace();
                System.exit(0);
            } finally {
                if (renderMode == RenderMode.PICKING) {
                    GL11.glPopName();
                }
            }
        }
        if (isClipperEnabled) finishClipper(statesCache);
        return (triangles);
    }
