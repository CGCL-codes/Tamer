    private ImageStack getStack() {
        if (imp.isHyperStack()) {
            int slices = imp.getNSlices();
            ImageStack stack = new ImageStack(imp.getWidth(), imp.getHeight());
            int c = imp.getChannel(), z = imp.getSlice(), t = imp.getFrame();
            for (int i = 1; i <= slices; i++) {
                imp.setPositionWithoutUpdate(c, i, t);
                stack.addSlice(null, new ColorProcessor(imp.getImage()));
            }
            imp.setPosition(c, z, t);
            currentChannel = c;
            currentFrame = t;
            if (imp.isComposite()) currentMode = ((CompositeImage) imp).getMode();
            return stack;
        } else return imp.getStack();
    }
