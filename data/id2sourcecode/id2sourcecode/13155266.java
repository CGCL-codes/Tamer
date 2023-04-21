    protected void calculateSlowIntegral() {
        boolean failed = false;
        double sum = 0;
        try {
            double arr[] = slowPV.getChannel().getArrDbl();
            double baseline = 0;
            for (int i = 0; i < arr.length; i++) {
                sum += arr[i];
            }
        } catch (GetException e) {
            failed = true;
        } catch (ConnectionException e2) {
            failed = true;
        }
        if (failed) {
            sum = 0;
        }
        calculatedSlowIntegral = sum * deltaT;
    }
