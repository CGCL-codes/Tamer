    public String download(String urlString, JProgressBar progressBar, String outFileName) {
        String fileName = "";
        int count;
        try {
            URL url = new URL(urlString);
            URLConnection conexion = url.openConnection();
            conexion.connect();
            int lenghtOfFile = conexion.getContentLength();
            if (outFileName != null) {
                fileName = outFileName;
            } else {
                fileName = urlString.substring(urlString.lastIndexOf("/") + 1);
            }
            File outputDir = new File("tmp");
            if (!outputDir.exists()) {
                outputDir.mkdirs();
            }
            InputStream input = new BufferedInputStream(url.openStream());
            OutputStream output = new FileOutputStream("tmp/" + fileName);
            byte data[] = new byte[1024];
            long total = 0;
            int countProgress = 0;
            while ((count = input.read(data)) != -1) {
                total += count;
                if (progressBar != null) {
                    countProgress = (int) (total * 100 / lenghtOfFile);
                    SwingUtilities.invokeLater(new UpdateProgressBarTask(progressBar, countProgress));
                    progressBar.repaint();
                }
                output.write(data, 0, count);
            }
            output.flush();
            output.close();
            input.close();
            if (progressBar != null) {
                SwingUtilities.invokeLater(new UpdateProgressBarTask(progressBar, 0));
                progressBar.repaint();
            }
        } catch (Exception ex) {
            Ini.logger.fatal("Error: ", ex);
            return null;
        }
        return fileName;
    }
