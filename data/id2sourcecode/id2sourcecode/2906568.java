    public boolean read_latency_message(Condition condition) {
        int retcode = m_reader.value().take(m_reader_messages, m_reader_infos, 1, ANY_SAMPLE_STATE.value, ANY_VIEW_STATE.value, ANY_INSTANCE_STATE.value);
        m_reader.check(retcode, "latency_messageDataReader::take");
        double read_time = processor().get_timestamp();
        int length = m_reader_messages.value.length;
        assert (length == m_reader_infos.value.length);
        for (int i = 0; i < length; i++) {
            latency_message message = m_reader_messages.value[i];
            SampleInfo info = m_reader_infos.value[i];
            double write_time = message.write_timestamp;
            double echo_time = message.echo_timestamp;
            double source_time = Processor.to_timestamp(info.source_timestamp);
            double arrival_time = read_time;
            m_send_latency.add(message.send_latency);
            m_echo_latency.add(read_time - echo_time);
            m_trip_latency.add(read_time - write_time);
            m_send_source_latency.add(message.source_latency);
            m_send_arrival_latency.add(message.arrival_latency);
            m_send_trip_latency.add(message.send_latency - message.source_latency - message.arrival_latency);
            m_echo_source_latency.add(source_time - echo_time);
            m_echo_arrival_latency.add(read_time - arrival_time);
            m_echo_trip_latency.add(arrival_time - source_time);
            if (m_previous_time != 0) {
                m_inter_arrival_time.add(read_time - m_previous_time);
            }
            m_previous_time = read_time;
        }
        m_reader.value().return_loan(m_reader_messages, m_reader_infos);
        return true;
    }
