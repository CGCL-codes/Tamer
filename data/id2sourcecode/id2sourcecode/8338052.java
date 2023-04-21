    public static synchronized void setProduct(String csChannel, String csProduct) {
        if (m_arrLogCenter != null) {
            int nNbLogCenter = m_arrLogCenter.size();
            for (int n = 0; n < nNbLogCenter; n++) {
                LogCenter logCenter = m_arrLogCenter.get(n);
                if (logCenter.getChannel().equals(csChannel) || csChannel == null) logCenter.setProduct(csProduct);
            }
        }
    }
