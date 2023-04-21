    public void paint(Graphics2D g, PlotFrame plotFrame) {
        g.setColor(Color.BLACK);
        int barWidth = plotFrame.getBarWidth();
        int barXStart = 1;
        Market lastMarket = plotFrame.getLastMarket();
        Market currentMarket = null;
        TimeUnit lastVisible = plotFrame.getTimeForX(g.getClipBounds().width);
        try {
            currentMarket = plotFrame.getDataSource().getOnOrBefore(lastMarket.getTicker(), lastVisible);
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
                if (currentMarket.getOpenPrice().compareTo(currentMarket.getClosePrice()) < 0) {
                    g.setColor(Color.GREEN);
                    g.fillRect(barXStart, Math.min(openY, closeY), barWidth - 1, Math.max(openY, closeY) - Math.min(openY, closeY) + 1);
                    int lineX = barXStart + ((barWidth - 1) / 2);
                    g.drawLine(lineX, highY, lineX, lowY);
                } else {
                    g.setColor(Color.RED);
                    g.fillRect(barXStart, Math.min(openY, closeY), barWidth - 1, Math.max(openY, closeY) - Math.min(openY, closeY) + 1);
                    int lineX = barXStart + ((barWidth - 1) / 2);
                    g.drawLine(lineX, highY, lineX, lowY);
                }
            }
            try {
                currentMarket = currentMarket.getPrevious();
            } catch (Exception e) {
                currentMarket = null;
            }
        }
    }
