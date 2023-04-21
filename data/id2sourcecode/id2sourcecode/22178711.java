    DataImpl makePoles() throws VisADException, RemoteException {
        SampledSet[] set_s = new SampledSet[n_stations];
        int ii = 0;
        float[][] locs = new float[3][2];
        for (int kk = 0; kk < n_stations; kk++) {
            boolean any = false;
            float hgt = -Float.MAX_VALUE;
            FieldImpl station = (FieldImpl) stations_field.getSample(kk);
            if (station == null) continue;
            int len = station.getLength();
            for (int i = 0; i < len; i++) {
                FieldImpl pole = (FieldImpl) station.getSample(i);
                if (pole == null || pole.getLength() < 2) continue;
                Set set = pole.getDomainSet();
                float[][] samples = set.getSamples(false);
                float[] hi = ((SampledSet) set).getHi();
                if (hi[2] > hgt) hgt = hi[2];
                if (!any && samples[0][0] == samples[0][0] && samples[1][0] == samples[1][0]) {
                    any = true;
                    locs[0][0] = samples[0][0];
                    locs[1][0] = samples[1][0];
                    locs[0][1] = samples[0][0];
                    locs[1][1] = samples[1][0];
                    if (samples[0][0] > lat_max) lat_max = samples[0][0];
                    if (samples[0][0] < lat_min) lat_min = samples[0][0];
                    if (samples[1][0] > lon_max) lon_max = samples[1][0];
                    if (samples[1][0] < lon_min) lon_min = samples[1][0];
                }
            }
            if (any) {
                locs[2][0] = 0.0f;
                locs[2][1] = hgt;
                set_s[ii++] = new Gridded3DSet(spatial_domain, locs, 2, null, null, null);
                if (hgt > hgt_max) hgt_max = hgt;
            }
        }
        SampledSet[] set_ss = new SampledSet[ii];
        System.arraycopy(set_s, 0, set_ss, 0, ii);
        return new UnionSet(spatial_domain, set_ss);
    }
