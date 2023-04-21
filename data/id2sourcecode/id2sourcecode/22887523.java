    public void create(transmitterDef def) {
        m_def = def;
        String[] params = new String[1];
        params[0] = Integer.toString(m_def.transmitter_id);
        m_qos_query.create(ANY_SAMPLE_STATE.value, ANY_VIEW_STATE.value, ANY_INSTANCE_STATE.value, "transmitter_id = %0", params);
        set_topic();
        transmitterQosSeqHolder qoss = new transmitterQosSeqHolder();
        SampleInfoSeqHolder infos = new SampleInfoSeqHolder();
        int retcode = qos_reader().value().read_w_condition(qoss, infos, 1, m_qos_query.value());
        if (retcode == RETCODE_NO_DATA.value) {
            m_qos = new transmitterQos();
            m_qos.group_id = m_def.group_id;
            m_qos.transmitter_id = m_def.transmitter_id;
            m_qos.partition_id = m_def.partition_id;
            m_qos.qos.latency_budget.duration.sec = 0;
            m_qos.qos.latency_budget.duration.nanosec = 0;
            m_qos.qos.transport_priority.value = 0;
            retcode = qos_writer().value().write(m_qos, 0);
            qos_writer().check(retcode, "transmitterQosDataWriter::write");
        } else {
            qos_reader().check(retcode, "transmitterQosDataReader::read_w_condition");
            assert (qoss.value.length == 1);
            assert (infos.value.length == 1);
            m_qos = qoss.value[0];
            assert (m_qos.group_id == m_def.group_id);
            assert (m_qos.transmitter_id == m_def.transmitter_id);
            assert (m_qos.partition_id == m_def.partition_id);
        }
        qos_reader().value().return_loan(qoss, infos);
        set_qos();
        m_active = true;
        m_writer_thread.start();
    }
