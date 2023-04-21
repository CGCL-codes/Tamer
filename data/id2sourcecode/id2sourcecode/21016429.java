        @Override
        public Void doInBackground() {
            progressDown.setValue(0);
            progressDown.setStringPainted(true);
            try {
                URL gUrl = new URL(ytFullUrl);
                HttpURLConnection hconn = (HttpURLConnection) gUrl.openConnection();
                hconn.setDoInput(true);
                hconn.setFollowRedirects(true);
                length = hconn.getContentLength();
                System.out.println("Length: " + length);
                InputStream in = null;
                OutputStream out = null;
                ytFileName = ytTitle.replaceAll("[^0-9A-Za-z-ÅÄÖåäö]", "_");
                ytFileName = System.getProperty("user.home") + "/" + ytFileName + ".flv";
                lblFileInfo.setText("Downloading to: " + ytFileName);
                in = hconn.getInputStream();
                out = new FileOutputStream(ytFileName);
                byte[] buffer = new byte[4096];
                totaldown = 0;
                bytes_read = 0;
                int progress = 0;
                double p = 0.00;
                while ((bytes_read = in.read(buffer)) != -1) {
                    out.write(buffer, 0, bytes_read);
                    totaldown += bytes_read;
                    p = ((double) totaldown / (double) length) * 100;
                    progress = (int) p;
                    setProgress(Math.min(progress, 100));
                    System.out.println("total: " + totaldown + " / " + length + " (" + (int) progress + "%)  --> " + ytFileName);
                }
                in.close();
                out.close();
            } catch (Exception e) {
                System.err.println(e.getLocalizedMessage());
            }
            return null;
        }
