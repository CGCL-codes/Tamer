    public double[] runWithoutAnnealing(boolean tuneScalingFactor, double startScale, double temperature) {
        double[] weights = runLBFGSSolver(tuneScalingFactor, startScale, temperature);
        if (tuneScalingFactor) {
            System.out.println("optimal scaling factor is " + weights[0]);
            double[] res = new double[numParameters];
            for (int i = 0; i < numParameters; i++) res[i] = weights[i + 1];
            return res;
        } else {
            return weights;
        }
    }
