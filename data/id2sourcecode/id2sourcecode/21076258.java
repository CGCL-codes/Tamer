    private void writeTaskLists(ZipOutputStream zipOut, Collection taskListNames) throws IOException {
        Map schedules = getEVSchedules(taskListNames);
        zipOut.putNextEntry(new ZipEntry(EV_FILE_NAME));
        EVExporter exp = new EVExporterXMLv1();
        exp.export(zipOut, schedules);
        zipOut.closeEntry();
    }
