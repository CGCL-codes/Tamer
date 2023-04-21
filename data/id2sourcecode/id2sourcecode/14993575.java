    public static void main(String[] args) {
        System.out.println("Creating topology");
        final int topSize = 10000;
        final Area topology = new Area();
        Random r = new Random(12345);
        for (int i = 0; i < 500; i++) {
            int x = r.nextInt(topSize);
            int y = r.nextInt(topSize);
            int w = r.nextInt(500) + 50;
            int h = r.nextInt(500) + 50;
            topology.add(new Area(new Rectangle(x, y, w, h)));
        }
        topology.subtract(new Area(new Rectangle(topSize / 2 - 200, topSize / 2 - 200, 400, 400)));
        final Area vision = new Area(new Rectangle(-Integer.MAX_VALUE / 2, -Integer.MAX_VALUE / 2, Integer.MAX_VALUE, Integer.MAX_VALUE));
        int pointCount = 0;
        for (PathIterator iter = topology.getPathIterator(null); !iter.isDone(); iter.next()) {
            pointCount++;
        }
        System.out.println("Starting test " + pointCount + " points");
        final AreaData data = new AreaData(topology);
        data.digest();
        final AreaTree tree = new AreaTree(topology);
        calculateVisibility(topSize / 2, topSize / 2, vision, tree);
        Area area1 = new Area();
        long start = System.currentTimeMillis();
        for (int i = 0; i < 1; i++) {
            area1 = calculateVisibility(topSize / 2, topSize / 2, vision, tree);
        }
        final Area a1 = area1;
        JFrame f = new JFrame();
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setBounds(0, 0, 400, 200);
        f.setLayout(new GridLayout());
        f.add(new JPanel() {

            BufferedImage topImage = null;

            Area theArea = null;

            {
                addMouseMotionListener(new MouseMotionAdapter() {

                    @Override
                    public void mouseDragged(MouseEvent e) {
                        Dimension size = getSize();
                        int x = (int) ((e.getX() - (size.width / 2)) / (size.width / 2.0 / topSize));
                        int y = (int) (e.getY() / (size.height / 2.0 / topSize) / 2);
                        long start = System.currentTimeMillis();
                        System.out.println("Calc: " + (System.currentTimeMillis() - start));
                        repaint();
                    }
                });
                addMouseListener(new MouseAdapter() {

                    @Override
                    public void mousePressed(MouseEvent e) {
                        Dimension size = getSize();
                        int x = (int) ((e.getX() - (size.width / 2)) / (size.width / 2.0 / topSize));
                        int y = (int) (e.getY() / (size.height / 2.0 / topSize) / 2);
                        long start = System.currentTimeMillis();
                        System.out.println("Calc: " + (System.currentTimeMillis() - start));
                        repaint();
                    }
                });
            }

            @Override
            protected void paintComponent(Graphics g) {
                Dimension size = getSize();
                g.setColor(Color.white);
                g.fillRect(0, 0, size.width, size.height);
                Graphics2D g2d = (Graphics2D) g;
                AffineTransform at = AffineTransform.getScaleInstance((size.width / 2) / (double) topSize, (size.height) / (double) topSize);
                if (topImage == null) {
                    Area top = topology.createTransformedArea(at);
                    topImage = new BufferedImage(size.width / 2, size.height, BufferedImage.OPAQUE);
                    Graphics2D g2 = topImage.createGraphics();
                    g2.setColor(Color.white);
                    g2.fillRect(0, 0, size.width / 2, size.height);
                    g2.setColor(Color.green);
                    g2.fill(top);
                    g2.dispose();
                }
                g.setColor(Color.black);
                g.drawLine(size.width / 2, 0, size.width / 2, size.height);
                g.setClip(new Rectangle(size.width / 2, 0, size.width / 2, size.height));
                g2d.translate(200, 0);
                g.setColor(Color.green);
                g2d.drawImage(topImage, 0, 0, this);
                g.setColor(Color.gray);
                if (theArea != null) {
                    g2d.fill(theArea.createTransformedArea(at));
                }
                for (AreaMeta areaMeta : data.getAreaList(new Point(0, 0))) {
                    g.setColor(Color.red);
                    g2d.draw(areaMeta.area.createTransformedArea(at));
                }
                g2d.translate(-200, 0);
            }
        });
        f.setVisible(true);
    }
