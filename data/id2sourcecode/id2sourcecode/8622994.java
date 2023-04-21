    public static final void printBuildInfo() {
        String revision = null;
        String date = null;
        URL url = Gbl.class.getResource("/revision.txt");
        if (url != null) {
            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
                revision = reader.readLine();
                date = reader.readLine();
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (revision == null) {
                    log.info("MATSim-Build: unknown");
                } else {
                    log.info("MATSim-Build: " + revision + " (" + date + ")");
                }
            }
        } else {
            log.info("MATSim-Build: unknown");
        }
    }
