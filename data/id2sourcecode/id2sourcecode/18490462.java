    public void updateBackground() {
        try {
            Robot rbt = new Robot();
            Toolkit tk = Toolkit.getDefaultToolkit();
            Dimension dim = tk.getScreenSize();
            BufferedImage img = rbt.createScreenCapture(new Rectangle(0, 0, (int) dim.getWidth(), (int) dim.getHeight()));
            float[] matrix = new float[15];
            for (int i = 0; i < 15; i++) {
                matrix[i] = 1.0f / 15.0f;
            }
            BufferedImageOp op = new ConvolveOp(new Kernel(2, 2, matrix));
            background = op.filter(img, background);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
