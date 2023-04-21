    public void generateXQuery() throws Exception {
        backButton.setEnabled(false);
        discardButton.setEnabled(false);
        approveButton.setEnabled(false);
        progressBar.setValue(0);
        eventList.setText("");
        registerProgress(GenerationEventType.INFO, "Reading input file", 1);
        InputStream is = null;
        if (rb_uri.isSelected()) {
            URL url = new URL(tf_uri.getText().trim());
            URLConnection conn = url.openConnection();
            conn.connect();
            is = conn.getInputStream();
        } else {
            is = new FileInputStream(tf_file.getText().trim());
        }
        XQueryMultiWriter xmw = new XQueryMultiWriter();
        XQRecursiveGenerator xrq = new XQRecursiveGenerator("", xmw);
        xrq.setInputStream(is);
        xrq.setLogger(new Log4XQW(this));
        xrq.generateXQueryWithPM(this);
        XQMultiDocument xmd = XQMultiDocument.fromXQueryMultiWriter(xmw);
        registerProgress(GenerationEventType.SUCCESS, "Finished generating successfully", 100);
        document = xmd;
        backButton.setEnabled(true);
        discardButton.setEnabled(true);
        approveButton.setEnabled(true);
    }
