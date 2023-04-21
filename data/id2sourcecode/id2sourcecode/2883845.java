    public static boolean kzip_deflopt_gz(String caption, File src, File dst, int splitSize, boolean rn) {
        System.out.println("kzip:" + splitSize + " @ " + System.currentTimeMillis());
        File zip = new File(src.getParentFile(), src.getName() + ".zip");
        File dir = new File(src.getParentFile(), "tmp_" + System.nanoTime());
        dir.mkdir();
        try {
            try {
                FileUtil.copyFile(src, new File(dir, src.getName()));
                String[] cmds = new String[rn ? 5 : 4];
                cmds[0] = BASE_DIR + "tools/kzip.exe";
                cmds[1] = "-y";
                cmds[2] = "-b" + splitSize;
                if (rn) {
                    cmds[3] = "-rn";
                }
                cmds[rn ? 4 : 3] = zip.getAbsolutePath();
                HighLevelSleep(EXEC_DELAY);
                final Process proc = Runtime.getRuntime().exec(cmds, null, dir);
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
                if (splitSize == 0 && !rn) {
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
            FileUtil.deleteDirectory(dir, true);
        }
    }
