    private void copyRedlines() throws IOException {
        out.flush();
        if (noRedlines == false) FileUtils.copyFile(redlinesTmpFile, outStream);
        redlinesTmpFile.delete();
    }
