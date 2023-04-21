    private void init() {
        AudibleButton button;
        PictureEntry current;
        JScrollPane scrollPane;
        JPanel entryPanel;
        JPanel infoPanel;
        JPanel mainPanel;
        JPanel flowPanel;
        JLabel label;
        Image image;
        float f;
        float g;
        int i;
        int j;
        for (i = 0; i < entries_.length - 1; i++) {
            for (j = 0; j < entries_.length - i - 1; j++) {
                f = entries_[j].distance;
                g = entries_[j + 1].distance;
                if (f > g) {
                    current = entries_[j];
                    entries_[j] = entries_[j + 1];
                    entries_[j + 1] = current;
                }
            }
        }
        setLayout(new BorderLayout());
        if (entries_.length == 0) {
            mainPanel = new JPanel();
            label = new JLabel("Keine Bilder gefunden");
            mainPanel.add(label);
            add(mainPanel, BorderLayout.CENTER);
        } else {
            mainPanel = new JPanel(new GridLayout(0, 1));
            scrollPane = new JScrollPane(mainPanel, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
            for (i = 0; i < entries_.length; i++) {
                current = entries_[i];
                try {
                    image = Images.newImage(current.thumbnail);
                    image = image.getScaledInstance(140, 140, Image.SCALE_SMOOTH);
                    button = new AudibleButton(new ImageIcon(image));
                } catch (Exception e) {
                    button = new AudibleButton("kein Bild");
                }
                entryPanel = new JPanel(new BorderLayout());
                button.setPreferredSize(new Dimension(150, 150));
                button.addActionListener(controller_);
                button.setActionCommand(SearchController.FETCH + " " + (id_ - 1) + " " + i);
                entryPanel.add(button, BorderLayout.WEST);
                infoPanel = new JPanel(new GridLayout(0, 1));
                label = new JLabel(" " + current.name);
                label.setForeground(Color.black);
                infoPanel.add(label);
                label = new JLabel(" Bild�hnlichkeit: " + ((int) ((1f - current.distance) * 100f)) + "%");
                label.setForeground(Color.black);
                infoPanel.add(label);
                label = new JLabel(" Preis: " + current.price + " Euro");
                label.setForeground(Color.black);
                infoPanel.add(label);
                entryPanel.add(infoPanel, BorderLayout.CENTER);
                mainPanel.add(entryPanel);
            }
            add(scrollPane, BorderLayout.CENTER);
        }
        flowPanel = new JPanel();
        button = new AudibleButton("Suchergebnis l�schen");
        button.addActionListener(controller_);
        button.setActionCommand(SearchController.CLOSE + " " + id_);
        flowPanel.add(button);
        add(flowPanel, BorderLayout.SOUTH);
    }
