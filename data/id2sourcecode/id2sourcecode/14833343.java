    static void export(HttpServletResponse response, @Nullable String app) {
        try {
            response.setHeader("Expires", "-1");
            response.setHeader("Cache-Control", "private, max-age=0");
            response.setHeader("Content-Type", "text/xml");
            response.setHeader("Content-Disposition", "attachment; filename=\"bugs.zip\"");
            final ServletOutputStream outputStream = response.getOutputStream();
            final ZipOutputStream zipOutputStream = new ZipOutputStream(outputStream);
            try {
                zipOutputStream.putNextEntry(new ZipEntry("bugs.xml"));
                final CloseShieldOutputStream closeShieldOutputStream = new CloseShieldOutputStream(zipOutputStream);
                final Store store = StoreFactory.getStore();
                final Exporter exporter = new Exporter(store);
                if (app != null) {
                    exporter.exportApplicationBugs(closeShieldOutputStream, app);
                } else {
                    exporter.exportAll(closeShieldOutputStream);
                }
            } catch (XMLStreamException e) {
                response.sendError(500);
            } finally {
                zipOutputStream.closeEntry();
                zipOutputStream.close();
            }
        } catch (Exception e) {
            LOGGER.error("Failed to export", e);
            throw new IllegalStateException("Failed to export", e);
        }
    }
