        @Override
        protected void calculateCalibrationImpl() {
            double[] d = new double[getValuesForCalibration().size()];
            for (int i = 0; i < d.length; i++) {
                d[i] = getValuesForCalibration().get(i).getChannelStrength();
            }
            setMean(mean(d));
        }
