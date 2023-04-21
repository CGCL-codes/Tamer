    @Override
    protected List<ImageListCell> doInBackground() throws Exception {
        ArrayList<ImageListCell> result = new ArrayList<ImageListCell>();
        MessageDigest digest = MessageDigest.getInstance("MD5");
        String md5String;
        if (listFiles.size() <= 1) GUI.instance(false).getProgressBar().setStringPainted(false);
        for (ImageListCell img : listFiles) {
            FileInputStream is = new FileInputStream(img.getSource());
            if (GUI.instance(false).getProgressBar().getValue() < listFiles.indexOf(img)) GUI.instance(false).getProgressBar().setValue(listFiles.indexOf(img));
            byte[] buffer = new byte[8192];
            int read = 0;
            try {
                while ((read = is.read(buffer)) > 0) {
                    digest.update(buffer, 0, read);
                }
                byte[] md5sum = digest.digest();
                BigInteger bigInt = new BigInteger(1, md5sum);
                md5String = bigInt.toString(16);
            } catch (IOException e) {
                throw new RuntimeException("Unable to process file for MD5", e);
            } finally {
                try {
                    is.close();
                } catch (IOException e) {
                    throw new RuntimeException("Unable to close input stream for MD5 calculation", e);
                }
            }
            thumbName = JIConfigurator.instance().getProperties().getProperty(JIConfigurator.JIMAGICK_WORKING_DIR, System.getProperty("user.home") + "/.jimagick/") + "thumbs/" + md5String + ".jpg";
            File thumb = new File(thumbName);
            thumb.getParentFile().mkdir();
            if (!thumb.exists()) createThumbnail(img.getSource().getAbsolutePath(), 100, 100, 100, thumbName);
            ImageListCell ilc = img.setParam(thumb, md5String);
            ilc.LoadXmlTag(DomXml.instance().getXmlDoc());
            publish(ilc);
            result.add(ilc);
        }
        return result;
    }
