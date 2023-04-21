    public void dispose() {
        m_writer_active = false;
        m_reader_active = false;
        m_report_active = false;
        m_dispatcher.shutdown();
        try {
            m_writer_thread.join();
            m_reader_thread.join();
            m_report_thread.join();
        } catch (Exception e) {
            System.err.println("Caught: " + e);
        }
        m_qos_query.destroy();
        m_echo_topic.destroy();
        m_topic.destroy();
    }
