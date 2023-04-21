    private void createPrimesCache() {
        final int n = 2147483647;
        final int sqrtOfN = (int) Math.rint(Math.sqrt(n));
        BitSet isCompound = new BitSet(n);
        int percent = 0;
        int lastPercent = 0;
        System.out.printf("Sifting:   0%%");
        System.out.flush();
        for (int i = 3; i <= sqrtOfN; i += 2) {
            if (!isCompound.get(i - 1)) {
                for (long j = (long) i * (long) i; j <= n; j += i) {
                    if (j % 2 != 0 && !isCompound.get((int) j - 1)) {
                        isCompound.set((int) j - 1, true);
                    }
                }
            }
            percent = i * 100 / (sqrtOfN - 2 + 1);
            if (percent != lastPercent) {
                System.out.printf("\b\b\b\b%3d%%", percent);
                System.out.flush();
            }
            lastPercent = percent;
        }
        System.out.printf("\n");
        System.out.flush();
        percent = 0;
        lastPercent = 0;
        System.out.printf("Writing:   0%%");
        System.out.flush();
        File binFile = new File("primes.bin");
        FileOutputStream stream = null;
        try {
            stream = new FileOutputStream(binFile);
            ByteBuffer buf2 = ByteBuffer.allocate(SIZE_OF_INT);
            buf2.order(ByteOrder.LITTLE_ENDIAN);
            buf2.putInt(2);
            buf2.rewind();
            stream.getChannel().write(buf2);
            ByteBuffer buf = ByteBuffer.allocate(SIZE_OF_INT * BLOCK);
            buf.order(ByteOrder.LITTLE_ENDIAN);
            int numsPut = 0;
            for (long i = 3; i <= n; i += 2) {
                if (!isCompound.get((int) i - 1)) {
                    buf.putInt((int) i);
                    numsPut++;
                    if (numsPut == BLOCK) {
                        buf.rewind();
                        stream.getChannel().write(buf);
                        numsPut = 0;
                        buf.clear();
                    }
                }
                percent = (int) Math.ceil((i * 1.0 / n) * 100.0);
                if (percent != lastPercent) {
                    System.out.printf("\b\b\b\b%3d%%", percent);
                    System.out.flush();
                }
                lastPercent = percent;
            }
            buf.limit(SIZE_OF_INT * numsPut);
            buf.rewind();
            stream.getChannel().write(buf);
            System.out.printf("\n");
            System.out.flush();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
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
