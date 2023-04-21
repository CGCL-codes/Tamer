            @Override
            public Double doInBackground() {
                startDownloadDate = new Date();
                refreshSpeed = 0;
                lastDate = null;
                try {
                    int totalSizeRead = 0;
                    int totalNumberRead = 0;
                    for (RaphPhotoGalleryPhoto photo : photoList) {
                        URL url = new URL(getCodeBase().toString() + photo.getUrl());
                        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                        DataInputStream dis = new DataInputStream(connection.getInputStream());
                        FileOutputStream fos = new FileOutputStream(destinationDirectory.toString() + "/" + photo.getFileName());
                        byte[] b = new byte[65536];
                        int sizeRead;
                        photo.setProcessedSize(0);
                        totalNumberRead++;
                        while ((sizeRead = dis.read(b)) > -1) {
                            fos.write(b, 0, sizeRead);
                            totalSizeRead += sizeRead;
                            photo.addToProcessedSize(sizeRead);
                            photo.setTotalProcessedSize(totalSizeRead);
                            photo.setTotalProcessedNumber(totalNumberRead);
                            publish(photo);
                            try {
                            } catch (Exception ignore) {
                            }
                        }
                        fos.close();
                    }
                } catch (MalformedURLException e1) {
                    System.err.println("MalformedURLException: " + e1);
                } catch (IOException e2) {
                    System.err.println("IOException: " + e2);
                }
                long totalDiffTime = (new Date()).getTime() - startDownloadDate.getTime();
                double totalSpeed = photoListTotalSize / (totalDiffTime / 1000);
                return new Double(totalSpeed);
            }
