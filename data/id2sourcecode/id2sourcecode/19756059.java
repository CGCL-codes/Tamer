            public void run() {
                Entry entry = null;
                GameState state = null;
                do {
                    try {
                        {
                            float min_x = 0, max_x = 0;
                            float min_y = 0, max_y = 0;
                            min_x = 0;
                            min_y = 0;
                            max_x = 1;
                            max_y = 1;
                            entry = m_trace.getEntries().get(m_currentState);
                            state = entry.getGameState();
                            if (state.getMap() != null && state.getMap() instanceof TwoDMap) {
                                TwoDMap m = (TwoDMap) state.getMap();
                                max_x = m.getSizeInDimension(0) * m.getCellSizeInDimension(0);
                                max_y = m.getSizeInDimension(1) * m.getCellSizeInDimension(1);
                            }
                            float border = (max_x - min_x) * 0.05f;
                            min_x -= border;
                            max_x += border;
                            min_y -= border;
                            max_y += border;
                            m_center_x = (min_x + max_x) / 2;
                            m_center_y = (min_y + max_y) / 2;
                            if (m_vp_autozoom && max_x - min_x != 0 && max_y - min_y != 0) {
                                double required_zoom_x = ((double) getWidth()) / (max_x - min_x);
                                double required_zoom_y = ((double) getHeight() - 67) / (max_y - min_y);
                                double required_zoom = Math.min(required_zoom_x, required_zoom_y);
                                m_vp_zoom = required_zoom;
                            }
                            if (m_vp_zoom > m_max_zoom) {
                                m_vp_zoom = m_max_zoom;
                            }
                            if (m_vp_zoom < m_min_zoom) {
                                m_vp_zoom = m_min_zoom;
                            }
                            if (m_vp_viewed_zoom != m_vp_zoom) {
                                m_vp_viewed_zoom = (3 * m_vp_viewed_zoom + m_vp_zoom) / 4;
                            }
                        }
                        v.update(m_graph2D);
                        Thread.sleep(10);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } while (v.isShowing());
            }
