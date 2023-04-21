    public int channel_age() {
        synchronized (m_sync_state) {
            if (!m_active_requests.isEmpty() || (m_client_peer != null && !m_client_peer.getActiveRequestMap().isEmpty())) {
                return RequestIDAllocator.peek_request_id();
            }
            if (m_client_peer != null && m_channel_age < m_client_peer.getChannelAge()) {
                return m_client_peer.getChannelAge();
            }
            return m_channel_age;
        }
    }
