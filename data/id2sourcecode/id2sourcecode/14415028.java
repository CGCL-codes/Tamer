    private static void readFile(File file, int option, PrintWriter writer) {
        try {
            DataAccessController controller = null;
            switch(option) {
                case 1:
                    if (PrideXmlControllerImpl.isValidFormat(file)) {
                        controller = new PrideXmlControllerImpl(file);
                    }
                    break;
                case 2:
                    if (MzMLControllerImpl.isValidFormat(file)) {
                        controller = new MzMLControllerImpl(file);
                    }
                    break;
            }
            if (controller != null) {
                writer.println("File: " + file.getAbsolutePath());
                controller.getMetaData();
                if (controller.hasSpectrum()) {
                    Collection<Comparable> ids = controller.getSpectrumIds();
                    int cnt = 0;
                    for (Comparable id : ids) {
                        controller.getSpectrumById(id);
                        cnt++;
                        if (cnt >= 50) {
                            break;
                        }
                    }
                }
                if (controller.hasChromatogram()) {
                    Collection<Comparable> ids = controller.getChromatogramIds();
                    int cnt = 0;
                    for (Comparable id : ids) {
                        controller.getChromatogramById(id);
                        cnt++;
                        if (cnt >= 50) {
                            break;
                        }
                    }
                }
                if (controller.hasIdentification()) {
                    Collection<Comparable> ids = controller.getIdentificationIds();
                    int cnt = 0;
                    for (Comparable id : ids) {
                        controller.getIdentificationById(id);
                        TableRowDataRetriever.getIdentificationTableRow(controller, id);
                        Collection<Comparable> pepIds = controller.getPeptideIds(id);
                        for (Comparable pepId : pepIds) {
                            TableRowDataRetriever.getPeptideTableRow(controller, id, pepId);
                        }
                        cnt++;
                        if (cnt >= 50) {
                            break;
                        }
                    }
                }
                controller.close();
                writer.println("-------------------------------------------------------------------------------");
                writer.flush();
            }
        } catch (Exception ex) {
            StackTraceElement[] traces = ex.getStackTrace();
            for (StackTraceElement trace : traces) {
                writer.println(trace.toString());
            }
            writer.println("-------------------------------------------------------------------------------");
            writer.flush();
        }
    }
