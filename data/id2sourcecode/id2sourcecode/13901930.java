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
            double source_time = Processor.to_timestamp(info.source_timestamp);
            double arrival_time = read_time;
            message.send_latency = read_time - write_time;
            message.echo_timestamp = processor().get_timestamp();
            message.source_latency = source_time - write_time;
            message.arrival_latency = read_time - arrival_time;
            message.config_number = m_config_number;
            retcode = m_writer.value().write(message, 0);
            m_writer.check(retcode, "latency_messageDataWriter::write");
        }
        m_reader.value().return_loan(m_reader_messages, m_reader_infos);
        return true;
    }
