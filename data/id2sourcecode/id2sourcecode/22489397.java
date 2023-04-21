    protected void readFile() {
        BufferedReader in = null;
        InputStreamReader inputStreamReader = null;
        try {
            URL url;
            try {
                url = new URL(this.getURL());
            } catch (Exception e) {
                e.printStackTrace();
                url = null;
            }
            URLConnection connection = url.openConnection();
            GZIPInputStream gzipInputStream = new GZIPInputStream(connection.getInputStream());
            inputStreamReader = new InputStreamReader(gzipInputStream);
            in = new BufferedReader(inputStreamReader);
            while (in.ready()) {
                this.processLine(in.readLine());
            }
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        } finally {
            try {
                if (in != null) in.close();
                if (inputStreamReader != null) inputStreamReader.close();
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
        }
    }
