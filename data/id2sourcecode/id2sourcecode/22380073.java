    private void sonifyPitch(FishBowl2D bowl, int i, double pt_min, double pt_max, double max_dist) {
        int oldPitch = currentPitch[i];
        int rowsize = bowl_pitch_set.size();
        double distFromCenter = bowl.getFish(i).distance(centerPt);
        double prox = bowl.avgFishProx(i);
        double idx = 0;
        switch(style) {
            case SONIC_DIST:
                idx = MiscUtil.mapValueToRange(distFromCenter, 0, max_dist, 0, rowsize);
                break;
            case SONIC_PROX:
                idx = MiscUtil.mapValueToRange(prox, 0, max_dist * 2, 0, rowsize);
                break;
            case SONIC_X:
                idx = MiscUtil.mapValueToRange(bowl.getFish(i).x, pt_min, pt_max, 0, rowsize);
                break;
        }
        double v = MiscUtil.mapValueToRange(prox, 0, pt_max * 2, 0, MAX_VOL);
        currentPitch[i] = (bowl_pitch_set.elementAt((int) idx)).intValue();
        if (i < MAX_POLY && currentPitch[i] != oldPitch) {
            JMIDI.getChannel(i).noteOff(oldPitch);
            JMIDI.getChannel(i).noteOn(currentPitch[i], MAX_VOL - (int) v);
        }
    }
