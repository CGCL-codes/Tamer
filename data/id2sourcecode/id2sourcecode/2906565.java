    private void writer_thread() {
        try {
            m_writer.create(m_writer_qos.value);
            latency_message message = new latency_message();
            message.application_id = processor().application_id();
            message.random_id = processor().random_id();
            message.transceiver_id = m_def.transceiver_id;
            message.sequence_number = 0;
            int last_size = 0;
            while (m_writer_active) {
                if (m_def.message_size != last_size) {
                    int size = 76;
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
                int retcode = m_writer.value().write(message, 0);
                m_writer.check(retcode, "latency_messageDataWriter::write");
                Thread.sleep(m_def.write_period);
            }
        } catch (DDSError error) {
            processor().report_error(error, partition_id(), m_def.transceiver_id);
            System.err.println("Transceiver writer thread exiting: " + error);
            error.printStackTrace();
        } catch (Exception e) {
            System.err.println("Transceiver writer thread exiting: " + e);
            e.printStackTrace();
        } finally {
            m_writer.destroy();
        }
    }
