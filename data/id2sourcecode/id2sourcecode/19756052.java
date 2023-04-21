    public TraceVisualizer(Trace t, int a_width, int a_height) {
        super("D2 Game Trace Visualization");
        m_trace = t;
        m_currentState = 0;
        rand = new Random();
        {
            JPanel panel = new JPanel();
            getContentPane().add(panel);
            JButton button1 = new JButton("Zoom in");
            button1.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    if (m_vp_zoom < m_max_zoom) {
                        m_vp_zoom *= 1.25;
                    }
                    m_vp_autozoom = false;
                }
            });
            JButton button2 = new JButton("Zoom out");
            button2.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    if (m_vp_zoom > m_min_zoom) {
                        m_vp_zoom /= 1.25;
                    }
                    m_vp_autozoom = false;
                }
            });
            JButton button3 = new JButton("Auto zoom");
            button3.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    m_vp_autozoom = true;
                }
            });
            JButton button4 = new JButton("previous");
            button4.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    if (m_currentState > 0) {
                        m_currentState--;
                    }
                }
            });
            JButton button5 = new JButton("next");
            button5.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    if (m_currentState < m_trace.getEntries().size() - 1) {
                        m_currentState++;
                    }
                }
            });
            panel.add(button1);
            panel.add(button2);
            panel.add(button3);
            panel.add(button4);
            panel.add(button5);
        }
        setSize(a_width, a_height);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
        createBufferStrategy(2);
        m_bufferStrategy = getBufferStrategy();
        m_graph2D = (Graphics2D) m_bufferStrategy.getDrawGraphics();
        {
        }
        class StateVisualizerUpdater extends Thread {

            TraceVisualizer v;

            public StateVisualizerUpdater(TraceVisualizer a_v) {
                v = a_v;
            }

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
        }
        StateVisualizerUpdater updater = new StateVisualizerUpdater(this);
        new Thread(updater).start();
    }
