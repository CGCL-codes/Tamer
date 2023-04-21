    private void writeDOMtoFile(Document doc, File file) {
        try {
            ZipOutputStream outputStream = new ZipOutputStream(new FileOutputStream(file));
            ZipEntry zipEntry = new ZipEntry(ITasksUiConstants.OLD_TASK_LIST_FILE);
            outputStream.putNextEntry(zipEntry);
            outputStream.setMethod(ZipOutputStream.DEFLATED);
            writeDOMtoStream(doc, outputStream);
            outputStream.flush();
            outputStream.closeEntry();
            outputStream.close();
        } catch (Exception fnfe) {
            StatusHandler.log(fnfe, "TaskList could not be found");
        }
    }
