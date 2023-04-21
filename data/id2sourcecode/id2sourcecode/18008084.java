    private VJVoxelLoc bisection(VJValue sample, VJVoxelLoc vlk0, float iso, float k0, float k1, float k0sample, float k1sample, float[] kstep, int steps) {
        float basek0 = k0;
        float basek1 = k1;
        float base0sample = k0sample;
        float base1sample = k1sample;
        VJVoxelLoc vl = null;
        for (int s = 0; s < steps; s++) {
            float m = k0 + (k1 - k0) / 2;
            float mdiff = m - basek0;
            vl = new VJVoxelLoc(vlk0.x + mdiff * (float) kstep[0], vlk0.y + mdiff * (float) kstep[1], vlk0.z + mdiff * (float) kstep[2]);
            float samplef = interpolator.value(sample, v, vl).floatvalue;
            float dk0 = k0sample - iso;
            float dk1 = k1sample - iso;
            float diso = samplef - iso;
            if (diso < EPSILON) return vl;
            if (dk0 * diso > 0.0) {
                k0sample = samplef;
                k0 = m;
            } else if (dk1 * diso > 0.0) {
                k1sample = samplef;
                k1 = m;
            } else if (Math.abs(dk0) >= Math.abs(dk1)) {
                k0sample = samplef;
                k0 = m;
            } else {
                k1sample = samplef;
                k1 = m;
            }
        }
        float sampleDiff = (k1sample - k0sample);
        if (Math.abs(sampleDiff) > 0.0) {
            float f = (iso - k0sample) / sampleDiff;
            float t = k0 + (k1 - k0) * f;
            float tshift = t - basek0;
            vl = new VJVoxelLoc(vlk0.x + tshift * (float) kstep[0], vlk0.y + tshift * (float) kstep[1], vlk0.z + tshift * (float) kstep[2]);
        }
        return vl;
    }
