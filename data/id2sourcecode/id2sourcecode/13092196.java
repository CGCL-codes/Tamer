    public Plot3D[] plotCorrectLUT() {
        Plot3D[] plots = new Plot3D[3];
        double[][][] correctLut = eliminator.correctLut;
        for (int x = 0; x < 3; x++) {
            RGBBase.Channel ch = RGBBase.Channel.getChannelByArrayIndex(x);
            plots[x] = plot3DLUT(ch, correctLut[x], ch.color, property, ch.name() + " Correct LUT", "Correct code");
        }
        PlotUtils.arrange(plots, 3, true);
        PlotUtils.setVisible(plots);
        return plots;
    }
