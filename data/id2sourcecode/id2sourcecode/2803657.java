    void addChannel() {
        int c = imp.getChannel();
        ImageStack stack = imp.getStack();
        CompositeImage ci = (CompositeImage) imp;
        LUT[] luts = ci.getLuts();
        ImageProcessor ip = stack.getProcessor(1);
        ImageProcessor ip2 = ip.createProcessor(ip.getWidth(), ip.getHeight());
        stack.addSlice(null, ip2, c);
        ImagePlus imp2 = imp.createImagePlus();
        imp2.setStack(stack);
        int n = imp2.getStackSize();
        imp2 = new CompositeImage(imp, ci.getMode());
        LUT lut = LUT.createLutFromColor(Color.white);
        int index = 0;
        for (int i = 1; i <= n; i++) {
            if (c + 1 == index + 1) {
                ((CompositeImage) imp2).setChannelLut(lut, i);
                c = -1;
            } else ((CompositeImage) imp2).setChannelLut(luts[index++], i);
        }
        imp.changes = false;
        imp.hide();
        imp2.show();
    }
