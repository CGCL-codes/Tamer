    private static void update() {
        if (VERSION.contains("dev")) return;
        try {
            URL updateURL = new URL(updateURL_s + "?v=" + URLEncoder.encode(VERSION, "UTF-8") + "&os=" + URLEncoder.encode(System.getProperty("os.name"), "UTF-8"));
            InputStream uis = updateURL.openStream();
            InputStreamReader uisr = new InputStreamReader(uis);
            BufferedReader ubr = new BufferedReader(uisr);
            String header = ubr.readLine();
            if (header.equals("LIBRETUNESUPDATEPAGE")) {
                String cver = ubr.readLine();
                String cdl = ubr.readLine();
                if (!cver.equals(VERSION)) {
                    System.out.println("Update available!");
                    int i = JOptionPane.showConfirmDialog(null, Language.get("UPDATE_MSG").replaceAll("%v", VERSION).replaceAll("%c", cver), Language.get("UPDATE_TITLE"), JOptionPane.YES_NO_OPTION);
                    if (i == 0) {
                        URL url = new URL(cdl);
                        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                        connection.connect();
                        if (connection.getResponseCode() / 100 != 2) {
                            throw new Exception("Server error! Response code: " + connection.getResponseCode());
                        }
                        int contentLength = connection.getContentLength();
                        if (contentLength < 1) {
                            throw new Exception("Invalid content length!");
                        }
                        int size = contentLength;
                        File tempfile = File.createTempFile("libretunes_update", ".zip");
                        tempfile.deleteOnExit();
                        RandomAccessFile file = new RandomAccessFile(tempfile, "rw");
                        InputStream stream = connection.getInputStream();
                        int downloaded = 0;
                        ProgressWindow pwin = new ProgressWindow(null, Language.get("DOWNLOAD_PROGRESS"));
                        pwin.setVisible(true);
                        pwin.setProgress(0);
                        pwin.setText(Language.get("CONNECT_PROGRESS"));
                        while (downloaded < size) {
                            byte buffer[];
                            if (size - downloaded > 1024) {
                                buffer = new byte[1024];
                            } else {
                                buffer = new byte[size - downloaded];
                            }
                            int read = stream.read(buffer);
                            if (read == -1) break;
                            file.write(buffer, 0, read);
                            downloaded += read;
                            pwin.setProgress(downloaded / size);
                        }
                        file.close();
                        System.out.println("Downloaded file to " + tempfile.getAbsolutePath());
                        pwin.setVisible(false);
                        pwin.dispose();
                        pwin = null;
                        Helper.unzip(tempfile);
                        JOptionPane.showMessageDialog(null, Language.get("UPDATE_SUCCESS_MSG"), Language.get("UPDATE_SUCCESS_TITLE"), JOptionPane.INFORMATION_MESSAGE);
                        Runtime.getRuntime().exec("LibreTunes.exe");
                        System.exit(0);
                    } else {
                    }
                }
                ubr.close();
                uisr.close();
                uis.close();
            } else {
                ubr.close();
                uisr.close();
                uis.close();
                throw new Exception("Update page had invalid header: " + header);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, ex.toString(), Language.get("ERROR_UPDATE_TITLE"), JOptionPane.ERROR_MESSAGE);
        }
    }
