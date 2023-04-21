    public static void download(String strUrl, String strFileDest, JLabel lStatus) {
        NoMuleRuntime.showDebug("Download : " + strUrl + "(" + strFileDest + ")");
        try {
            URL url = new URL(strUrl);
            OutputStream out = new BufferedOutputStream(new FileOutputStream(strFileDest));
            URLConnection conn = url.openConnection();
            NoMuleRuntime.showDebug("[" + strUrl + "]");
            InputStream in = conn.getInputStream();
            byte[] buffer = new byte[1024];
            int numRead;
            long numWritten = 0;
            long lastNumWritten = 0;
            while (((numRead = in.read(buffer)) != -1) && (!Thread.currentThread().isInterrupted())) {
                out.write(buffer, 0, numRead);
                numWritten += numRead;
                if ((lastNumWritten + (100 * 1024)) < numWritten) {
                    lastNumWritten = numWritten;
                    if (lStatus != null) {
                        lStatus.setText((numWritten / 1024) + "kb");
                        lStatus.repaint();
                    } else {
                        System.out.println((numWritten / 1024) + "kb");
                    }
                }
            }
            in.close();
            out.close();
        } catch (MalformedURLException e) {
            NoMuleRuntime.showError("Internal Error. Malformed URL.");
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            NoMuleRuntime.showError("Internal Error. Could not find file.");
            e.printStackTrace();
        } catch (IOException e) {
            NoMuleRuntime.showError("Internal I/O Error.");
            e.printStackTrace();
        }
    }
