    void downloadLatestVersion() {
        URL url;
        try {
            System.out.println("Getting ready to download");
            url = new URL(updateurl);
            HttpURLConnection hConnection = (HttpURLConnection) url.openConnection();
            HttpURLConnection.setFollowRedirects(true);
            if (HttpURLConnection.HTTP_OK == hConnection.getResponseCode()) {
                System.out.println("Ready to download");
                InputStream in = hConnection.getInputStream();
                BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream("protocombat.jar"));
                int filesize = hConnection.getContentLength();
                progress.setMaximum(filesize);
                byte[] buffer = new byte[4096];
                int numRead;
                long numWritten = 0;
                while ((numRead = in.read(buffer)) != -1) {
                    out.write(buffer, 0, numRead);
                    numWritten += numRead;
                    System.out.println((double) numWritten / (double) filesize);
                    progress.setValue((int) numWritten);
                }
                if (filesize != numWritten) System.out.println("Wrote " + numWritten + " bytes, should have been " + filesize); else System.out.println("Downloaded successfully.");
                out.close();
                in.close();
            } else {
                System.out.println("Download failed: " + hConnection.getResponseCode());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
