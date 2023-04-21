    public Plot3D[] plotTouchedLUT() {
        Plot3D[] plots = new Plot3D[3];
        double[][][] touchedLut = eliminator.touchedLut.getTouchedLUT();
        for (int x = 0; x < 3; x++) {
            RGBBase.Channel ch = RGBBase.Channel.getChannelByArrayIndex(x);
            plots[x] = plot3DLUT(ch, touchedLut[x], ch.color, property, ch.name() + " Touched LUT", "Touched times");
            plots[x].setFixedBounds(2, 0, 100);
        }
        PlotUtils.arrange(plots, 3, true);
        PlotUtils.setVisible(plots);
        return plots;
    }
