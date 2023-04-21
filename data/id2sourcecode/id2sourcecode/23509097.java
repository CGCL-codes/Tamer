    @Override
    public String toString() {
        return "TrafficShaping with Write Limit: " + writeLimit + " Read Limit: " + readLimit + " and Counter: " + (trafficCounter != null ? trafficCounter.toString() : "none");
    }
