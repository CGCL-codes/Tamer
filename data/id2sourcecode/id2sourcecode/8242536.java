    private void captureImages() {
        int x, y, w, h;
        try {
            x = main.getSettings().getMuleXOffset();
            y = main.getSettings().getMuleYOffset();
            h = main.getSettings().getMuleHeight();
            w = main.getSettings().getMuleWidth();
        } catch (Exception e) {
            e.printStackTrace();
            x = 50;
            y = 50;
            w = 215;
            h = 150;
        }
        try {
            robot.delay(100);
            Rectangle rectTop = new Rectangle(x - 50, y - 50, 100, 100);
            Rectangle rectBot = new Rectangle(x - 50 + w, y - 50 + h, 100, 100);
            BufferedImage top = robot.createScreenCapture(rectTop);
            robot.delay(100);
            BufferedImage bot = robot.createScreenCapture(rectBot);
            Graphics2D g = top.createGraphics();
            g.setColor(Color.RED);
            g.drawLine(50, 40, 50, 60);
            g.drawLine(40, 50, 60, 50);
            g = bot.createGraphics();
            g.setColor(Color.RED);
            g.drawLine(50, 40, 50, 60);
            g.drawLine(40, 50, 60, 50);
            capTopIcon = new ImageIcon(top);
            capBotIcon = new ImageIcon(bot);
            lblCapTopImg.setIcon(capTopIcon);
            lblCapBotImg.setIcon(capBotIcon);
            lblCapTopImg.repaint();
            lblCapBotImg.repaint();
            this.repaint();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
