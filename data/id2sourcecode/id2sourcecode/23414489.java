    public void run() {
        try {
            setStatus(TaskStatus.PROCESSING);
            logger.info("Started saving peak list " + peakList.getName());
            FileOutputStream fos = new FileOutputStream(fileName);
            OutputStream finalStream = fos;
            if (compression) {
                ZipOutputStream zos = new ZipOutputStream(fos);
                zos.setLevel(9);
                zos.putNextEntry(new ZipEntry(fileName.getName()));
                finalStream = zos;
            }
            Hashtable<RawDataFile, String> dataFilesIDMap = new Hashtable<RawDataFile, String>();
            for (RawDataFile file : peakList.getRawDataFiles()) {
                dataFilesIDMap.put(file, file.getName());
            }
            peakListSaveHandler = new PeakListSaveHandler(finalStream, dataFilesIDMap);
            peakListSaveHandler.savePeakList(peakList);
        } catch (Exception e) {
            if (getStatus() == TaskStatus.PROCESSING) {
                setStatus(TaskStatus.ERROR);
            }
            errorMessage = e.toString();
            e.printStackTrace();
            return;
        }
        logger.info("Finished saving " + peakList.getName());
        setStatus(TaskStatus.FINISHED);
    }
