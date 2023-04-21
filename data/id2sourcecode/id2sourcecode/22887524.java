    public void dispose() {
        m_active = false;
        try {
            m_writer_thread.join();
        } catch (Exception e) {
            System.err.println("Caught: " + e);
        }
        m_qos_query.destroy();
        m_topic.destroy();
    }
