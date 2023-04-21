    public void update_def(transceiverDef def) {
        assert (m_def.transceiver_id == def.transceiver_id);
        assert (m_writer_active);
        assert (m_reader_active);
        assert (m_report_active);
        if (m_def.scheduling_class != def.scheduling_class || m_def.thread_priority != def.thread_priority || m_def.topic_kind != def.topic_kind || m_def.topic_id != def.topic_id) {
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
            if (m_def.topic_kind != def.topic_kind || m_def.topic_id != def.topic_id) {
                m_def = def;
                set_topics();
                set_qos();
            } else {
                m_def = def;
            }
            m_config_number++;
            m_writer_active = true;
            m_writer_thread.start();
            m_reader_active = true;
            m_reader_thread.start();
            m_report_active = true;
            m_report_thread.start();
        } else {
            m_def = def;
            m_config_number++;
        }
    }
