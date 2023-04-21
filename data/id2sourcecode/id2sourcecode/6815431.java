    protected String download(String urlString) {
        String fileName = "";
        int count;
        try {
            URL url = new URL(urlString);
            URLConnection conexion = url.openConnection();
            conexion.connect();
            int lenghtOfFile = conexion.getContentLength();
            fileName = urlString.substring(urlString.lastIndexOf("/") + 1);
            InputStream input = new BufferedInputStream(url.openStream());
            OutputStream output = new FileOutputStream(MainFrame.destinationFolder + "/" + fileName);
            byte data[] = new byte[1024];
            long total = 0;
            int countProgress = 0;
            while ((count = input.read(data)) != -1) {
                total += count;
                countProgress = (int) (total * 100 / lenghtOfFile);
                SwingUtilities.invokeLater(new UpdateProgressBarTask(DownloadApacheTomcat.jProgressBar1, countProgress));
                DownloadApacheTomcat.jProgressBar1.repaint();
                output.write(data, 0, count);
            }
            output.flush();
            output.close();
            input.close();
            SwingUtilities.invokeLater(new UpdateProgressBarTask(DownloadApacheTomcat.jProgressBar1, 0));
            DownloadApacheTomcat.jProgressBar1.repaint();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return fileName;
    }
