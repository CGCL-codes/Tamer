    public void updateBackground() {
        try {
            final Robot rbt = new Robot();
            final Toolkit tk = Toolkit.getDefaultToolkit();
            final Dimension dim = tk.getScreenSize();
            background = rbt.createScreenCapture(new Rectangle(0, 0, (int) dim.getWidth(), (int) dim.getHeight()));
        } catch (final Exception ex) {
        }
    }
