    private void extractResourcesFromJar(File outputDir, Resource[] resources) throws FileNotFoundException, IOException {
        for (int i = 0; i < resources.length; i++) {
            File target = new File(outputDir, resources[i].getFilename());
            logger.debug("copying media resource [" + target.getAbsolutePath() + "]");
            FileOutputStream fos = new FileOutputStream(target);
            InputStream is = resources[i].getInputStream();
            byte[] buff = new byte[1];
            while (is.read(buff) != -1) fos.write(buff);
            fos.flush();
            fos.close();
            is.close();
        }
    }
