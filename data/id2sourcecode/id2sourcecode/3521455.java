    private void createUIComponents() {
        cmbSwissprot = new JComboBox(iSwissProtMeans);
        swissMean1 = new JComboBox(iSwissProtMeans);
        swissMean2 = new JComboBox(iSwissProtMeans);
        constructSpeciesCombobox();
        int min = 1;
        int max = iPeptideExample.length() - 1;
        int init = (max + 1) / 2;
        negativeSplit = new JSlider(JSlider.HORIZONTAL, min, max, init);
        negativeSplit.setMajorTickSpacing(5);
        negativeSplit.setMinorTickSpacing(1);
        negativeSplit.setPaintTicks(true);
        negativeSplit.setPaintLabels(true);
        negativeSplit.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
    }
