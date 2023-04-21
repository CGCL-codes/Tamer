    public static void main(String[] args) {
        Random rnd = new Random();
        SHA1Old oldsha = new SHA1Old();
        SHA1 newsha = new SHA1();
        ByteBuffer dBuffer = ByteBuffer.allocateDirect(BUFF_MAX_SIZE);
        ByteBuffer hBuffer = ByteBuffer.allocate(BUFF_MAX_SIZE);
        for (int i = 0; i < BUFF_MAX_SIZE; i++) {
            byte b = (byte) (rnd.nextInt() & 0xFF);
            dBuffer.put(b);
        }
        dBuffer.rewind();
        hBuffer.put(dBuffer);
        hBuffer.rewind();
        dBuffer.rewind();
        try {
            System.out.println("Setting high thread priority to decrease test jitter");
            Thread.currentThread().setPriority(Thread.MAX_PRIORITY);
            Thread.sleep(2000);
        } catch (Exception ignore) {
        }
        for (int t = 0; t < TESTS.length; t++) {
            int buffsize = TESTS[t] * 1024;
            dBuffer.position(0);
            dBuffer.limit(buffsize);
            hBuffer.position(0);
            hBuffer.limit(buffsize);
            int loops = LOOPS[t] / TEST_SPEED_FACTOR;
            String info = " [" + buffsize / 1024 + "KB, " + loops + "x] = ";
            double totalMBytes = ((double) buffsize / (1024 * 1024)) * loops;
            long time;
            double speed;
            System.out.println("direct:");
            System.out.print("Old SHA1");
            time = System.currentTimeMillis();
            for (int i = 0; i < loops; i++) {
                oldsha.reset();
                oldsha.digest(dBuffer);
            }
            time = System.currentTimeMillis() - time;
            speed = totalMBytes / (time / (double) 1024);
            System.out.println(info + time + " ms @ " + speed + " MiB/s");
            System.out.print("New SHA1 ");
            time = System.currentTimeMillis();
            for (int i = 0; i < loops; i++) {
                newsha.reset();
                newsha.digest(dBuffer);
            }
            time = System.currentTimeMillis() - time;
            speed = totalMBytes / (time / (double) 1024);
            System.out.println(info + time + " ms @ " + speed + " MiB/s");
            System.out.println("heap:");
            System.out.print("Old SHA1");
            time = System.currentTimeMillis();
            for (int i = 0; i < loops; i++) {
                oldsha.reset();
                oldsha.digest(hBuffer);
            }
            time = System.currentTimeMillis() - time;
            speed = totalMBytes / (time / (double) 1024);
            System.out.println(info + time + " ms @ " + speed + " MiB/s");
            System.out.print("New SHA1 ");
            time = System.currentTimeMillis();
            for (int i = 0; i < loops; i++) {
                newsha.reset();
                newsha.digest(hBuffer);
            }
            time = System.currentTimeMillis() - time;
            speed = totalMBytes / (time / (double) 1024);
            System.out.println(info + time + " ms @ " + speed + " MiB/s");
            System.out.println();
        }
        System.out.println("performing randomized buffer windowing checks, this may take a while");
        byte[] oldd;
        byte[] newd;
        byte[] oldh;
        byte[] newh;
        int size;
        int offset;
        ByteBuffer dview;
        ByteBuffer hview;
        for (int i = 0; i < LOOPS[1] / TEST_SPEED_FACTOR; i++) {
            size = rnd.nextInt(BUFF_MAX_SIZE);
            offset = rnd.nextInt(BUFF_MAX_SIZE - size - 1);
            hBuffer.limit(offset + size);
            hBuffer.position(offset);
            dBuffer.limit(offset + size);
            dBuffer.position(offset);
            oldsha.reset();
            newsha.reset();
            oldh = oldsha.digest(hBuffer);
            newh = newsha.digest(hBuffer);
            oldsha.reset();
            newsha.reset();
            oldd = oldsha.digest(dBuffer);
            newd = newsha.digest(dBuffer);
            if (!Arrays.equals(oldh, newh) || !Arrays.equals(oldd, newd) || !Arrays.equals(oldd, oldh)) {
                System.out.println("hash mismatch at offset: " + offset + " size: " + size);
                System.out.println("\t\t" + ByteFormatter.nicePrint(oldh));
                System.out.println("\t\t" + ByteFormatter.nicePrint(newh));
                System.out.println("\t\t" + ByteFormatter.nicePrint(oldd));
                System.out.println("\t\t" + ByteFormatter.nicePrint(newd));
            }
            if (hBuffer.limit() != offset + size || dBuffer.limit() != offset + size || hBuffer.position() != offset || dBuffer.position() != offset) System.out.println("buffer does not match its original state");
            dview = dBuffer.slice();
            hview = hBuffer.slice();
            oldsha.reset();
            newsha.reset();
            oldh = oldsha.digest(hview);
            newh = newsha.digest(hview);
            oldsha.reset();
            newsha.reset();
            oldd = oldsha.digest(dview);
            newd = newsha.digest(dview);
            if (!Arrays.equals(oldh, newh) || !Arrays.equals(oldd, newd) || !Arrays.equals(oldd, oldh)) {
                System.out.println("(view) hash mismatch at offset: " + offset + " size: " + size);
                System.out.println("\t\t" + ByteFormatter.nicePrint(oldh));
                System.out.println("\t\t" + ByteFormatter.nicePrint(newh));
                System.out.println("\t\t" + ByteFormatter.nicePrint(oldd));
                System.out.println("\t\t" + ByteFormatter.nicePrint(newd));
            }
            if (hview.limit() != hview.capacity() || dview.limit() != dview.capacity() || hview.position() != 0 || dview.position() != 0) System.out.println("view buffer does not match its original state");
        }
        System.out.println("DONE");
    }
