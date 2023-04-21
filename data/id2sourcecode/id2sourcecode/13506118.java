    private void copyMediaResources(File outputDir) {
        logger.debug("Copying media resources to output location");
        try {
            ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver(new DefaultResourceLoader());
            Resource[] media = resolver.getResources(MEDIA_RESOURCES);
            for (int i = 0; i < media.length; i++) {
                File target = new File(outputDir, media[i].getFilename());
                logger.debug("copying media resource [" + target.getAbsolutePath() + "]");
                FileOutputStream fos = new FileOutputStream(target);
                InputStream is = media[i].getInputStream();
                byte[] buff = new byte[1];
                while (is.read(buff) != -1) fos.write(buff);
                fos.flush();
                fos.close();
                is.close();
            }
        } catch (Exception e) {
            logger.error("Failed to move media resources to output directory", e);
        }
    }
