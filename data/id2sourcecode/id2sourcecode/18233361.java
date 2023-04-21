    private void doEventProcessorCount(MergeSortEvent event) {
        if (isInitialized) return;
        isInitialized = true;
        processors.clear();
        int processorCount = event.getIntData1();
        if (processorCount != 3 && processorCount != 7 && processorCount != 15 && processorCount != 31) {
            logEvent("processorCount must be 3, 7, 15 or 31!");
            return;
        }
        panelTree = new JPanel(new GridLayout(0, processorCount));
        frame.add(panelTree, BorderLayout.CENTER);
        int begin = (processorCount + 1) / 2;
        int gap = 0;
        int count = 1;
        int num = 0;
        ProcessorPanel processorPanel;
        while (count <= processorCount) {
            for (int i = 0; i < begin - 1; i++) panelTree.add(new JPanel());
            for (int x = 0; x < count - 1; x++) {
                processorPanel = new ProcessorPanel("" + (num++));
                panelTree.add(processorPanel);
                processors.add(processorPanel);
                for (int i = 0; i < gap - 1; i++) panelTree.add(new JPanel());
            }
            processorPanel = new ProcessorPanel("" + (num++));
            panelTree.add(processorPanel);
            processors.add(processorPanel);
            for (int i = 0; i < begin - 1; i++) panelTree.add(new JPanel());
            gap = begin;
            begin /= 2;
            count *= 2;
        }
        frame.setVisible(true);
    }
