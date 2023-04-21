    public void maketrans() {
        if (trans) {
            removeWindowListener(dirtFlagger);
            bi = robot.createScreenCapture(new Rectangle(getLocation().x, getLocation().y, getSize().width, getSize().height));
            for (int i = 0; i < bi.getHeight(); i++) {
                for (int j = 0; j < bi.getWidth(); j++) {
                    int rgba = bi.getRGB(j, i);
                    int red = (rgba >> 16) & 0xff;
                    int green = (rgba >> 8) & 0xff;
                    int blue = rgba & 0xff;
                    int alpha = (rgba >> 24) & 0xff;
                    if (red < redM) red = 0; else red -= redM;
                    if (green < greenM) green = 0; else green -= greenM;
                    if (blue < blueM) blue = 0; else blue -= blueM;
                    rgba = (alpha << 24) | (red << 16) | (green << 8) | blue;
                    bi.setRGB(j, i, rgba);
                }
            }
            getContentPane().repaint();
            getRootPane().repaint();
            setVisible(true);
            dirty = false;
            addWindowListener(dirtFlagger);
        } else {
            validate();
            repaint();
        }
    }
