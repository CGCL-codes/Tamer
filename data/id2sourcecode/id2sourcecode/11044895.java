    public double calculateTrust(Double categoryMatching, Double fixedValue, Double categoryMatchWieght, Double fixedValueWight) {
        if (categoryMatching != null) {
            if (fixedValue != null) {
                if (categoryMatchWieght != null && fixedValueWight != null) {
                    trust = (categoryMatching * categoryMatchWieght + fixedValue * fixedValueWight) / (categoryMatchWieght + fixedValueWight);
                } else {
                    trust = (categoryMatching + fixedValue) / 2;
                }
            } else {
                trust = categoryMatching;
            }
        } else if (fixedValue != null) {
            trust = fixedValue;
        } else {
            trust = 0.0;
        }
        return trust;
    }
