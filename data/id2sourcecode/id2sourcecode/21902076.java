    public String exportAllProjectsToZip() {
        List<PlanProperties> ppList = em.createQuery("select p from PlanProperties p").getResultList();
        if (!ppList.isEmpty()) {
            log.debug("number of plans to export: " + ppList.size());
            String filename = "allprojects.zip";
            String exportPath = OS.getTmpPath() + "export" + System.currentTimeMillis() + "/";
            new File(exportPath).mkdirs();
            String binarydataTempPath = exportPath + "binarydata/";
            File binarydataTempDir = new File(binarydataTempPath);
            binarydataTempDir.mkdirs();
            try {
                OutputStream out = new BufferedOutputStream(new FileOutputStream(exportPath + filename));
                ZipOutputStream zipOut = new ZipOutputStream(out);
                for (PlanProperties pp : ppList) {
                    log.debug("EXPORTING: " + pp.getName());
                    ZipEntry zipAdd = new ZipEntry(String.format("%1$03d", pp.getId()) + "-" + FileUtils.makeFilename(pp.getName()) + ".xml");
                    zipOut.putNextEntry(zipAdd);
                    exportComplete(pp.getId(), zipOut, binarydataTempPath);
                    zipOut.closeEntry();
                }
                zipOut.close();
                out.close();
                new File(exportPath + "finished.info").createNewFile();
                FacesMessages.instance().add(FacesMessage.SEVERITY_INFO, "Export was written to: " + exportPath);
                log.info("Export was written to: " + exportPath);
            } catch (IOException e) {
                FacesMessages.instance().add(FacesMessage.SEVERITY_ERROR, "An error occured while generating the export file.");
                log.error("An error occured while generating the export file.", e);
                File errorInfo = new File(exportPath + "error.info");
                try {
                    Writer w = new FileWriter(errorInfo);
                    w.write("An error occured while generating the export file:");
                    w.write(e.getMessage());
                    w.close();
                } catch (IOException e1) {
                    log.error("Could not write error file.");
                }
            } finally {
                OS.deleteDirectory(binarydataTempDir);
            }
        } else {
            FacesMessages.instance().add("No Projects found!");
        }
        return null;
    }
