    public void onReceiveBid(MessageBid bid) {
        synchronized (syncObj) {
            Collections.sort(asks, compAsks);
            if (asks.size() > 0) {
                MessageAsk ask = (MessageAsk) asks.getFirst();
                double priceAsk = ask.getPrice();
                double priceBid = bid.getPrice();
                if (priceBid >= priceAsk) {
                    double finalPrice = (priceAsk + priceBid) / 2;
                    super.match(ask, bid, finalPrice);
                    asks.remove(ask);
                } else {
                    bids.add(bid);
                }
            } else {
                bids.add(bid);
            }
        }
    }
