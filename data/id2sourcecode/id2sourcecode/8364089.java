    private File downloadFile(URL url) throws SurescriptsPharmacyException {
        File zipFile = null;
        BufferedInputStream in = null;
        FileOutputStream fout = null;
        try {
            in = new BufferedInputStream(url.openStream());
            fout = new FileOutputStream(zipFile);
            byte data[] = new byte[1024];
            int count;
            while ((count = in.read(data, 0, 1024)) != -1) {
                fout.write(data, 0, count);
            }
        } catch (IOException e) {
            throw new SurescriptsPharmacyException("An IO Exception occured while downloading the file from the server");
        } finally {
            if (in != null) try {
                in.close();
            } catch (IOException e) {
            }
            if (fout != null) try {
                fout.close();
            } catch (IOException e) {
            }
        }
        return zipFile;
    }
