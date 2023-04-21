    public byte[] showArchive(HTTPurl urlData, OutputStream outStream) throws Exception {
        PageTemplate template = new PageTemplate(store.getProperty("path.template") + File.separator + "ArchiveList.html");
        StringBuffer buff = new StringBuffer();
        File outFile = new File(new DllWrapper().getAllUserPath() + "archive");
        if (outFile.exists() == false) outFile.mkdirs();
        File[] files = outFile.listFiles();
        Arrays.sort(files);
        for (int x = files.length - 1; files != null && x >= 0; x--) {
            File archiveFile = files[x];
            if (archiveFile.isDirectory() == false && archiveFile.getName().startsWith("Schedule-")) {
                buff.append("<tr>\n");
                buff.append("<td>");
                buff.append("<a href='/servlet/ArchiveDataRes?action=showItemInfo&file=" + URLEncoder.encode(archiveFile.getName(), "UTF-8") + "'>");
                buff.append("<img src='/images/log.png' border='0' alt='Schedule Log' width='24' height='24'></a> ");
                buff.append("<a href='/servlet/ArchiveDataRes?action=deleteArchiveFile&file=" + URLEncoder.encode(archiveFile.getName(), "UTF-8") + "'>");
                buff.append("<img src='/images/delete.png' border='0' alt='Schedule Log' width='24' height='24'></a> ");
                buff.append("</td>");
                buff.append("<td style='padding-left:20px;'> " + archiveFile.getName() + " </td>");
                buff.append("</tr>\n");
            }
        }
        template.replaceAll("$ArchiveList", buff.toString());
        return template.getPageBytes();
    }
