        public void run() {
            String urlString = url.toString();
            int sep = urlString.lastIndexOf("/");
            String filename = urlString.substring(sep + 1);
            File destFile = new File(PLUGIN_DIRECTORY + File.separator + filename);
            if (destFile.exists()) {
                if (!dialogs.overwriteFile(filename)) {
                    return;
                }
            }
            FileOutputStream fos = null;
            try {
                fos = new FileOutputStream(destFile);
            } catch (FileNotFoundException e1) {
                e1.printStackTrace();
            }
            System.out.println(destFile.toString());
            try {
                URLConnection uc = url.openConnection();
                InputStream in = uc.getInputStream();
                ProgressMonitorInputStream pmis = new ProgressMonitorInputStream(null, "Downloading " + filename, in);
                ProgressMonitor pm = pmis.getProgressMonitor();
                pm.setMaximum(uc.getContentLength());
                int num = 0;
                byte b[] = new byte[100];
                try {
                    while ((num = pmis.read(b)) != -1) {
                        fos.write(b, 0, num);
                    }
                    fos.close();
                    pmis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e2) {
                e2.printStackTrace();
            }
            try {
                pluginManager.installPlugin(destFile);
            } catch (PopeyeException e) {
                dialogs.showErrorMessage(e.getMessage());
            }
        }
