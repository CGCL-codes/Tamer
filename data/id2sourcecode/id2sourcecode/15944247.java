    public final Plot3D[] plotCorrectLUT() {
        Plot3D[] plots = new Plot3D[6];
        int index = 0;
        for (int x = 0; x < 3; x++) {
            for (int c = 0; c < 2; c++) {
                double[][][] correctLut = eliminators[c].correctLut;
                RGBBase.Channel ch = RGBBase.Channel.getChannelByArrayIndex(x);
                RGBBase.Channel adjch = properties[c].getAdjacentChannel(ch);
                plots[index] = Plot3D.getInstance(adjch.name() + "->" + ch.name() + " Correct LUT");
                plots[index].addGridPlot(ch.name(), ch.color, codeValues, codeValues, correctLut[x]);
                plots[index].setAxeLabel(0, adjch + " code");
                plots[index].setAxeLabel(1, ch + " code");
                plots[index].setAxeLabel(2, "Correct code");
                plots[index].setFixedBounds(0, 0, 255);
                plots[index].setFixedBounds(1, 0, 255);
                plots[index].setFixedBounds(2, -10, 10);
                index++;
            }
        }
        PlotUtils.arrange(plots, 3, 2);
        PlotUtils.setVisible(plots);
        return plots;
    }
