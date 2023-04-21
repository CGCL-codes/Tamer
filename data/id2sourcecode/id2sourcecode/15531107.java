        public void run() {
            try {
                final ByteBuffer buffer = ByteBuffer.allocate(BUFFER_SIZE);
                Random randomGenerator = new Random();
                randomGenerator.nextBytes(buffer.array());
                for (int i = 0; i < NUMBER_OF_ITERATIONS; i++) {
                    MTInitiator.write(InitiatorTest.MultiThreadingTest.this, TARGET_DRIVE_NAME, buffer, randomGenerator.nextInt(), buffer.remaining());
                    buffer.rewind();
                }
            } catch (Exception e) {
                fail();
            }
        }
