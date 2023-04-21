    private void loadCSVRegistration(URL url) {
        if (url == null) return;
        int lineNo = -1;
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream(), "UTF-8"));
            try {
                String line;
                lineNo = 0;
                while (null != (line = reader.readLine())) {
                    ++lineNo;
                    line = line.trim();
                    if (line.isEmpty() || line.startsWith("#")) continue;
                    String[] fields = line.split("\t");
                    if (fields.length < 1) throw new IllegalStateException("No field at all?! Shouldn't have line.isEmpty() returned true?!!!");
                    try {
                        String className = fields[0].trim();
                        String detectorID = fields.length > 1 ? fields[1].trim() : className;
                        String orderHintStr = fields.length > 2 ? fields[2].trim() : String.valueOf(7000);
                        Class clazz = Class.forName(className);
                        int orderHint = Integer.parseInt(orderHintStr);
                        MimeTypeDetector mimeTypeDetector = (MimeTypeDetector) clazz.newInstance();
                        mimeTypeDetector.setDetectorID(detectorID);
                        mimeTypeDetector.setOrderHint(orderHint);
                        addMimeTypeDetector(mimeTypeDetector);
                    } catch (Throwable x) {
                        log.error("loadCSVRegistration(" + url + ", lineNo " + lineNo + "): " + x.getClass().getName() + ": " + x.getMessage(), x);
                    }
                }
            } finally {
                reader.close();
            }
        } catch (Throwable x) {
            log.error("loadCSVRegistration(" + url + ", lineNo " + lineNo + "): " + x.getClass().getName() + ": " + x.getMessage(), x);
        }
    }
