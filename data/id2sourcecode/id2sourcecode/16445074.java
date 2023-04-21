    public MatrixVisualization(URL url) {
        super();
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        try {
            tableModel = CSVReader(url.openStream());
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error loading table. Exiting...");
            System.exit(1);
        }
        initComponents();
    }
