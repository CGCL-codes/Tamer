    public void applyLUT() {
        byte[] reds2 = reds, greens2 = greens, blues2 = blues;
        if (mapSize < 256) {
            reds2 = new byte[256];
            greens2 = new byte[256];
            blues2 = new byte[256];
            for (int i = 0; i < mapSize; i++) {
                reds2[i] = reds[i];
                greens2[i] = greens[i];
                blues2[i] = blues[i];
            }
            scale(reds2, greens2, blues2, 256);
        }
        IndexColorModel cm = new IndexColorModel(8, 256, reds2, greens2, blues2);
        ImageProcessor ip = imp.getChannelProcessor();
        ip.setColorModel(cm);
        if (imp.isComposite()) ((CompositeImage) imp).setChannelColorModel(cm);
        if (imp.getStackSize() > 1 && !imp.isComposite()) imp.getStack().setColorModel(cm);
        imp.updateAndDraw();
    }
