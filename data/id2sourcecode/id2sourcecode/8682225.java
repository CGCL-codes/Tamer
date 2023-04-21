    @Test
    public void testGrabPixelsWhileRunning() throws IOException, BoblightException {
        final Random random = new Random();
        final int scrWidth = SCREEN_WIDTH_MIN + random.nextInt(SCREEN_WIDTH_MAX - SCREEN_WIDTH_MIN);
        final int scrHeight = SCREEN_HEIGHT_MIN + random.nextInt(SCREEN_HEIGHT_MAX - SCREEN_HEIGHT_MIN);
        testable = spy(new AbstractActiveGrabber(client, false, 100, 80) {

            @Override
            public int[] grabPixelAt(int xpos, int ypos) {
                return null;
            }

            @Override
            protected void updateDimensions() {
            }

            @Override
            protected int getScreenWidth() {
                return scrWidth;
            }

            @Override
            protected int getScreenHeight() {
                return scrHeight;
            }

            @Override
            public void setup(FlagManager flagManager) throws BoblightException {
            }

            @Override
            public void cleanup() {
            }
        });
        int size = 2 + random.nextInt(64 - 2);
        Whitebox.setInternalState(testable, "width", size);
        Whitebox.setInternalState(testable, "height", size);
        doAnswer(new Answer<Object>() {

            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                Whitebox.setInternalState(testable, "stop", true);
                return null;
            }
        }).when(client).sendRgb(false, null);
        System.out.println("Starting test method.");
        testable.run();
        double cellWidth = (double) scrWidth / (double) size;
        double cellHeight = (double) scrHeight / size;
        System.out.println("Starting verification.");
        for (int width = 0; width < size; width++) {
            for (int height = 0; height < size; height++) {
                final int xpos = (int) (cellWidth / 2 + cellWidth * width);
                final int ypos = (int) (cellHeight / 2 + cellHeight * height);
                verify(testable).grabPixelAt(xpos, ypos);
            }
        }
    }
