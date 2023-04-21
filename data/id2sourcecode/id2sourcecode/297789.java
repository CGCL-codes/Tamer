    public static void download(String strUrl, String strFileDest, JLabel lStatus) {
        try {
            URL url = new URL(strUrl);
            OutputStream out = new BufferedOutputStream(new FileOutputStream(strFileDest));
            URLConnection conn = url.openConnection();
            System.out.println("[" + strUrl + "]");
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
                    lStatus.setText((numWritten / 1024) + "kb");
                    lStatus.repaint();
                }
            }
            in.close();
            out.close();
        } catch (MalformedURLException e) {
            System.out.println("Internal Error. Malformed URL.");
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            System.out.println("Internal Error. Could not find file.");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("Internal I/O Error.");
            e.printStackTrace();
        }
    }
