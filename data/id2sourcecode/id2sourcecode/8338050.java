    public static synchronized String getRunId(String csChannel) {
        if (m_arrLogCenter != null) {
            int nNbLogCenter = m_arrLogCenter.size();
            for (int n = 0; n < nNbLogCenter; n++) {
                LogCenter logCenter = m_arrLogCenter.get(n);
                if (logCenter.getChannel().equals(csChannel)) return logCenter.getRunId();
            }
        }
        return null;
    }
