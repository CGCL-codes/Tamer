            public synchronized void run() {
                try {
                    status.setText("Working ...");
                    Image image = selected.getImage();
                    ByteArrayOutputStream tmp = new ByteArrayOutputStream();
                    BufferedImage bufferedImage = new BufferedImage(image.getWidth(null), image.getHeight(null), BufferedImage.TYPE_INT_RGB);
                    bufferedImage.createGraphics().drawImage(image, 0, 0, null);
                    ImageIO.write(bufferedImage, "jpg", tmp);
                    tmp.close();
                    int contentLength = tmp.size();
                    if (contentLength > 1024 * 1024) throw new Exception("Image is too big to upload");
                    URL uploadURL = new URL(documentBase, "upload.php");
                    HttpURLConnection connection = (HttpURLConnection) uploadURL.openConnection();
                    connection.setRequestMethod("POST");
                    connection.setDoOutput(true);
                    connection.setUseCaches(false);
                    connection.setDefaultUseCaches(false);
                    connection.setRequestProperty("content-type", "img/jpeg");
                    connection.setRequestProperty("content-length", String.valueOf(contentLength));
                    OutputStream out = connection.getOutputStream();
                    out.write(tmp.toByteArray());
                    out.close();
                    InputStream in = connection.getInputStream();
                    int c;
                    while ((c = in.read()) != -1) System.err.write(c);
                    in.close();
                    URL imageURL = new URL(documentBase, connection.getHeaderField("file-name"));
                    status.setText("Done - image is uploaded to " + imageURL + " (for at least 5 minutes) ...");
                } catch (Throwable exception) {
                    JOptionPane.showMessageDialog(MainPanel.this, exception.toString(), "Error", JOptionPane.ERROR_MESSAGE);
                    exception.printStackTrace();
                    status.setText("Failed, try again ...");
                }
            }
