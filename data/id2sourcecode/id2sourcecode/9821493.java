    @SuppressWarnings("unused")
    private void emailNote() {
        if (Desktop.isDesktopSupported()) {
            Desktop desktop = Desktop.getDesktop();
            String text2 = browser.getContentsToEmail();
            QUrl url = new QUrl("mailto:");
            url.addQueryItem("subject", browser.getTitle());
            url.addQueryItem("body", text2);
            QDesktopServices.openUrl(url);
        }
    }
