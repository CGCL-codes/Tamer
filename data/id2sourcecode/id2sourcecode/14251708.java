    public static void runTests() {
        try {
            MessageDigest sha1Sun = MessageDigest.getInstance("SHA-1");
            SHA1 sha1Gudy = new SHA1();
            ByteBuffer buffer = ByteBuffer.allocate(1024 * 1024);
            File dir = new File(dirname);
            File[] files = dir.listFiles();
            for (int i = 0; i < files.length; i++) {
                FileChannel fc = new RandomAccessFile(files[i], "r").getChannel();
                System.out.println("Testing " + files[i].getName() + " ...");
                while (fc.position() < fc.size()) {
                    fc.read(buffer);
                    buffer.flip();
                    byte[] raw = new byte[buffer.limit()];
                    System.arraycopy(buffer.array(), 0, raw, 0, raw.length);
                    sha1Gudy.update(buffer);
                    sha1Gudy.saveState();
                    ByteBuffer bb = ByteBuffer.wrap(new byte[56081]);
                    sha1Gudy.digest(bb);
                    sha1Gudy.restoreState();
                    sha1Sun.update(raw);
                    buffer.clear();
                }
                byte[] sun = sha1Sun.digest();
                sha1Sun.reset();
                byte[] gudy = sha1Gudy.digest();
                sha1Gudy.reset();
                if (Arrays.equals(sun, gudy)) {
                    System.out.println("  SHA1-Gudy: OK");
                } else {
                    System.out.println("  SHA1-Gudy: FAILED");
                }
                buffer.clear();
                fc.close();
                System.out.println();
            }
        } catch (Throwable e) {
            Debug.printStackTrace(e);
        }
    }
