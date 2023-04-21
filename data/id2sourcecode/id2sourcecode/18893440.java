    public void paintBeforeMotes(Graphics g) {
        final Mote selectedMote = visualizer.getSelectedMote();
        if (simulation == null || selectedMote == null || selectedMote.getInterfaces().getRadio() == null) {
            return;
        }
        final Position sPos = selectedMote.getInterfaces().getPosition();
        Position motePos = selectedMote.getInterfaces().getPosition();
        Point pixelCoord = visualizer.transformPositionToPixel(motePos);
        int x = pixelCoord.x;
        int y = pixelCoord.y;
        FontMetrics fm = g.getFontMetrics();
        g.setColor(Color.BLACK);
        MRM radioMedium = (MRM) simulation.getRadioMedium();
        Mote[] dests = simulation.getMotes();
        if (dests == null || dests.length == 0) {
            String msg = "No edges";
            int msgWidth = fm.stringWidth(msg);
            g.setColor(Color.BLACK);
            g.drawString(msg, x - msgWidth / 2, y + 2 * Visualizer.MOTE_RADIUS + 3);
            return;
        }
        g.setColor(Color.BLACK);
        int edges = 0;
        for (Mote d : dests) {
            if (d == selectedMote) {
                continue;
            }
            final Radio dRadio = d.getInterfaces().getRadio();
            TxPair txPair = new RadioPair() {

                public Radio getFromRadio() {
                    return selectedMote.getInterfaces().getRadio();
                }

                public Radio getToRadio() {
                    return dRadio;
                }
            };
            double probArr[] = radioMedium.getChannelModel().getProbability(txPair, Double.NEGATIVE_INFINITY);
            double prob = probArr[0];
            double ss = probArr[1];
            if (prob == 0.0d) {
                continue;
            }
            edges++;
            String msg = String.format("%1.1f%%, %1.2fdB", 100.0 * prob, ss);
            Point pixel = visualizer.transformPositionToPixel(d.getInterfaces().getPosition());
            int msgWidth = fm.stringWidth(msg);
            g.setColor(new Color(1 - (float) prob, (float) prob, 0.0f));
            g.drawLine(x, y, pixel.x, pixel.y);
            g.setColor(Color.BLACK);
            g.drawString(msg, pixel.x - msgWidth / 2, pixel.y + 2 * Visualizer.MOTE_RADIUS + 3);
        }
        String msg = dests.length + " edges";
        int msgWidth = fm.stringWidth(msg);
        g.setColor(Color.BLACK);
        g.drawString(msg, x - msgWidth / 2, y + 2 * Visualizer.MOTE_RADIUS + 3);
    }
