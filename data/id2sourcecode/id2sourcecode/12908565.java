    public static void colorimetric(String[] args) {
        double d65xyValues[] = Illuminant.D65WhitePoint.getxyValues();
        LCDTarget lcdTarget1 = LCDTarget.Instance.getFromAUORampXLS("0329_panel01.xls", LCDTargetBase.Number.Ramp260);
        LCDTarget lcdTarget2 = LCDTarget.Instance.getFromAUORampXLS("0329_panel02.xls", LCDTargetBase.Number.Ramp260);
        LCDTarget lcdTarget3 = LCDTarget.Instance.getFromAUORampXLS("0329_panel03.xls", LCDTargetBase.Number.Ramp260);
        LCDTarget targets[] = new LCDTarget[] { lcdTarget1, lcdTarget2, lcdTarget3 };
        Plot2D plot = Plot2D.getInstance();
        int index = 1;
        for (LCDTarget target : targets) {
            Color c = RGBBase.Channel.getChannel(index++).color;
            for (RGBBase.Channel ch : new RGBBase.Channel[] { RGBBase.Channel.B, RGBBase.Channel.W }) {
                List<Patch> scale = target.filter.grayScalePatch(ch);
                for (Patch p : scale) {
                    CIExyY xyY = new CIExyY(p.getXYZ());
                    plot.addCacheScatterLinePlot(target.getFilename() + "_" + ch.name(), c, xyY.x, xyY.y);
                }
            }
        }
        plot.addScatterPlot("D65", d65xyValues[0], d65xyValues[1]);
        plot.setAxeLabel(0, "x");
        plot.setAxeLabel(0, "y");
        plot.setVisible();
    }
