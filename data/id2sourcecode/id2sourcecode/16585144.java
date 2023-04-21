    public void loadScript(PrintStream out, PrintStream err, URL url) {
        URLConnection urlcnx;
        BufferedReader br;
        try {
            urlcnx = url.openConnection();
            br = new BufferedReader(new InputStreamReader(urlcnx.getInputStream()));
            String line = new String();
            boolean moreLines = true;
            while (moreLines) {
                line = (String) br.readLine();
                if (line != null) {
                    out.println(line);
                } else {
                    moreLines = false;
                }
            }
        } catch (IOException ioe) {
            ioe.printStackTrace(err);
            err.flush();
            return;
        }
    }
