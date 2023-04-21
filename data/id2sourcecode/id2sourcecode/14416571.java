    private void exportSpindles(String level, long id, boolean soft) throws IOException {
        String content = getSpindlesExportContent(level, id, soft);
        FileUtils.writeStringToFile(new File(ServletUtil.getSessionExpPath(getThreadLocalRequest().getSession()) + "/spindles.tsv"), content);
    }
