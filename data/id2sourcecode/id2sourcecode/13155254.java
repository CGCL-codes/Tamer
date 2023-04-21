    protected void calculateFastIntegral() {
        boolean failed = false;
        double sum = 0;
        try {
            double arr[] = fastPV.getChannel().getArrDbl();
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
        calculatedFastIntegral = sum * deltaT;
    }
