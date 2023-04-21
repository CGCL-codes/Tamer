    public void clear() {
        boolean efficientClearing;
        double a0 = -1, a1 = -1;
        double b0 = -1, b1 = -1;
        double p0 = -1;
        OrderBook orderBook = auctioneer.getOrderBook();
        if (orderBook.getLowestMatchedBid() == null) {
            return;
        }
        if (orderBook.getHighestUnmatchedBid() == null || orderBook.getLowestUnmatchedAsk() == null) {
            efficientClearing = false;
        } else {
            a0 = orderBook.getHighestUnmatchedBid().getPrice();
            b0 = orderBook.getLowestUnmatchedAsk().getPrice();
            p0 = (a0 + b0) / 2;
            efficientClearing = orderBook.getHighestMatchedAsk().getPrice() <= p0 && p0 <= orderBook.getLowestMatchedBid().getPrice();
        }
        if (!efficientClearing) {
            a1 = orderBook.getLowestMatchedBid().getPrice();
            b1 = orderBook.getHighestMatchedAsk().getPrice();
        }
        Iterator<Order> matchedShouts = orderBook.matchOrders().iterator();
        while (matchedShouts.hasNext()) {
            Order bid = matchedShouts.next();
            Order ask = matchedShouts.next();
            if (efficientClearing) {
                auctioneer.clear(ask, bid, p0);
            } else {
                if (bid.getPrice() > a1) {
                    auctioneer.clear(ask, bid, a1, b1, ask.getQuantity());
                }
            }
        }
    }
