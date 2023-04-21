            public void actionPerformed(ActionEvent arg0) {
                frame.hide();
                try {
                    Dimension dim = null;
                    dim = Toolkit.getDefaultToolkit().getScreenSize();
                    Robot robot;
                    robot = new Robot();
                    robot.delay(100);
                    BufferedImage img = robot.createScreenCapture(new Rectangle(1, 1, (int) dim.getWidth(), (int) dim.getHeight()));
                    icon = new ImageIcon(img);
                    label.setIcon(icon);
                    frame.repaint();
                    label.repaint();
                } catch (AWTException e) {
                    e.printStackTrace();
                }
                frame.show();
            }
