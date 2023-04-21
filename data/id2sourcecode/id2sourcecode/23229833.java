    public String readFile(URL url) {
        StringBuffer buffer = new StringBuffer();
        try {
            InputStreamReader isr = new InputStreamReader(url.openStream());
            Reader in = new BufferedReader(isr);
            int ch;
            while ((ch = in.read()) > -1) {
                buffer.append((char) ch);
            }
            in.close();
            return buffer.toString();
        } catch (IOException e) {
            ExceptionDialog.displayExceptionDialog(new org.iisc.ilts.utils.BrahmiIconFrame(), e);
            return null;
        }
    }
