        public ChannelsTableModel(JTable parent, Programs programs, Config config) {
            m_programs = programs;
            m_config = config;
            m_parent = parent;
            if (m_config != null) {
                setChannels(Programs.getChannels());
            }
        }
