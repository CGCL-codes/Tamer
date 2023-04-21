    public JPanel getButtonBar() {
        JPanel buttonBar = new JPanel();
        buttonBar.setLayout(new BoxLayout(buttonBar, BoxLayout.LINE_AXIS));
        buttonBar.add(Box.createHorizontalGlue());
        upButton = new JButton(UP_ICON);
        upButton.setEnabled(false);
        upButton.setToolTipText("Previous match");
        buttonBar.add(upButton);
        downButton = new JButton(DOWN_ICON);
        downButton.setEnabled(false);
        downButton.setToolTipText("Next match");
        buttonBar.add(downButton);
        buttonBar.add(Box.createHorizontalStrut(20 + 10));
        pageUpButton = new JButton(PAGEUP_ICON);
        pageUpButton.setEnabled(false);
        pageUpButton.setToolTipText("Previous page");
        buttonBar.add(pageUpButton);
        pageDownButton = new JButton(PAGEDOWN_ICON);
        pageDownButton.setEnabled(false);
        pageDownButton.setToolTipText("Next page");
        buttonBar.add(pageDownButton);
        buttonBar.add(Box.createHorizontalStrut(20 + 10));
        openPDFButton = new JButton(PDF_ICON_LARGE);
        openPDFButton.setToolTipText("Open PDF in external viewer");
        if (Desktop.isDesktopSupported() == false) {
            openPDFButton.setEnabled(false);
        }
        buttonBar.add(openPDFButton);
        buttonBar.add(Box.createHorizontalGlue());
        setListeners();
        return buttonBar;
    }
