    private void saveSpecificationToFile(String fullFileName) {
        if (fullFileName.equals("")) {
            return;
        }
        JStatusBar.getInstance().updateProgressOverSeconds(2);
        try {
            ZipOutputStream outputStream = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(fullFileName)));
            outputStream.putNextEntry(new ZipEntry("specification.xml"));
            XMLEncoder encoder = new XMLEncoder(outputStream);
            encoder.setExceptionListener(new ExceptionListener() {

                public void exceptionThrown(Exception exception) {
                    exception.printStackTrace();
                }
            });
            writeSpecification(encoder);
            encoder.close();
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        JStatusBar.getInstance().resetProgress();
    }
