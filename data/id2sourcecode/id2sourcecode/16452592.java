    private void copyFile(File source, File destination) throws Exception {
        File finalDestination = new File(destination, source.getName());
        String details = "";
        try {
            if (source.isDirectory()) {
                details += "Copy dir STATE: " + source.getAbsolutePath() + " ==> " + finalDestination.getAbsolutePath();
                FileUtils.copyDirectory(source, finalDestination);
            } else {
                details += "Copy file STATE: " + source.getAbsolutePath() + " ==> " + finalDestination.getAbsolutePath();
                FileUtils.copyFile(source, finalDestination = new File(destination, source.getName()));
            }
            details = details.replace("STATE", "<b>Ok</b>");
        } catch (Exception e) {
            details = details.replace("STATE", "<b>Failed</b>");
            throw e;
        } finally {
            report(details, details.contains("<b>Ok</b>") ? Reporter.PASS : Reporter.FAIL);
        }
    }
