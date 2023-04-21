    @Override
    public double consume(String result) throws InkMLComplianceException {
        if (result.equals("T")) {
            this.setLastValue(1.0);
            return 1.0;
        } else if (result.equals("F")) {
            this.setLastValue(0.0);
            return 0.0;
        } else if (result.equals("*")) {
            if (!hasLastValue()) {
                throw new InkMLComplianceException("For this channel the value '*'  is not accepted in the first place");
            }
            return getLastValue();
        } else if (!this.getChannel().isIntermittent()) {
            throw new InkMLComplianceException("A non intermittent boolean channel only accepts T or F or '*'");
        } else if (result.equals("?")) {
            unsetLastValue();
            return Double.NaN;
        } else {
            throw new InkMLComplianceException("A boolean channel accepts 'T' or 'F' or '?' or '*' or it can be left out when no other value is following");
        }
    }
