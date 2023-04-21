    public int channel_age() {
        synchronized (m_sync_state) {
            if (!m_active_requests.isEmpty() || (m_server_peer != null && !m_server_peer.getActiveRequestMap().isEmpty())) {
                return RequestIDAllocator.peek_request_id();
            }
            if (m_server_peer != null && m_channel_age < m_server_peer.getChannelAge()) {
                return m_server_peer.getChannelAge();
            }
            return m_channel_age;
        }
    }
