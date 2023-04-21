    void deleteChannel() {
        int c = imp.getChannel();
        ImageStack stack = imp.getStack();
        CompositeImage ci = (CompositeImage) imp;
        LUT[] luts = ci.getLuts();
        stack.deleteSlice(c);
        ImagePlus imp2 = imp.createImagePlus();
        imp2.setStack(stack);
        int n = imp2.getStackSize();
        int mode = ci.getMode();
        if (mode == CompositeImage.COMPOSITE && n == 1) mode = CompositeImage.COLOR;
        imp2 = new CompositeImage(imp, mode);
        int index = 0;
        for (int i = 1; i <= n; i++) {
            if (c == index + 1) index++;
            ((CompositeImage) imp2).setChannelLut(luts[index++], i);
        }
        imp.changes = false;
        imp.hide();
        imp2.show();
    }
