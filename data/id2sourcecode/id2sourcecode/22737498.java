        public URIInfo(String uri) {
            m_id = null;
            m_params = null;
            String[] ids = extractID(uri);
            if (ids != null && ids.length >= 1) {
                m_id = ids[0];
                if (ids.length > 1) {
                    m_params = new String[ids.length - 1];
                    for (int i = 0; i < m_params.length; i++) {
                        m_params[i] = ids[i + 1];
                    }
                }
            }
        }
