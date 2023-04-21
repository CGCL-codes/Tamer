    public MainFrame() {
        source = new DefaultActiveList<RandomContact>();
        buffer = new DataList<RandomContact>(source);
        infoLabel = new JLabel();
        performanceMonitor1 = new PerformanceMonitor(1000);
        performanceMonitor2 = new PerformanceMonitor(1000);
        performanceMonitor1.start();
        performanceMonitor2.start();
        source.addActiveListListener(performanceMonitor1);
        buffer.addActiveListListener(performanceMonitor2);
        performanceReader = new Thread() {

            int seq = 0;

            public void run() {
                while (!isInterrupted()) {
                    final PerformanceMonitor.Info info1 = performanceMonitor1.getInfo();
                    final PerformanceMonitor.Info info2 = performanceMonitor2.getInfo();
                    Runnable runner = new Runnable() {

                        public void run() {
                            String text = seq++ + ": " + "src events=" + info1.getEvents() + ", " + "dst events=" + info2.getEvents() + ", " + "src changes=" + info1.getChanges() + ", " + "dst changes=" + info2.getChanges();
                            infoLabel.setText(text);
                        }
                    };
                    EventQueue.invokeLater(runner);
                    try {
                        sleep(1000);
                    } catch (InterruptedException ex) {
                        interrupt();
                    }
                }
            }
        };
        performanceReader.start();
        writers = new LinkedList<RandomContactWriter>();
        loadButton = new JButton();
        loadButton.setToolTipText("Reload test data.");
        loadButton.setText("Load data");
        loadButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent evt) {
                loadTestData();
            }
        });
        startButton = new JButton();
        startButton.setToolTipText("Execute a new thread.");
        startButton.setText("Start thread (" + writers.size() + ")");
        startButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent evt) {
                RandomContactWriter writer = new RandomContactWriter();
                writers.add(writer);
                writer.setList(source);
                writer.setPriority(Thread.NORM_PRIORITY);
                writer.start();
                startButton.setText("Start (" + writers.size() + ")");
            }
        });
        stopButton = new JButton("Stop threads");
        stopButton.setToolTipText("Stop all running threads.");
        stopButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent evt) {
                Iterator<RandomContactWriter> iter = writers.iterator();
                while (iter.hasNext()) {
                    RandomContactWriter writer = iter.next();
                    writer.interrupt();
                    iter.remove();
                }
                startButton.setText("Start (" + writers.size() + ")");
            }
        });
        UniqueListPane listPane = new UniqueListPane(buffer);
        AggregationChartPane chartPane = new AggregationChartPane(buffer);
        ContactFilterPane filterPane = new ContactFilterPane(buffer);
        NavigatorPane navigatorPane = new NavigatorPane(buffer);
        AggregationTablePane tablePane = new AggregationTablePane(buffer);
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.add("Filter table", filterPane);
        tabbedPane.add("Unique list", listPane);
        tabbedPane.add("Aggregation table", tablePane);
        tabbedPane.add("Aggregation chart", chartPane);
        tabbedPane.add("Navigator pane", navigatorPane);
        JPanel buttonPane = new JPanel();
        buttonPane.setLayout(new FlowLayout());
        buttonPane.add(loadButton);
        buttonPane.add(startButton);
        buttonPane.add(stopButton);
        Cell cell = new Cell();
        TetrisLayout mainLayout = new TetrisLayout(3, 1);
        mainLayout.setRowWeight(0, 100);
        mainLayout.setRowWeight(1, 0);
        mainLayout.setRowWeight(2, 0);
        JPanel main = new JPanel();
        main.setLayout(mainLayout);
        main.add(tabbedPane, cell);
        main.add(buttonPane, cell);
        main.add(infoLabel, cell);
        setContentPane(main);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        WindowTerminator terminator = new WindowTerminator(this);
        addWindowListener(terminator);
    }
