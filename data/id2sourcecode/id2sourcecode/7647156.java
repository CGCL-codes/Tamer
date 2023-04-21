    private void readCache(int from, int to) {
        FileInputStream stream = null;
        try {
            stream = primeBuf();
            Pair<Integer, Integer> nearest = nearestPrimeIdx(stream, from, to);
            int nearestFrom = nearest.first;
            int nearestTo = nearest.second;
            System.out.printf("Reading prime cache...   0%%");
            int cacheSize = nearestTo - nearestFrom + 1;
            ArrayList<Integer> inArray = new ArrayList<Integer>(cacheSize);
            int percent = 0;
            int lastPercent = 0;
            stream.getChannel().position(SIZE_OF_INT * nearestFrom);
            int availCount = cacheSize;
            for (int i = 0; i < cacheSize; i += BLOCK) {
                int readCount = BLOCK < availCount ? BLOCK : availCount;
                IntBuffer block = readCurrentPrimeBuffered(stream, readCount);
                for (int p = 0; p < readCount; p++) {
                    inArray.add(block.get());
                }
                percent = (int) Math.ceil((i + readCount) * 100.0 / cacheSize);
                if (percent != lastPercent) {
                    System.out.printf("\b\b\b\b%3d%%", percent);
                    System.out.flush();
                }
                lastPercent = percent;
                availCount -= BLOCK;
            }
            System.out.printf("\nTotal primes in prime cache: %d\n", cacheSize);
            System.out.flush();
            System.out.printf("Building in-memory tree of prime sums... ");
            System.out.flush();
            makeCacheTree(inArray);
            inArray = null;
            System.out.printf("done.\n");
            System.out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (stream != null) try {
                stream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
