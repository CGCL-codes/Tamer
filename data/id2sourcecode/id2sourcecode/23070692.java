    @Before
    public void setUp() throws Exception {
        mono = new SoundBuffer(1, 100);
        for (int i = 0; i < 100; i++) mono.getChannelData(0)[i] = i;
        stereo = new SoundBuffer(2, 150);
        for (int i = 0; i < 150; i++) {
            stereo.getChannelData(0)[i] = 2 * i;
            stereo.getData()[1][i] = 3 * i;
        }
    }
