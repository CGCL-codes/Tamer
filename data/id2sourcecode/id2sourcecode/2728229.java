    private File downloadAny(String address, String fileName) {
        File localFileName = new File(FileLocator.getDownloadDir(), fileName);
        OutputStream out = null;
        InputStream in = null;
        try {
            URL url = new URL(address);
            out = new BufferedOutputStream(new FileOutputStream(localFileName));
            in = url.openConnection().getInputStream();
            byte[] buffer = new byte[1024];
            int numRead;
            while ((numRead = in.read(buffer)) != -1) {
                out.write(buffer, 0, numRead);
            }
        } catch (Exception exception) {
            logger.warning(exception.getMessage());
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
                if (out != null) {
                    out.close();
                }
            } catch (IOException ioe) {
                logger.warning(ioe.getMessage());
            }
        }
        File completed = new File(FileLocator.getCompletedDir(), localFileName.getName());
        localFileName.renameTo(completed);
        logger.info(String.format("%s --> %s (%.1fKB)", address, localFileName, (double) (completed.length() / 1024)));
        return completed;
    }
