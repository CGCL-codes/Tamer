    private void sonifyAngle(FishBowl2D bowl, int fish) {
        int oldPitch = currentPitch[fish];
        P2D vec = bowl.getFish(fish).getVec();
        double angle = new P2D(0, 0).angle(vec);
        double degrees = Math.toDegrees(angle) + 90;
        currentPitch[fish] = (int) MiscUtil.mapValueToRange(degrees, 0, 180, pitch_base, pitch_base + pitch_range);
        if (currentPitch[fish] != oldPitch) {
            if (currentPitch[fish] > oldPitch) currentAngleDir[fish] = true; else currentAngleDir[fish] = false;
            int vol = (int) (bowl.getFish(fish).getVolatility() / 4.0);
            if (tick % vol == 0) {
                JMIDI.getChannel(fish).allNotesOff();
                JMIDI.getChannel(fish).noteOn(currentPitch[fish], MAX_VOL);
            }
        }
    }
