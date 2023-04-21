        private void writeSemaphoreFile(File inSemaphoreFile) throws Exception {
            FileOutputStream semaphoreOutputStream = new FileOutputStream(inSemaphoreFile);
            FileLock lock = null;
            try {
                lock = semaphoreOutputStream.getChannel().tryLock();
                if (lock == null) {
                    semaphoreOutputStream.close();
                    System.err.println("Locking failed.");
                    throw new Exception();
                }
            } catch (UnsatisfiedLinkError eUle) {
            } catch (NoClassDefFoundError eDcdf) {
            }
            semaphoreOutputStream.write(System.getProperty("user.name").getBytes());
            semaphoreOutputStream.write('\n');
            semaphoreOutputStream.write(String.valueOf(System.currentTimeMillis()).getBytes());
            semaphoreOutputStream.close();
            semaphoreOutputStream = null;
            Tools.setHidden(inSemaphoreFile, true, false);
            if (lock != null) lock.release();
        }
