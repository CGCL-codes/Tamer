    public static boolean lockAndAppend(File file, String output) {
        for (int i = 0; i < RETRIES; i++) {
            try {
                FileOutputStream fos = new FileOutputStream(file, true);
                FileLock fl = fos.getChannel().tryLock();
                if (fl != null) {
                    OutputStreamWriter fw = new OutputStreamWriter(fos);
                    fw.append(output);
                    fl.release();
                    fw.close();
                    System.out.println("Locked, appended, and released for file: " + file.getAbsolutePath() + " value: " + output);
                    return (true);
                } else {
                    System.out.println("Can't get lock for " + file.getAbsolutePath() + " try number " + i + " of " + RETRIES);
                    Thread.sleep(2000);
                }
                fos.close();
            } catch (IOException e) {
                System.out.println("Exception with LockingFileTools: " + e.getMessage());
                e.printStackTrace();
            } catch (InterruptedException e) {
                System.out.println("Exception with LockingFileTools: " + e.getMessage());
                e.printStackTrace();
            }
        }
        System.out.println("Unable to get lock for " + file.getAbsolutePath() + " gave up after " + RETRIES + " tries");
        return (false);
    }
