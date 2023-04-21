    public void refresh() {
        int tabIndex = Preview.folderPreview.getSelectionIndex();
        String titleText = JReader.getPreview(tabIndex).getCurrent().getTitle();
        Date date = JReader.getPreview(tabIndex).getCurrent().getDate();
        String authorText = JReader.getPreview(tabIndex).getCurrent().getAuthor();
        String fromText = JReader.getPreview(tabIndex).getCurrent().getChannelTitle();
        String sourceText = "View channel source (XML)";
        final String url = JReader.getPreview(tabIndex).getCurrent().getLink();
        if (!previewItem.isDisposed()) if (titleText != null) previewItem.setText((titleText.length() > 20) ? titleText.substring(0, 16).concat("...") : titleText); else previewItem.setText("brak tytułu");
        titleLink.setText("<a>" + titleText + "</a>");
        titleLink.setFont(fontBold);
        browser.setText(JReader.getPreview(tabIndex).getCurrent().getHTML());
        if (JReader.getPreview(tabIndex).getCurrent().isShowingItem()) {
            String infoText = "";
            if (date != null) infoText = GUI.shortDateFormat.format(date) + "     ";
            if (authorText != null) infoText += "Author: " + authorText + "\t";
            if (fromText != null) infoText += "From: " + fromText;
            info.setText(infoText);
            source.setText("");
        } else {
            info.setText("");
            source.setText("<a>" + sourceText + "</a>");
        }
        titleLink.addListener(SWT.Selection, new Listener() {

            public void handleEvent(Event event) {
                browser.setUrl(url);
            }
        });
        source.addListener(SWT.Selection, new Listener() {

            public void handleEvent(Event e) {
                int tabIndex = Preview.folderPreview.getSelectionIndex();
                if (JReader.getPreview(tabIndex).getCurrent().getSource() != null) browser.setUrl(JReader.getPreview(tabIndex).getCurrent().getSource()); else browser.setText("<b>Source not found.</b>");
            }
        });
        source.addMouseMoveListener(new MouseMoveListener() {

            public void mouseMove(MouseEvent e) {
                int tabIndex = Preview.folderPreview.getSelectionIndex();
                String text = JReader.getPreview(tabIndex).getCurrent().getSource();
                if (text != null) {
                    GUI.statusLine.setText(text);
                }
            }
        });
        titleLink.addMouseMoveListener(new MouseMoveListener() {

            public void mouseMove(MouseEvent e) {
                int tabIndex = Preview.folderPreview.getSelectionIndex();
                String text = JReader.getPreview(tabIndex).getCurrent().getLink();
                if (text != null) {
                    GUI.statusLine.setText(text);
                }
            }
        });
        titleLink.addListener(SWT.KeyUp, new Listener() {

            public void handleEvent(Event event) {
                if (event.character == 'm') {
                    browser.setUrl(url);
                }
            }
        });
    }
