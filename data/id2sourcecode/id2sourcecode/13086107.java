    private boolean calculateURN() {
        int urnCalculationMode = LibraryPrefs.UrnCalculationMode.get().intValue();
        FileInputStream inStream = null;
        try {
            inStream = new FileInputStream(shareFile.getSystemFile());
            MessageDigest messageDigest = new SHA1();
            byte[] buffer = new byte[64 * 1024];
            int length;
            long start = System.currentTimeMillis();
            long start2 = System.currentTimeMillis();
            while ((length = inStream.read(buffer)) != -1) {
                messageDigest.update(buffer, 0, length);
                long end2 = System.currentTimeMillis();
                try {
                    Thread.sleep((end2 - start2) * urnCalculationMode);
                } catch (InterruptedException exp) {
                    Thread.currentThread().interrupt();
                    return false;
                }
                start2 = System.currentTimeMillis();
            }
            inStream.close();
            byte[] shaDigest = messageDigest.digest();
            long end = System.currentTimeMillis();
            URN urn = new URN("urn:sha1:" + Base32.encode(shaDigest));
            shareFile.setURN(urn);
            if (NLogger.isDebugEnabled(UrnCalculationWorker.class)) {
                NLogger.debug(UrnCalculationWorker.class, "SHA1 time: " + (end - start) + " size: " + shareFile.getSystemFile().length());
            }
            SWDownloadFile file = downloadService.getDownloadFileByURN(urn);
            if (file != null) {
                AltLocContainer altCont = file.getGoodAltLocContainer();
                shareFile.getAltLocContainer().addContainer(altCont);
            }
            return true;
        } catch (IOException exp) {
            NLogger.debug(UrnCalculationWorker.class, exp, exp);
            return false;
        } finally {
            IOUtil.closeQuietly(inStream);
        }
    }
