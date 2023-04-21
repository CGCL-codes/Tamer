                    @Override
                    public void actionPerformed(ActionEvent ae) {
                        String input = getParameter("src");
                        try {
                            URL url = new URL(getCodeBase(), input);
                            BufferedReader r = new BufferedReader(new InputStreamReader(url.openStream()));
                            RevisionVisualization app = new RevisionVisualization(r, AsApplet.this);
                            r.close();
                            app.addWindowListener(new WindowAdapter() {

                                @Override
                                public void windowOpened(WindowEvent e) {
                                    openAction.setEnabled(false);
                                }

                                @Override
                                public void windowClosed(WindowEvent e) {
                                    openAction.setEnabled(true);
                                }
                            });
                            SwingUtils.center(app, 100, 100);
                            SwingUtils.show(app);
                        } catch (Exception err) {
                            ThrowablePane.show(AsApplet.this, err);
                        }
                    }
