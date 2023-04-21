    public void aggregate(MonitoredDataWrapper other) {
        Double val1 = this.mdToDouble();
        Double val2 = other.mdToDouble();
        if (val1 == null || val2 == null) {
            _value = 0;
        } else {
            _value = (val1 + val2) / 2;
        }
    }
