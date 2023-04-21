    public void addPosterization(RasterFilterListManager filterManager, IRasterRendering rendering) throws FilterTypeException {
        if (data.isPosterizationActive()) {
            EnhancementStretchListManager elm = new EnhancementStretchListManager(filterManager);
            LinearStretchParams leParams = new LinearStretchParams();
            IStatistics stats = lyr.getDataSource().getStatistics();
            double min = 0;
            double max = 0;
            try {
                stats.calcFullStatistics();
                if (lyr.getDataType()[0] == IBuffer.TYPE_BYTE) {
                    min = 0;
                    max = 255;
                } else if (data.getBandFromBandType() < 3) {
                    min = stats.getMin()[data.getBandFromBandType()];
                    max = stats.getMax()[data.getBandFromBandType()];
                } else if (data.getBandFromBandType() == 3) {
                    min = Math.min(Math.min(stats.getMin()[0], stats.getMin()[1]), stats.getMin()[2]);
                    max = Math.max(Math.max(stats.getMax()[0], stats.getMax()[1]), stats.getMax()[2]);
                }
            } catch (InterruptedException e) {
                return;
            } catch (RasterDriverException e) {
                return;
            } catch (FileNotOpenException e) {
                return;
            }
            double[] in = new double[(data.getPosterizationLevels() - 1) * 2 + 4];
            int[] out = new int[(data.getPosterizationLevels() - 1) * 2 + 4];
            double lastIn = 0, lastOut = 0;
            lastIn = in[0] = in[1] = min;
            lastOut = out[0] = out[1] = 0;
            in[in.length - 1] = in[in.length - 2] = max;
            out[out.length - 1] = out[out.length - 2] = 255;
            int nPieces = data.getPosterizationLevels() - 1;
            int n = 0;
            int increment = 255 / nPieces;
            for (int i = 3; i < in.length - 2; i++) {
                if ((i % 2) != 0) {
                    out[i] = (int) Math.round(lastOut + increment);
                    lastOut = (int) out[i];
                } else out[i] = (int) lastOut;
                n++;
            }
            if (data.getPosterizationLevels() == 2) {
                in[2] = in[3] = lastIn + ((data.getPosterizationThreshold() * (max - min)) / 255);
            } else {
                for (int i = 2; i < in.length - 2; i = i + 2) {
                    in[i] = in[i + 1] = lastIn + ((max - min) / data.getPosterizationLevels());
                    lastIn = (int) in[i];
                }
            }
            leParams.rgb = (lyr.getDataType()[0] == IBuffer.TYPE_BYTE);
            leParams.red.stretchIn = in;
            leParams.red.stretchOut = out;
            leParams.green.stretchIn = in;
            leParams.green.stretchOut = out;
            leParams.blue.stretchIn = in;
            leParams.blue.stretchOut = out;
            elm.addEnhancedStretchFilter(leParams, lyr.getDataSource().getStatistics(), rendering.getRenderBands(), false);
        }
    }
