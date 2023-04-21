    private void writer_thread() {
        try {
            m_creation_time = processor().get_timestamp();
            m_writer.create(m_writer_qos.value);
            throughput_message message = new throughput_message();
            message.application_id = processor().application_id();
            message.random_id = processor().random_id();
            message.transmitter_id = m_def.transmitter_id;
            message.creation_time = m_creation_time;
            message.creation_duration = processor().get_timestamp() - m_creation_time;
            message.sequence_number = 0;
            int last_size = 0;
            while (m_active) {
                if (m_def.message_size != last_size) {
                    int size = 60;
                    size = (m_def.message_size > size) ? m_def.message_size - size : 1;
                    last_size = m_def.message_size;
                    message.payload_data = new char[size];
                    for (int i = 0; i < size; i++) {
                        message.payload_data[i] = (char) (65 + (((i / 2) % 26) + (i % 2) * 32));
                    }
                }
                message.sequence_number++;
                message.config_number = m_config_number;
                message.write_timestamp = processor().get_timestamp();
                for (int index = 0; index < m_def.messages_per_burst; index++) {
                    message.instance_id = index;
                    int retcode = m_writer.value().write(message, 0);
                    m_writer.check(retcode, "throughput_messageDataWriter::write");
                }
                Thread.sleep(m_def.burst_period);
            }
        } catch (DDSError error) {
            processor().report_error(error, partition_id(), m_def.transmitter_id);
            System.err.println("Transmitter writer thread exiting: " + error);
            error.printStackTrace();
        } catch (Exception e) {
            System.err.println("Transmitter writer thread exiting: " + e);
            e.printStackTrace();
        } finally {
            m_writer.destroy();
        }
    }
