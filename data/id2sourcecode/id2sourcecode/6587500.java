    private File findSeriesMatrixFile(String selectedSeries) {
        try {
            String URLname = "ftp://ftp.ncbi.nih.gov/pub/geo/DATA/SeriesMatrix/" + selectedSeries + "/" + selectedSeries + "_series_matrix.txt.gz";
            URL url = new URL(URLname);
            URLConnection con = url.openConnection();
            GZIPInputStream in = new GZIPInputStream(con.getInputStream());
            String target = seriesMatrixFile.getPath();
            OutputStream out = new FileOutputStream(target);
            byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            in.close();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
            MessageDialog.openWarning(main.getShell(), "Download failed!", "ChiBE could not download the file. Check your parameters.");
        }
        return seriesMatrixFile;
    }
