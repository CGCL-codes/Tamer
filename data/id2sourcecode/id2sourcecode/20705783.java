        public void setPrograms(Programs programs) {
            m_programs = programs;
            if (m_programs != null) {
                Channels channels = Programs.getChannels();
                if (channels != null) {
                    m_channelDescs = new Vector();
                    channels.getSortedChannelDescriptions(m_channelDescs);
                    int size = m_channelDescs.size();
                    if (m_config != null) {
                        for (int i = 0; i < size; i++) {
                            if (!m_config.isChannelMarked(m_channelDescs.get(i).toString())) {
                                m_channelDescs.remove(i--);
                                size--;
                            }
                        }
                    }
                    m_rowCount = m_channelDescs.size();
                    m_rowHeader.setHeadings(m_channelDescs.toArray());
                    m_vertScroll.setValues(0, 1, 0, m_rowCount - 1);
                }
                setNow();
            } else m_cellData = null;
            adjustScrollbarVisibleAmount();
        }
