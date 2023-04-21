    @Override
    public double consume(String result) throws InkMLComplianceException {
        switch(result.charAt(0)) {
            case '*':
                if (!hasLastValue()) {
                    throw new InkMLComplianceException("The value '*'  is not accepted at the first position.");
                }
                return getValueForPoint(getLastValue());
            case '?':
            case ',':
                if (!this.getChannel().isIntermittent()) {
                    throw new InkMLComplianceException("For non intermittent channel '" + this.getChannel().getName().toString() + "' its not acceptable to " + ((result.charAt(0) == ',') ? "drop the value." : "substitute the value with '?'."));
                }
                this.unsetLastValue();
                return Double.NaN;
            case '!':
                this.setState(NumberFormatter.State.EXPLICIT);
            case '\'':
                if (result.charAt(0) == '\'') {
                    this.setState(NumberFormatter.State.FIRST_O);
                }
            case '"':
                if (this.getChannel().isIntermittent()) {
                    throw new InkMLComplianceException("An intermittent does not accepts qualifier prefixes for the values");
                }
                if (result.charAt(0) == '"') {
                    if (this.getState() == NumberFormatter.State.EXPLICIT) {
                        throw new InkMLComplianceException("A numerical channel cannot switch from explicit values to second order values");
                    }
                    this.setState(NumberFormatter.State.SECOND_O);
                }
                result = result.substring(1);
            default:
                return getValueForPoint(Double.parseDouble(result));
        }
    }
