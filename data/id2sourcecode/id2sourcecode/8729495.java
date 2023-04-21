        public void run() {
            if (m_responder.isOpen()) {
                m_responder.m_open = false;
                NIOUtils.closeKeyAndChannelSilently(m_responder.getKey(), m_responder.getChannel());
                m_responder.shutdown(m_exception);
            }
        }
