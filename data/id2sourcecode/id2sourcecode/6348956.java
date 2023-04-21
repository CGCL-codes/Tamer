    public void paint(Graphics2D g, PlotFrame plotFrame) {
        g.setColor(Color.BLACK);
        int tapSize = 2;
        int barWidth = plotFrame.getBarWidth();
        if (barWidth > 5) {
            tapSize = (barWidth - 1) / 2;
        }
        int lineWidth = 1;
        int barXStart = 1;
        MarketData dataSource = plotFrame.getDataSource();
        Market currentMarket = null;
        Market lastMarket = plotFrame.getLastMarket();
        TimeUnit lastVisible = plotFrame.getTimeForX(g.getClipBounds().width);
        try {
            currentMarket = dataSource.getOnOrBefore(lastMarket.getTicker(), lastVisible);
        } catch (Exception e) {
            currentMarket = lastMarket;
        }
        while (currentMarket != null && barXStart > 0) {
            barXStart = plotFrame.getXForTime(currentMarket.getMarketTime());
            if (barXStart <= g.getClipBounds().width - barWidth) {
                int openY = plotFrame.getYForPrice(currentMarket.getOpenPrice());
                int closeY = plotFrame.getYForPrice(currentMarket.getClosePrice());
                int highY = plotFrame.getYForPrice(currentMarket.getHighPrice());
                int lowY = plotFrame.getYForPrice(currentMarket.getLowPrice());
                if (useColors) {
                    int rc = currentMarket.getOpenPrice().compareTo(currentMarket.getClosePrice());
                    if (rc < 0) g.setColor(Color.GREEN); else if (rc > 0) g.setColor(Color.RED); else g.setColor(Color.BLACK);
                }
                if (plotType == PlotTypes.OHL || plotType == PlotTypes.OHLC) {
                    g.drawLine(barXStart, openY, barXStart + tapSize - 1, openY);
                }
                if (plotType == PlotTypes.OHLC || plotType == PlotTypes.HLC) {
                    g.drawLine(barXStart + tapSize + lineWidth, closeY, barXStart + tapSize + tapSize, closeY);
                }
                g.fillRect(barXStart + tapSize, highY, lineWidth, lowY - highY + 1);
            }
            try {
                currentMarket = currentMarket.getPrevious();
            } catch (Exception e) {
                currentMarket = null;
            }
        }
    }
