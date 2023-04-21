    private void loadBprs(String dir, String[] bprSeq) throws MalformedURLException, FileNotFoundException, IOException, AeBprException {
        File rootDir = new File(dir);
        File fbpr = new File(rootDir, Servent.SERVICE_CHAIN);
        File[] listBprs = fbpr.listFiles();
        if (listBprs != null) {
            File bprFile = null;
            for (int seq = 0; seq < bprSeq.length; seq++) {
                bprFile = getCorrectBprFile(bprSeq[seq], listBprs);
                FileUtils.copyFileToDirectory(bprFile, new File(m_bpr));
                LOGGER.info("deploying " + (seq + 1) + ". bpr=" + bprFile.getName() + " " + m_bpr);
            }
        }
    }
