    public void setActive(boolean val) {
        if (val) {
            System.out.println("Activating Camera driver...");
            if (runThread) {
                init(readFromLog, writeLog);
                m_thread.start();
            } else {
                System.out.println("Initializating jni camera driver...");
                init2(readFromLog, writeLog);
                m_thread.start();
            }
        }
        super.setActive(val);
    }
