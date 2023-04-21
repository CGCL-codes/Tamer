    @Override
    public void run() {
        while (true) {
            String currentBatch = null;
            try {
                currentBatch = waitingBatchDirs.take();
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy hh:mm:ss:SSS");
                writeToFile(currentBatch + "status.txt", "started at " + sdf.format(new Date()), true);
                writeToFile(currentBatch + "started.txt", sdf.format(new Date()), true);
                String fids = readFile(currentBatch + "fids.txt");
                String points = readFile(currentBatch + "points.txt");
                ArrayList<String> sample = layerIntersectDao.sampling(fids, points);
                FileOutputStream fos = new FileOutputStream(currentBatch + "sample.zip");
                ZipOutputStream zip = new ZipOutputStream(fos);
                zip.putNextEntry(new ZipEntry("sample.csv"));
                IntersectUtil.writeSampleToStream(fids.split(","), points.split(","), sample, zip);
                zip.close();
                fos.close();
                writeToFile(currentBatch + "status.txt", "finished at " + sdf.format(new Date()), true);
                writeToFile(currentBatch + "finished.txt", sdf.format(new Date()), true);
                currentBatch = null;
            } catch (Exception e) {
                if (currentBatch != null) {
                    try {
                        writeToFile(currentBatch + "status.txt", "error " + e.getMessage(), true);
                        writeToFile(currentBatch + "error.txt", e.getMessage(), true);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
                e.printStackTrace();
            }
        }
    }
