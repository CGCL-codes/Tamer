    public Main(Renderer r) {
        super();
        mRenderer = r;
        Frame mFrame = null;
        mFrame = new Frame("Newbound 3D Renderer");
        mFrame.setBackground(Color.BLACK);
        mFrame.setSize(INITIAL_WIDTH, INITIAL_HEIGHT);
        mFrame.setVisible(true);
        WindowListener wl = new WindowAdapter() {

            public void windowClosing(WindowEvent arg0) {
                System.exit(0);
            }
        };
        mFrame.addWindowListener(wl);
        Panel p = new Panel() {

            public void paint(Graphics g) {
                int x = getWidth();
                int y = getHeight();
                g.clearRect(0, 0, x, y);
                g.drawImage(mRenderer.getImage(), 0, 0, this);
            }

            public void update(Graphics g) {
            }
        };
        mFrame.add(p, BorderLayout.CENTER);
        mFrame.doLayout();
        long lag = 0;
        long mLastRenderTime = System.currentTimeMillis();
        double mCurrentScale = 10;
        Graphics g = p.getGraphics();
        while (mFrame.isVisible()) {
            long now = System.currentTimeMillis();
            mRenderer.setSize(p.getWidth(), p.getHeight());
            mRenderer.clearModel();
            BufferedImage bi = mRenderer.getImage();
            int width = bi.getWidth();
            int height = bi.getHeight();
            lag = (int) (((now - mLastRenderTime) * 0.1d) + (0.9d * lag));
            mLastRenderTime = now;
            int maxscale = (width + height) / 2;
            if ((lag < 200) && (mCurrentScale < maxscale)) mCurrentScale++; else if ((lag > 300) && (mCurrentScale > 10)) mCurrentScale = mCurrentScale / 2;
            double scale = mCurrentScale;
            double percent = (double) (System.currentTimeMillis() % 10000) / 10000d;
            double magic = Math.PI * 2d * percent;
            double x1 = Math.sin(magic) / 4d + 0.5d;
            double y1 = 0.5d;
            double z1 = Math.cos(magic) / 4d + 0.5d;
            double x2 = 0.5d;
            double y2 = 0.75d;
            double z2 = 0.5d;
            double x3 = 0.5d;
            double y3 = 0.25d;
            double z3 = 0.5d;
            Vector3d nonorm = new Vector3d();
            mRenderer.addLine(1d, 1d, 0d, 1d, 1d, 1d, scale, Color.BLUE.getRGB(), 0, 0, 0, 0);
            mRenderer.addLine(1d, 1d, 0d, 1d, 1d, 1d, scale, Color.BLUE.getRGB(), 0, 0, 0, 0);
            mRenderer.addLine(1d, 0d, 0d, 1d, 0d, 1d, scale, Color.BLUE.getRGB(), 0, 0, 0, 0);
            mRenderer.addLine(0d, 1d, 0d, 0d, 1d, 1d, scale, Color.BLUE.getRGB(), 0, 0, 0, 0);
            mRenderer.addLine(0d, 0d, 0d, 0d, 0d, 1d, scale, Color.BLUE.getRGB(), 0, 0, 0, 0);
            mRenderer.addTriangle(1d, 0.75d, 0.25d, 1d, 0.75d, 0.75d, 1d, 0.25d, 0.25d, scale, Color.YELLOW.getRGB() & 0xFFFFFFFF, 0d);
            mRenderer.addTriangle(1d, 0.25d, 0.25d, 1d, 0.75d, 0.75d, 1d, 0.25d, 0.75d, scale, Color.GREEN.getRGB() & 0xFFFFFFFF, 0d);
            mRenderer.addTriangle(x2, y2, z2, x3, y3, z3, x1, y1, z1, scale, Color.RED.getRGB(), 0);
            mRenderer.addSphere(scale, x1, y1, z1, 0.25d, 12, Color.CYAN.getRGB() & 0x44FFFFFF, 0.5d);
            mRenderer.renderImage(400);
            p.paint(p.getGraphics());
        }
    }
