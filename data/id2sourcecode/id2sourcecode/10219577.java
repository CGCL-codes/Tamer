    public boolean modifyShout(Order shout) {
        double a = estimatedAskQuote();
        double b = estimatedBidQuote();
        double t = getAgent().getValuation(auction);
        double p = 0;
        if (Double.isInfinite(a) || Double.isInfinite(b)) {
            p = t;
        } else {
            p = (a + b) / 2;
        }
        if (((TokenTradingAgent) agent).isBuyer()) {
            if (p < t) {
                shout.setPrice(p);
            } else {
                shout.setPrice(t);
            }
        } else {
            if (p > t) {
                shout.setPrice(p);
            } else {
                shout.setPrice(t);
            }
        }
        return super.modifyShout(shout);
    }
