    private int streamConf() throws IOException {
        int avail = 0;
        switch(mode) {
            case FILE:
                if (!Helper.findInString(filename, ".jar")) {
                    is = new FileInputStream(filename);
                    isr = new InputStreamReader(is, INPUT_CHARSET);
                } else {
                    is = Helper.getZipInput(filename);
                    isr = new InputStreamReader(is, INPUT_CHARSET);
                }
                break;
            case STREAM:
                isr = new InputStreamReader(is, INPUT_CHARSET);
                break;
            case NET:
                is = url.openStream();
                isr = new InputStreamReader(is, INPUT_CHARSET);
                break;
        }
        switch(mode) {
            case FILE:
            case STREAM:
                avail = is.available();
                break;
            case NET:
                while (is.read() != -1) avail++;
                isr.close();
                is.close();
                is = url.openStream();
                isr = new InputStreamReader(is, INPUT_CHARSET);
                break;
            case STRING:
                avail = fileStr.length();
                break;
        }
        return avail;
    }
