    public void logMessage(int level, String message) {
        try {
            if (level <= m_messageMaxLevel) {
                Thread currentThread = Thread.currentThread();
                String threadInfo = " [" + currentThread.getName() + ":" + currentThread.getPriority() + " ]";
                if (m_messageLogFile == null) {
                    System.out.println(m_timeFormat.format(new Date()) + threadInfo + "    (" + level + ") " + message);
                } else {
                    if (!m_dateQualifier.equals(getNow())) {
                        initializeMessageLogger();
                    }
                    m_messageLogFile.write(m_dateFormat.format(new Date()) + threadInfo + "    (" + level + ") " + message + "\r\n");
                }
            }
        } catch (Exception e) {
            System.out.println(new Date() + "    (" + level + ") " + message);
        }
    }
