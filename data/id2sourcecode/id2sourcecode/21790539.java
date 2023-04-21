    public static int pickFromDistribution(final double[] probabilities, final double prob, final int checkboundary) {
        if (prob < 0.0 || prob > 1.0) throw new ArithmeticException("Invalid probability for pickFromDistribution (must be 0.0<=x<=1.0)");
        if (probabilities.length == 1) return 0; else if (probabilities.length < checkboundary) {
            for (int x = 0; x < probabilities.length - 1; x++) if (probabilities[x] > prob) return exemptZeroes(probabilities, x);
            return exemptZeroes(probabilities, probabilities.length - 1);
        } else {
            int top = probabilities.length - 1;
            int bottom = 0;
            int cur;
            while (top != bottom) {
                cur = (top + bottom) / 2;
                if (probabilities[cur] > prob) if (cur == 0 || probabilities[cur - 1] <= prob) return exemptZeroes(probabilities, cur); else top = cur; else if (cur == probabilities.length - 1) return exemptZeroes(probabilities, cur); else if (bottom == cur) bottom++; else bottom = cur;
            }
            return exemptZeroes(probabilities, bottom);
        }
    }
