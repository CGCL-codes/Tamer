    public InputStream exportCurriculum(Curriculum curriculum) {
        byte[] buf = new byte[1024];
        String folderName = curriculum.getName().replaceAll(" ", "_");
        java.io.File folder = new java.io.File(tmpDirectory + "/" + folderName);
        folder.mkdir();
        String imsmanifest = tmpDirectory + "/" + folderName + "/imsmanifest.xml";
        ScormEventListener listener = new ScormExporterEventListener(imsmanifest);
        ScormEventListenerBroadcaster broadcaster = new ScormEventListenerBroadcaster();
        broadcaster.register(listener);
        exporter.setBroadcaster(broadcaster);
        exporter.processCurriculum(curriculum);
        ZipOutputStream out = null;
        String outFilename = tmpDirectory + "/" + curriculum.getName() + ".zip";
        try {
            out = new ZipOutputStream(new FileOutputStream(outFilename));
            FileInputStream in = new FileInputStream(imsmanifest);
            ZipEntry zipEntry = new ZipEntry("imsmanifest.xml");
            out.putNextEntry(zipEntry);
            transferBytes(buf, out, in);
            out.closeEntry();
            in.close();
            addXsdEntry(buf, out, in, "adlcp_rootv1p2.xsd");
            addXsdEntry(buf, out, in, "ims_xml.xsd");
            addXsdEntry(buf, out, in, "imscp_rootv1p1p2.xsd");
            addXsdEntry(buf, out, in, "imsmd_rootv1p2p1.xsd");
            out.flush();
            out.close();
            folder.delete();
            return new FileInputStream(outFilename);
        } catch (FileNotFoundException e) {
            logger.error(e);
        } catch (IOException e) {
            logger.error(e);
        }
        return null;
    }
