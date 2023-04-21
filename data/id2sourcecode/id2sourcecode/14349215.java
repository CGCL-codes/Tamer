    private File getRemoteFile(String reportLocation, String localPath) {
        try {
            URL reportURL = new URL(reportLocation);
            InputStream in = reportURL.openStream();
            File downloadedFile = new File(localPath);
            if (downloadedFile.exists()) {
                try {
                    downloadedFile.delete();
                } catch (Exception ex) {
                    log.warning("Error al borrar: " + ex.toString());
                }
            }
            FileOutputStream fout = new FileOutputStream(downloadedFile);
            byte buf[] = new byte[1024];
            int s = 0;
            while ((s = in.read(buf, 0, 1024)) > 0) fout.write(buf, 0, s);
            in.close();
            fout.flush();
            fout.close();
            log.info("ReportStarter - Leyo reporte remote file:" + downloadedFile.getAbsolutePath());
            return downloadedFile;
        } catch (FileNotFoundException e) {
            if (reportLocation.indexOf("Subreport") == -1) log.warning("404 not found: Report cannot be found on server " + e.getMessage());
            return null;
        } catch (IOException e) {
            log.severe("I/O error when trying to download (sub)report from server " + e.getMessage());
            return null;
        }
    }
