    public RandomAccessFile fileOpen(boolean force) {
        ExecutionTimer t = new ExecutionTimer();
        t.start();
        try {
            while (true) {
                try {
                    file = new RandomAccessFile(Path, mode);
                    FileChannel channel = file.getChannel();
                    channel.force(force);
                    lock = channel.lock();
                    break;
                } catch (NonWritableChannelException e1) {
                    System.out.println("file:FileWriter:fileOpen(force):Exception::" + e1.getMessage());
                } catch (Exception e) {
                    System.out.println("file:FileWriter:fileOpen(force):Exception::" + e.getMessage());
                    Thread.sleep(2000);
                }
            }
        } catch (Exception e) {
            System.out.println("file:FileWriter:fileOpen(force):Exception::" + e.getMessage());
        }
        t.end();
        TimerRecordFile timerFile = new TimerRecordFile("file", "FileWriter", "fileOpen", t.duration());
        return file;
    }
