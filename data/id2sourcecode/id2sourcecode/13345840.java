    protected double[] getBCMwave() {
        double[] fullarray = new double[1];
        try {
            Channel ca1 = cf.getChannel(bcmWavePv);
            fullarray = ca1.getArrDbl();
        } catch (ConnectionException ce) {
            myDoc.errormsg(ce + " " + bcmWavePv);
        } catch (GetException ge) {
            myDoc.errormsg(ge + " " + bcmWavePv);
        }
        return fullarray;
    }
