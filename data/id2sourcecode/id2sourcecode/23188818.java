    void compositeToRGB(CompositeImage imp, String title) {
        int channels = imp.getNChannels();
        int slices = imp.getNSlices();
        int frames = imp.getNFrames();
        int images = channels * slices * frames;
        if (channels == images) {
            compositeImageToRGB(imp, title);
            return;
        }
        width = imp.getWidth();
        height = imp.getHeight();
        imageSize = width * height * 4.0 / (1024.0 * 1024.0);
        channels1 = imp.getNChannels();
        slices1 = slices2 = imp.getNSlices();
        frames1 = frames2 = imp.getNFrames();
        int c1 = imp.getChannel();
        int z1 = imp.getSlice();
        int t2 = imp.getFrame();
        if (!showDialog()) return;
        String title2 = keep ? WindowManager.getUniqueName(imp.getTitle()) : imp.getTitle();
        ImagePlus imp2 = imp.createHyperStack(title2, 1, slices2, frames2, 24);
        convertHyperstack(imp, imp2);
        imp2.setOpenAsHyperStack(slices2 > 1 || frames2 > 1);
        imp2.show();
        if (!keep) {
            imp.changes = false;
            imp.close();
        }
    }
