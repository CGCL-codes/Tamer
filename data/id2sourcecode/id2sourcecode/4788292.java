    public Reduce(Dimension dimension) {
        super();
        try {
            Robot robot = new Robot();
            screenshot = robot.createScreenCapture(new Rectangle(Toolkit.getDefaultToolkit().getScreenSize()));
            AffineTransform tx = new AffineTransform();
            tx.scale(dimension.getWidth() / screenshot.getWidth(), dimension.getHeight() / screenshot.getHeight());
            AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR);
            screenshot = op.filter(screenshot, null);
        } catch (AWTException ae) {
            getLogger().error("Unable to build robot", ae);
        }
        animator = PropertySetter.createAnimator(6000, this, "position", 0.0f, 0.0f);
        animator.start();
    }
