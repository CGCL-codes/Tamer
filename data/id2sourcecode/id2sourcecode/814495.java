    private byte[] showArchiveList(HTTPurl urlData) throws Exception {
        PageTemplate template = new PageTemplate(store.getProperty("path.template") + File.separator + "ArchiveTaskList.html");
        StringBuffer buff = new StringBuffer();
        File outFile = new File(new DllWrapper().getAllUserPath() + "archive");
        if (outFile.exists() == false) outFile.mkdirs();
        File[] files = outFile.listFiles();
        for (int x = 0; files != null && x < files.length; x++) {
            File archiveFile = files[x];
            if (archiveFile.isDirectory() == false && archiveFile.getName().startsWith("Task-")) {
                buff.append("<tr>\n");
                buff.append("<td>");
                buff.append("<a href='/servlet/TaskManagementDataRes?action=06&file=" + URLEncoder.encode(archiveFile.getName(), "UTF-8") + "'>");
                buff.append("<img src='/images/log.png' border='0' alt='Schedule Log' width='24' height='24'></a> ");
                buff.append("<a href='/servlet/TaskManagementDataRes?action=07&file=" + URLEncoder.encode(archiveFile.getName(), "UTF-8") + "'>");
                buff.append("<img src='/images/delete.png' border='0' alt='Schedule Log' width='24' height='24'></a> ");
                buff.append("</td>");
                buff.append("<td style='padding-left:20px;'>" + archiveFile.getName() + "</td>\n");
                buff.append("</tr>\n");
            }
        }
        template.replaceAll("$ArchiveList", buff.toString());
        return template.getPageBytes();
    }
