    public double getAveragePrice(UserId exclude, double cost) {
        double ab = getAverageBid(exclude, cost);
        double aa = getAverageAsk(exclude, cost);
        double ap = (ab + aa) / 2;
        return ap;
    }
