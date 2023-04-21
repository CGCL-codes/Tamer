    public Boolean downloadUrl(String dir, String urlString) {
        debug.print("dir=" + dir + " urlString=" + urlString);
        BufferedInputStream in = null;
        RandomAccessFile out = null;
        Integer size = 0;
        int BLOCK_SIZE = 4096;
        try {
            URL url = new URL(urlString);
            log.warn("Downloading file: " + urlString + " ...");
            URLConnection con = url.openConnection();
            size = con.getContentLength();
            in = new BufferedInputStream(con.getInputStream());
            localFileName = getFileName(dir, urlString);
            out = new RandomAccessFile(localFileName, "rw");
            Integer howManyBytes;
            Integer readSoFar = 0;
            byte[] bytesIn = new byte[BLOCK_SIZE];
            while ((howManyBytes = in.read(bytesIn)) >= 0) {
                out.write(bytesIn, 0, howManyBytes);
                readSoFar += howManyBytes;
                Float f = 100 * readSoFar.floatValue() / size.floatValue();
                Integer pct = f.intValue();
                String title = String.format("download: %d%% %s", pct, config.kmttg);
                config.gui.progressBar_setValue(pct);
                config.gui.setTitle(title);
                config.gui.refresh();
            }
            in.close();
            out.close();
            log.warn("Download completed successfully");
            return true;
        } catch (MalformedURLException e) {
            log.error(e.toString() + " - " + urlString);
        } catch (NoRouteToHostException e) {
            log.error("URL cannot be reached: " + urlString);
        } catch (ConnectException e) {
            log.error("Connection error: " + e.getMessage());
        } catch (FileNotFoundException e) {
            log.error("File or Path not found: " + e.getMessage());
        } catch (Exception e) {
            log.error(e.toString());
        } finally {
            try {
                in.close();
                out.close();
            } catch (Exception ee) {
            }
        }
        return false;
    }
