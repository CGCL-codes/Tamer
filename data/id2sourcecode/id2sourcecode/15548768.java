            public void run() {
                try {
                    MessageDigest.getInstance("456").digest("test".getBytes(), 0, 1555);
                } catch (Exception e) {
                    RuntimeException x = new RuntimeException("Starting nest", e);
                    for (int i = 0; i < 10; i++) {
                        x = new RuntimeException("Nesting: " + i, x);
                    }
                    throw x;
                }
            }
