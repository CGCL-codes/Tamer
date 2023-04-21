    public IntroductionPage() {
        String message = new String("Error: Resource Not Found.");
        java.net.URL url = ClassLoader.getSystemResource("docs/budget_wizard_intro.html");
        if (url != null) {
            try {
                StringBuffer buf = new StringBuffer();
                BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
                while (reader.ready()) {
                    buf.append(reader.readLine());
                }
                message = buf.toString();
            } catch (IOException ex) {
                message = new String("IO Error.");
            }
        }
        JLabel label = new JLabel(message);
        setLayout(new BorderLayout());
        add(label, BorderLayout.NORTH);
    }
