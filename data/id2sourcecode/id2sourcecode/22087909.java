    public void onReceiveAsk(MessageAsk ask) {
        synchronized (syncObj) {
            Collections.sort(bids, compBids);
            if (bids.size() > 0) {
                MessageBid bid = (MessageBid) bids.getFirst();
                double priceAsk = ask.getPrice();
                double priceBid = bid.getPrice();
                if (priceBid >= priceAsk) {
                    double finalPrice = (priceAsk + priceBid) / 2;
                    super.match(ask, bid, finalPrice);
                    bids.remove(bid);
                } else {
                    asks.add(ask);
                }
            } else {
                asks.add(ask);
            }
        }
    }
