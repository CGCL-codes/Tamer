    void compositeImageToRGB(CompositeImage imp, String title) {
        if (imp.getMode() == CompositeImage.COMPOSITE) {
            ImagePlus imp2 = imp.createImagePlus();
            imp.updateImage();
            imp2.setProcessor(title, new ColorProcessor(imp.getImage()));
            imp2.show();
            return;
        }
        ImageStack stack = new ImageStack(imp.getWidth(), imp.getHeight());
        int c = imp.getChannel();
        int n = imp.getNChannels();
        for (int i = 1; i <= n; i++) {
            imp.setPositionWithoutUpdate(i, 1, 1);
            stack.addSlice(null, new ColorProcessor(imp.getImage()));
        }
        imp.setPosition(c, 1, 1);
        ImagePlus imp2 = imp.createImagePlus();
        imp2.setStack(title, stack);
        Object info = imp.getProperty("Info");
        if (info != null) imp2.setProperty("Info", info);
        imp2.show();
    }
