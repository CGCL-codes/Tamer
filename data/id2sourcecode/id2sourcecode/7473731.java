    private void copyFile(File source, File destination) throws Exception {
        File finalDestination = new File(destination, source.getName());
        String details = "";
        try {
            if (source.isDirectory()) {
                try {
                    details += "Copy dir STATE: " + source.getAbsolutePath() + " ==> " + finalDestination.getAbsolutePath();
                    FileUtils.copyDirectory(source, finalDestination);
                } catch (IOException e) {
                    throw e;
                }
            } else {
                try {
                    details += "Copy file STATE: " + source.getAbsolutePath() + " ==> " + finalDestination.getAbsolutePath();
                    FileUtils.copyFile(source, finalDestination = new File(destination, source.getName()));
                } catch (IOException e) {
                    report.report("Copy file STATE: " + source.getAbsolutePath() + " ==> " + (finalDestination != null ? finalDestination.getAbsolutePath() : "null"));
                    throw e;
                }
            }
            details = details.replace("STATE", "<b>Ok</b>");
        } catch (Exception e) {
            details = details.replace("STATE", "<b>Failed</b>");
            throw e;
        } finally {
            report(details);
        }
    }
