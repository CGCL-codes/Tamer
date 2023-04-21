    private double computeTheoreticalProfit() {
        double pCE = 0;
        FourHeapOrderBook shoutEngine = new FourHeapOrderBook();
        ArrayList<Order> orders = new ArrayList<Order>();
        Iterator<Agent> traders = auction.getTraderIterator();
        while (traders.hasNext()) {
            TokenTradingAgent trader = (TokenTradingAgent) traders.next();
            int quantity = trader.determineQuantity(auction);
            double value = trader.getValuation(auction);
            boolean isBid = trader.isBuyer();
            Order order = new Order(trader, quantity, value, isBid);
            orders.add(order);
            try {
                shoutEngine.add(order);
            } catch (DuplicateShoutException e) {
                logger.error(e.getMessage());
                throw new AuctionRuntimeException(e);
            }
        }
        Order hiAsk = shoutEngine.getHighestMatchedAsk();
        Order loBid = shoutEngine.getLowestMatchedBid();
        if (hiAsk == null || loBid == null) {
            return pCE;
        } else {
            double minPrice = Order.maxPrice(shoutEngine.getHighestMatchedAsk(), shoutEngine.getHighestUnmatchedBid());
            double maxPrice = Order.minPrice(shoutEngine.getLowestUnmatchedAsk(), shoutEngine.getLowestMatchedBid());
            double midEquilibriumPrice = (minPrice + maxPrice) / 2;
            List matchedShouts = shoutEngine.matchOrders();
            if (matchedShouts != null) {
                Iterator i = matchedShouts.iterator();
                while (i.hasNext()) {
                    Order bid = (Order) i.next();
                    Order ask = (Order) i.next();
                    pCE += ((AbstractTradingAgent) bid.getAgent()).equilibriumProfitsEachDay(getAuction(), midEquilibriumPrice, bid.getQuantity());
                    pCE += ((AbstractTradingAgent) ask.getAgent()).equilibriumProfitsEachDay(getAuction(), midEquilibriumPrice, ask.getQuantity());
                }
            }
        }
        return pCE;
    }
