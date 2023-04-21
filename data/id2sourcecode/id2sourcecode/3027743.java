    private void logFatalError(String message) {
        errors++;
        if (errlog != null && errors <= 1) {
            try {
                URL url2 = new URL(gateway + errlog);
                URLConnection connection = url2.openConnection();
                connection.setDoOutput(true);
                PrintWriter out = new PrintWriter(connection.getOutputStream());
                out.write(message);
                out.close();
            } catch (Exception E) {
            }
        }
    }
