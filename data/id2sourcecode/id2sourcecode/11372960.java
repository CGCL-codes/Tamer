    private void updateWeight() {
        double w = changedWeight.doubleValue();
        double z0 = ((w / (weight_MAX - weight_MIN)) + 1);
        if (w > weight_MAX || w < weight_MIN) {
            if (w > weight_MAX) {
                w = weight_MAX;
            } else {
                w = weight_MIN;
            }
        }
        double ave = (weight_MAX + weight_MIN) / 2;
        double rng = (weight_MAX - weight_MIN) / 2;
        double delta = -(Math.abs(w - ave) / rng) + 1;
        double p;
        ControllerComponent component = output.getControllerComponent();
        if (component instanceof ContinuousNeuron) {
            p = get_p(((ContinuousNeuron) component).getCellPotential() + ((ContinuousNeuron) component).getBias());
        } else {
            p = get_p(output.getActivation());
        }
        switch(getRule()) {
            case 0:
                w += delta * getN() * p * input.getActivation() * output.getActivation();
                break;
            case 1:
                w += delta * getN() * p * (input.getActivation() - z0) * output.getActivation();
                break;
            case 2:
                w += delta * getN() * p * input.getActivation() * (output.getActivation() - z0);
                break;
            case 3:
                break;
        }
        changedWeight = new Double(w);
    }
