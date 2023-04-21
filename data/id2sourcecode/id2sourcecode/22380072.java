    private void sonifyTriads(FishBowl2D bowl, int i, double pt_min, double pt_max, double max_dist) {
        int rowsize = bowl_pitch_set.size();
        int[] oldTriad = new int[3];
        for (int t = 0; t < 2; t++) oldTriad[t] = currentTriad[i][t];
        double prox = bowl.avgFishProx(i);
        double x = MiscUtil.mapValueToRange(bowl.getFish(i).x, pt_min, pt_max, 0, rowsize);
        double y = MiscUtil.mapValueToRange(bowl.getFish(i).y, pt_min, pt_max, 0, rowsize);
        double p = MiscUtil.mapValueToRange(prox, 0, max_dist * 2, 0, rowsize);
        double vol = MiscUtil.mapValueToRange(prox, 0, pt_max * 2, 0, MAX_VOL);
        currentTriad[i][0] = (bowl_pitch_set.elementAt((int) x)).intValue();
        currentTriad[i][1] = (bowl_pitch_set.elementAt((int) y)).intValue();
        currentTriad[i][2] = (bowl_pitch_set.elementAt((int) p)).intValue();
        for (int t = 0; t < 2; t++) {
            if (i < MAX_POLY && currentTriad[i][t] != oldTriad[t]) {
                JMIDI.getChannel(i).noteOff(oldTriad[t]);
                JMIDI.getChannel(i).noteOn(currentTriad[i][t], MAX_VOL - (int) vol);
            }
        }
    }
