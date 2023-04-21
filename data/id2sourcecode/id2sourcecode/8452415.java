    public RandomAccessFile fileOpen() {
        ExecutionTimer t = new ExecutionTimer();
        t.start();
        try {
            while (true) {
                file = new RandomAccessFile(Path, mode);
                try {
                    FileChannel channel = file.getChannel();
                    lock = channel.lock();
                    break;
                } catch (NonWritableChannelException e1) {
                    System.out.println("file:FileWriter:fileOpen():Exception::" + e1.getMessage());
                } catch (Exception e) {
                    System.out.println("file:FileWriter:fileOpen():Exception::" + e.getMessage());
                    Thread.sleep(2000);
                }
            }
        } catch (Exception e) {
            System.out.println("file:FileWriter:fileOpen():Exception::" + e.getMessage());
        }
        t.end();
        TimerRecordFile timerFile = new TimerRecordFile("file", "FileWriter", "fileOpen", t.duration());
        return file;
    }
