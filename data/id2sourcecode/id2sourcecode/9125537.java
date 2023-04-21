            public void actionPerformed(final ActionEvent e) {
                Point currentMousePos = MouseInfo.getPointerInfo().getLocation();
                image = robot.createScreenCapture(new Rectangle(currentMousePos.x - width / zoom / 2, currentMousePos.y - height / zoom / 2, width / zoom, height / zoom)).getScaledInstance(width, height, Image.SCALE_DEFAULT);
                repaint();
            }
