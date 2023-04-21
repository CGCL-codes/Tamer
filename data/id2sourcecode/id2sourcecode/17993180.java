    @Override
    public float sample(float lambda) {
        float power;
        int interval_idx = SampleSpectralIntervals1(lambda, SMITS_MIN_LAMBDA, SMITS_MAX_LAMBDA, SMITS_BINS);
        int min_idx = v3dominis();
        power = getChannel(min_idx) * smits_lut[SMITS_WHITE_BASE_FUNC][interval_idx] * SMITS_SCALE;
        int idx_1 = (min_idx + 1) % 3;
        int idx_2 = (min_idx + 2) % 3;
        if (getChannel(idx_1) > getChannel(idx_2)) {
            int tmp = idx_1;
            idx_1 = idx_2;
            idx_2 = tmp;
        }
        power += (getChannel(idx_1) - getChannel(min_idx)) * smits_lut[complement_lut[min_idx]][interval_idx] * SMITS_SCALE;
        power += (getChannel(idx_2) - getChannel(idx_1)) * smits_lut[primary_lut[idx_2]][interval_idx] * SMITS_SCALE;
        return power;
    }
