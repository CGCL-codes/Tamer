    private void updatePics() {
        try {
            this.contents.requestFocus();
            this.robot = new Robot();
            this.size = this.contents.getPreferredSize();
            int w = this.size.width;
            int h = this.size.height;
            rectangle.setBounds(this.x, this.y, 5, 5);
            this.topLeft = this.robot.createScreenCapture(rectangle);
            rectangle.setBounds(this.x + w - 5, this.y, 5, 5);
            this.topRight = this.robot.createScreenCapture(rectangle);
            rectangle.setBounds(this.x, this.y + h - 5, 5, 5);
            this.bottomLeft = this.robot.createScreenCapture(rectangle);
            rectangle.setBounds(this.x + w - 5, this.y + h - 5, 5, 5);
            this.bottomRight = this.robot.createScreenCapture(rectangle);
            rectangle.setBounds(this.x + w - 1, this.y, 1, h - 5);
            this.right = this.robot.createScreenCapture(rectangle);
            rectangle.setBounds(this.x + 5, this.y + h - 1, w - 10, 1);
            this.bottom = this.robot.createScreenCapture(rectangle);
            Component layeredPane = getLayeredPane();
            if (layeredPane == null) {
                return;
            }
            point.x = this.x;
            point.y = this.y;
            SwingUtilities.convertPointFromScreen(point, layeredPane);
            bufferRectangle = new Rectangle(point.x, point.y, 5, 5);
            drawRemaining(bufferRectangle, layeredPane, this.topLeft);
            bufferRectangle = new Rectangle(point.x + w - 5, point.y, 5, 5);
            drawRemaining(bufferRectangle, layeredPane, this.topRight);
            bufferRectangle = new Rectangle(point.x, point.y + h - 5, 5, 5);
            drawRemaining(bufferRectangle, layeredPane, this.bottomLeft);
            bufferRectangle = new Rectangle(point.x + w - 5, point.y + h - 5, 5, 5);
            drawRemaining(bufferRectangle, layeredPane, this.bottomRight);
            bufferRectangle = new Rectangle(point.x + w - 1, point.y, 1, h - 5);
            drawRemaining(bufferRectangle, layeredPane, this.right);
            bufferRectangle = new Rectangle(point.x + 5, point.y + h - 1, w - 10, 1);
            drawRemaining(bufferRectangle, layeredPane, this.bottom);
            ((JComponent) this.contents).putClientProperty(RoundedPopupFactory.BOTTOM_LEFT_PIC, this.bottomLeft);
            ((JComponent) this.contents).putClientProperty(RoundedPopupFactory.BOTTOM_RIGHT_PIC, this.bottomRight);
            ((JComponent) this.contents).putClientProperty(RoundedPopupFactory.TOP_LEFT_PIC, this.topLeft);
            ((JComponent) this.contents).putClientProperty(RoundedPopupFactory.TOP_RIGHT_PIC, this.topRight);
            ((JComponent) this.contents).putClientProperty(RoundedPopupFactory.RIGHT_PIC, this.right);
            ((JComponent) this.contents).putClientProperty(RoundedPopupFactory.BOTTOM_PIC, this.bottom);
        } catch (Exception e) {
        }
    }
