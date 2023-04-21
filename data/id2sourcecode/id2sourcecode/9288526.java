    public void download(Tasks task, Version v) throws MalformedURLException, IOException, VersionException {
        task.setOperationName("Downloading EDACC " + v);
        java.net.URL url = new java.net.URL(v.getLocation());
        java.net.URLConnection connection = url.openConnection();
        int length = connection.getContentLength();
        java.io.BufferedInputStream in = new java.io.BufferedInputStream(connection.getInputStream());
        java.io.FileOutputStream fos = new java.io.FileOutputStream(edacc.experiment.Util.getPath() + System.getProperty("file.separator") + "update.zip");
        java.io.BufferedOutputStream bout = new BufferedOutputStream(fos, 1024 * 24);
        byte data[] = new byte[1024 * 24];
        int count;
        int cur = 0;
        while ((count = in.read(data, 0, 1024 * 24)) > 0) {
            bout.write(data, 0, count);
            cur += count;
            task.setStatus(edacc.experiment.Util.convertUnit(cur) + " / " + edacc.experiment.Util.convertUnit(length));
            task.setTaskProgress((float) cur / (float) length);
        }
        bout.close();
        fos.close();
        in.close();
        task.setTaskProgress(0f);
        task.setStatus("Validating update.zip");
        try {
            String md5 = edacc.manageDB.Util.calculateMD5(new File(edacc.experiment.Util.getPath() + System.getProperty("file.separator") + "update.zip"));
            if (v.getMd5() == null || !v.getMd5().equals(md5)) {
                throw new VersionException("Error while validating md5 checksum. Please try again.");
            }
        } catch (FileNotFoundException ex) {
            throw new VersionException("Error while validating md5 checksum. Please try again.");
        } catch (NoSuchAlgorithmException ex) {
            throw new VersionException("Error while validating md5 checksum. Please try again.");
        }
    }
