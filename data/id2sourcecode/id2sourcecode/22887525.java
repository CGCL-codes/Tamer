    public void update_def(transmitterDef def) {
        assert (m_def.transmitter_id == def.transmitter_id);
        assert (m_active);
        if (m_def.scheduling_class != def.scheduling_class || m_def.thread_priority != def.thread_priority || m_def.topic_kind != def.topic_kind || m_def.topic_id != def.topic_id) {
            m_active = false;
            try {
                m_writer_thread.join();
            } catch (Exception e) {
                System.err.println("Caught: " + e);
            }
            if (m_def.topic_kind != def.topic_kind || m_def.topic_id != def.topic_id) {
                m_def = def;
                set_topic();
                set_qos();
            } else {
                m_def = def;
            }
            m_config_number++;
            m_active = true;
            m_writer_thread.start();
        } else {
            m_def = def;
            m_config_number++;
        }
    }
