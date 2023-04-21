    protected boolean download(final File home, final Link link) {
        logger.trace("download {}", link);
        if ((link.getUrl() == null) || "".equals(link.getUrl())) {
            return false;
        }
        final String linkUrl = updateSite + link.getUrl();
        final File target = new File(home, link.getName().replace("/", File.pathSeparator));
        if (target.exists()) {
            target.delete();
        }
        if (!target.getParentFile().exists()) {
            target.getParentFile().mkdirs();
        }
        logger.debug("Download {} to {}", linkUrl, target.getPath());
        URL url = null;
        try {
            url = new URL(linkUrl);
        } catch (final MalformedURLException ex) {
            logger.error("Downloading {} ", linkUrl, ex);
            target.delete();
            return false;
        }
        InputStream inputStream = null;
        OutputStream outputStream = null;
        try {
            final URLConnection conn = url.openConnection();
            inputStream = conn.getInputStream();
            logger.debug("Downloading file, update Size(compressed): {} Bytes", conn.getContentLength());
            outputStream = new BufferedOutputStream(new FileOutputStream(target));
            FileUtils.copyStream(inputStream, outputStream);
            outputStream.flush();
            final Boolean check = link != null ? CheckSum.validate(target.getPath(), link.getChecksum()) : Boolean.TRUE;
            logger.debug("Download Complete! {}", check);
            return check;
        } catch (final IOException ex) {
            logger.error("Downloading {} ", linkUrl, ex);
            target.delete();
            return false;
        } finally {
            FileUtils.close(inputStream);
            FileUtils.close(outputStream);
        }
    }
