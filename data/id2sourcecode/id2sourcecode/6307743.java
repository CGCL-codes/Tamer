    public void logError(int severity, String message) {
        try {
            if (severity <= m_errorMaxLevel) {
                Thread currentThread = Thread.currentThread();
                String threadInfo = " [" + currentThread.getName() + ":" + currentThread.getPriority() + " ]";
                if (m_messageLogFile == null) {
                    System.out.println(m_timeFormat.format(new Date()) + threadInfo + " ** (" + severity + ") " + message);
                } else {
                    if (!m_dateQualifier.equals(getNow())) {
                        initializeMessageLogger();
                    }
                    m_messageLogFile.write(m_dateFormat.format(new Date()) + threadInfo + " ** (" + severity + ") " + message + "\r\n");
                    m_messageLogFile.flush();
                }
            }
        } catch (Exception e) {
            System.out.println("Error: " + message);
        }
    }
