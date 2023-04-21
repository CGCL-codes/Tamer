            public void actionPerformed(ActionEvent e) {
                setVisible(false);
                BufferedImage biScreen;
                biScreen = robot.createScreenCapture(rectScreenSize);
                setVisible(true);
                ia.setImage(biScreen);
                jsp.getHorizontalScrollBar().setValue(0);
                jsp.getVerticalScrollBar().setValue(0);
            }
