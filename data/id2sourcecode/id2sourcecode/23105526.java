    private boolean download(String urlLocation, File location) {
        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;
        try {
            progressMsg = "Downloading " + location.getName();
            progressPercent = 0;
            repaint();
            if (location.exists()) {
                location.delete();
            }
            if (!location.getParentFile().exists()) {
                location.getParentFile().mkdirs();
            }
            URL url = new URL(urlLocation);
            URLConnection urlc = url.openConnection();
            long size = -1;
            String s = urlc.getHeaderField("content-length");
            if (s != null) {
                size = Long.parseLong(s);
            }
            bis = new BufferedInputStream(urlc.getInputStream());
            bos = new BufferedOutputStream(new FileOutputStream(location));
            byte[] buffer = new byte[4096];
            int read = 0;
            if (size > 0) progressBar.initializeSpeedValue(size);
            do {
                read = bis.read(buffer);
                if (read > 0) {
                    progressBar.incrementSpeedValue(read);
                    bos.write(buffer, 0, read);
                }
            } while (read > 0);
            bis.close();
            bis = null;
            bos.close();
            bos = null;
            if (size > 0 && location.length() != size) {
                System.out.println("FAILED to download file: " + location.getAbsolutePath());
                System.out.println("   expected " + size + " bytes and got " + location.length() + " bytes");
                progressMsg = "FAILED to download file: " + urlLocation;
                progressPercent = 0;
                repaint();
                return false;
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (bis != null) try {
                bis.close();
            } catch (Exception e) {
            }
            if (bos != null) try {
                bos.close();
            } catch (Exception e) {
            }
        }
        progressMsg = "FAILED to download file: " + urlLocation;
        progressPercent = 0;
        repaint();
        return false;
    }
