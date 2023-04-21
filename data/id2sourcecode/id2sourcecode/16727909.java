    public void load(URL url) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
        try {
            DataAdapter da = new StreamDataAdapter(in);
            try {
                da.computeStats();
            } catch (IOException ioe) {
                throw ioe;
            } catch (RuntimeException rte) {
                throw rte;
            } catch (Exception e) {
                throw MathRuntimeException.createIOException(e);
            }
            if (sampleStats.getN() == 0) {
                throw MathRuntimeException.createEOFException("URL {0} contains no data", url);
            }
            in = new BufferedReader(new InputStreamReader(url.openStream()));
            fillBinStats(in);
            loaded = true;
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (Exception ex) {
                }
            }
        }
    }
