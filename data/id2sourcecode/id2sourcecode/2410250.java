        @Override
        public void init() {
            try {
                String input = this.getParameter("src");
                if (input == null) {
                    this.getContentPane().add(new JLabel("parameter src undefined"));
                    return;
                }
                JPanel pane = new JPanel(new BorderLayout());
                this.setContentPane(pane);
                openAction = new AbstractAction("Run") {

                    private static final long serialVersionUID = 1L;

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
                };
                JButton button = new JButton(openAction);
                button.setFont(new Font("Dialog", Font.BOLD, 18));
                pane.add(button, BorderLayout.CENTER);
                pane.add(new JLabel("Pierre Lindenbaum " + Me.MAIL), BorderLayout.SOUTH);
            } catch (Exception e) {
                this.setContentPane(new ThrowablePane(e, e.getMessage()));
            }
        }
