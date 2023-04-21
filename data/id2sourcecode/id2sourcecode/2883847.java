    public static boolean bjwflate_deflopt_gz(String caption, File src, File dst, int splitSize, boolean noprep) {
        System.out.println("bjwflate:" + splitSize + " @ " + System.currentTimeMillis());
        File zip = new File(src.getParentFile(), src.getName() + ".zip");
        try {
            try {
                String[] cmds = new String[noprep ? 5 : 4];
                cmds[0] = BASE_DIR + "tools/BJWFlate.exe";
                cmds[1] = "-y";
                if (noprep) {
                    cmds[2] = "-n";
                }
                cmds[noprep ? 3 : 2] = zip.getAbsolutePath();
                cmds[noprep ? 4 : 3] = src.getAbsolutePath();
                HighLevelSleep(EXEC_DELAY);
                final Process proc = Runtime.getRuntime().exec(cmds, null);
                ByteArrayOutputStream stdout = new ByteArrayOutputStream();
                ByteArrayOutputStream stderr = new ByteArrayOutputStream();
                Streams.asynchronousTransfer(proc.getInputStream(), stdout);
                Streams.asynchronousTransfer(proc.getErrorStream(), stderr);
                final SimpleCountDownLatch latch = new SimpleCountDownLatch();
                new Thread() {

                    public void run() {
                        if (!latch.await(2 * 1000)) {
                            proc.destroy();
                        }
                    }
                }.start();
                int exit = proc.waitFor();
                latch.countDown();
                if (splitSize == 4) {
                    System.out.println("" + caption + "");
                }
                if (stderr.size() != 0) {
                    System.out.println("" + Text.utf8(stdout.toByteArray()) + "");
                    System.out.println("" + Text.utf8(stderr.toByteArray()) + "");
                }
                if (exit != 0) {
                    System.out.println("Exit value: " + exit + "");
                }
                if (exit != 0) {
                    return false;
                }
            } catch (Exception exc) {
                System.out.println("EXCEPTION: " + exc.getClass().getName() + "");
                exc.printStackTrace();
                return false;
            }
            {
            }
            try {
                zip2gz(new FileInputStream(zip), new FileOutputStream(dst));
            } catch (IOException exc) {
                System.out.println("EXCEPTION: " + exc.getClass().getName() + "");
                exc.printStackTrace();
                return false;
            }
            return true;
        } finally {
            zip.delete();
        }
    }
