    public static void main(String[] args) {
        try {
            Properties properties;
            boolean stdin = false;
            if (args.length >= 1 && (args[0].equals("--read"))) {
                System.out.print("Reading data from standard in..");
                stdin = true;
                properties = new Properties();
                properties.load(System.in);
            } else {
                URL url = JohnnyVonApplication.class.getClassLoader().getResource("support/input.txt");
                if (url != null) {
                    properties = new Properties();
                    properties.load(url.openStream());
                } else {
                    System.out.println("Using default data.  Run with --read to read data.");
                    properties = JohnnyVonDisplay.DEFAULTS;
                }
            }
            JFrame frame = new JohnnyVonDisplay(properties, new JohnnyVonDisplay.Closer() {

                public void close(JohnnyVonDisplay display) {
                    System.exit(0);
                }
            });
            if (stdin) System.out.println(".done.");
            frame.addWindowListener(new WindowAdapter() {

                public void windowClosing(WindowEvent we) {
                    System.exit(0);
                }
            });
        } catch (Exception e) {
            System.out.println("Failed.  See below for details. (" + e + ")");
            e.printStackTrace();
        }
    }
