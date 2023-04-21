    public void show() {
        final JFrame frame = new JFrame();
        frame.setUndecorated(true);
        try {
            Robot robot = new Robot();
            Dimension size = Toolkit.getDefaultToolkit().getScreenSize();
            int x = 0;
            int y = 0;
            final BufferedImage img = robot.createScreenCapture(new Rectangle(x, y, size.width, size.height));
            final JXPanel panel = new JXPanel();
            Painter painter = new AbstractPainter<JXPanel>() {

                protected void doPaint(Graphics2D g, JXPanel component, int width, int height) {
                    Point origin = new Point(0, 0);
                    SwingUtilities.convertPointFromScreen(origin, component);
                    g.drawImage(img, origin.x, origin.y, null);
                }
            };
            SplashAnim splash = new SplashAnim();
            CompoundPainter rotator = new CompoundPainter(splash);
            TextPainter text = new TextPainter("org.glossitope");
            text.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 160));
            text.setVerticalAlignment(TextPainter.VerticalAlignment.TOP);
            text.setFillPaint(Color.BLACK);
            text.setBorderPaint(Color.BLACK);
            text.setAreaEffects(new NeonBorderEffect());
            text.setStyle(TextPainter.Style.OUTLINE);
            panel.setBackgroundPainter(new CompoundPainter(painter, splash, text));
            frame.add(panel);
            frame.pack();
            frame.setBackground(new Color(0, 0, 0, 0));
            frame.setSize(500, 500);
            frame.setLocation(300, 100);
            frame.setVisible(true);
            Animator anim = PropertySetter.createAnimator(1500, splash, "rotation", 0, 180, 90, 180 + 90, 180, 360, 360 - 90);
            anim.addTarget(new TimingTargetAdapter() {

                public void timingEvent(float f) {
                    panel.repaint();
                }
            });
            anim.addTarget(new PropertySetter(splash, "color", Color.BLACK, Color.RED, Color.BLACK));
            anim.addTarget(new PropertySetter(text, "fillPaint", Color.WHITE, Color.BLACK, Color.WHITE));
            anim.setRepeatCount(1);
            anim.addTarget(new TimingTargetAdapter() {

                public void end() {
                    frame.setVisible(false);
                    frame.dispose();
                }
            });
            anim.start();
        } catch (Exception ex) {
            u.p(ex);
        }
    }
