    public static void writeZIPReport(Galaxy galaxy, Race race, String flags, String reportName, Charset charset, OutputStream os) {
        try {
            ZipOutputStream zos = new ZipOutputStream(os);
            zos.putNextEntry(new ZipEntry(reportName + ".rep"));
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(zos, charset), Galaxy.FILEBLOCKSIZE);
            writeReport(galaxy, new PrintWriter(writer), race, flags);
            zos.close();
            os.flush();
        } catch (IOException err) {
            Galaxy.getLogger().log(Level.SEVERE, "Can't create game " + galaxy.getName() + " turn " + galaxy.getTurn() + (race != null ? " report for " + race.getName() : " full report"), err);
        } finally {
            System.gc();
        }
    }
