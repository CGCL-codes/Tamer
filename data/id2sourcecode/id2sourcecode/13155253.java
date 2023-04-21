    protected void calculateFastData() {
        if (fastCurveData == null) {
            fastCurveData = new CurveData();
            boolean failed = false;
            try {
                double arr[] = fastPV.getChannel().getArrDbl();
                timearr = new double[arr.length];
                for (int i = 0; i < arr.length; i++) {
                    fastCurveData.addPoint(i * deltaT, arr[i]);
                    timearr[i] = i * deltaT;
                }
            } catch (GetException e) {
                failed = true;
            } catch (ConnectionException e2) {
                failed = true;
            }
            if (failed) {
                fastCurveData.addPoint(0, 0);
                fastCurveData.addPoint(1, 1);
                fastCurveData.addPoint(2, 0);
            }
        } else {
            boolean failed = false;
            try {
                double arr[] = fastPV.getChannel().getArrDbl();
                fastCurveData.setPoints(timearr, arr);
            } catch (GetException e) {
                failed = true;
            } catch (ConnectionException e2) {
                failed = true;
            } catch (Exception e) {
                System.out.println("Exception in fast Data: " + e.getMessage());
            }
            if (failed) {
                fastCurveData.addPoint(0, 0);
                fastCurveData.addPoint(1, 1);
                fastCurveData.addPoint(2, 0);
            }
        }
    }
