    public P2D_Sonifyer(int pit_base, int pit_range) {
        if (!JMIDI.isReady()) JMIDI.load();
        pitch_base = pit_base;
        pitch_range = pit_range;
        centerPt = new P2D(0, 0);
        setOrchestra(ORCH_ORCH);
        setIntervals(INT_ALL);
        currentBowlPitch = pitch_base + (int) (Math.random() * pitch_range);
        bowl_pitch_set = new Vector<Integer>();
        currentPitch = new int[MAX_CHAN];
        currentTriad = new int[MAX_CHAN][3];
        currentAngleDir = new boolean[MAX_CHAN];
        fishTempi = new int[MAX_CHAN];
        pitchCenters = new int[MAX_CHAN];
        for (int i = 0; i < MAX_CHAN; i++) {
            currentPitch[i] = pitch_base + (int) (pitch_range / 3.0) + (int) (Math.random() * (pitch_range / 3.0));
            pitchCenters[i] = currentPitch[i];
            fishTempi[i] = 16;
            if (DEBUG) {
                System.out.println("Current Pitch #" + i + ": " + currentPitch[i]);
                System.out.println("Current Interval #" + i + ": " + intervals[i]);
            }
            JMIDI.getChannel(i).allNotesOff();
        }
    }
