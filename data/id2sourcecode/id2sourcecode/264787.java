    private byte[] showFileList(HTTPurl urlData) throws Exception {
        String pathString = urlData.getParameter("path");
        File[] files = null;
        File baseDir = null;
        boolean inBounds = false;
        String[] paths = store.getCapturePaths();
        DllWrapper capEng = new DllWrapper();
        boolean showPlay = store.getProperty("filebrowser.ShowWsPlay").equals("1");
        if (pathString != null) {
            File thisPath = new File(pathString);
            String requestedFilePath = thisPath.getCanonicalPath();
            for (int x = 0; x < paths.length; x++) {
                String rootFilePath = new File(paths[x]).getCanonicalPath();
                if (requestedFilePath.indexOf(rootFilePath) == 0) {
                    inBounds = true;
                    break;
                }
            }
        }
        if (inBounds == false) {
            files = new File[paths.length];
            for (int x = 0; x < paths.length; x++) {
                files[x] = new File(paths[x]);
            }
        } else {
            baseDir = new File(pathString);
            String fileMasks = DataStore.getInstance().getProperty("filebrowser.masks");
            files = baseDir.listFiles(new FileTypeFilter(fileMasks));
        }
        StringBuffer buff = new StringBuffer(2048);
        NumberFormat nf = NumberFormat.getInstance();
        PageTemplate template = new PageTemplate(store.getProperty("path.template") + File.separator + "FileList.html");
        boolean dirsAtTop = "1".equals(store.getProperty("filebrowser.DirsAtTop"));
        Arrays.sort(files, new CompareFiles(dirsAtTop));
        if (baseDir != null) {
            File parent = baseDir.getParentFile();
            if (parent == null) buff.append("<tr><td colspan='3'><strong><a style='text-decoration: none; color: #FFFFFF;' href='/servlet/" + urlData.getServletClass() + "'>"); else buff.append("<tr><td colspan='3'><strong><a style='text-decoration: none; color: #FFFFFF;' href='/servlet/" + urlData.getServletClass() + "?path=" + URLEncoder.encode(baseDir.getParentFile().getCanonicalPath(), "UTF-8") + "'>");
            buff.append("&lt;parent&gt;</a></strong></td></tr>\n");
        }
        for (int x = 0; x < files.length; x++) {
            if (inBounds == false) {
                buff.append("<tr><td colspan='2'><strong><a style='text-decoration: none; color: #FFFFFF;' href='/servlet/" + urlData.getServletClass() + "?path=" + URLEncoder.encode(files[x].getCanonicalPath(), "UTF-8") + "'>");
                buff.append(files[x].getCanonicalPath() + "</a> ");
                long freeSpace = capEng.getFreeSpace(files[x].getCanonicalPath());
                buff.append("(" + nf.format((freeSpace / (1024 * 1024))) + " MB Free)");
                buff.append("</strong></td>");
                buff.append("<td></td></tr>\n");
            } else if (files[x].isDirectory() && files[x].isHidden() == false) {
                buff.append("<tr><td colspan='2'><strong><a style='text-decoration: none; color: #FFFFFF;' href='/servlet/" + urlData.getServletClass() + "?path=" + URLEncoder.encode(files[x].getCanonicalPath(), "UTF-8") + "'>");
                buff.append("&lt;" + files[x].getName() + "&gt;</a></strong></td>");
                buff.append("<td><a class='noUnder' onClick='return confirmAction(\"" + URLEncoder.encode(files[x].getPath(), "UTF-8") + "\");' href='#'><img align='absmiddle' src='/images/delete.png' border='0' alt='Delete' width='24' height='24'></a></td></tr>\n");
            } else if (files[x].isHidden() == false) {
                buff.append("<tr><td>" + files[x].getName() + "</td><td>" + nf.format(files[x].length() / 1024) + " KB</td>");
                buff.append("<td><a class='noUnder' onClick='return confirmAction(\"" + URLEncoder.encode(files[x].getPath(), "UTF-8") + "\");' href='#'><img align='absmiddle' src='/images/delete.png' border='0' alt='Delete' width='24' height='24'></a> ");
                buff.append("<a class='noUnder' href='/servlet/" + urlData.getServletClass() + "?action=02&file=" + URLEncoder.encode(files[x].getPath(), "UTF-8") + "'><img align='absmiddle' src='/images/RunTaskSmall.png' border='0' alt='Run Task' width='24' height='24'></a> ");
                if (showPlay) buff.append("<a class='noUnder' href='wsplay://ws/" + URLEncoder.encode(files[x].getPath(), "UTF-8") + "'><img align='absmiddle' src='/images/play.png' border='0' alt='Play file using wsplay protocol' width='24' height='24'></a> ");
                buff.append("</td></tr>\n");
            }
        }
        template.replaceAll("$fileList", buff.toString());
        return template.getPageBytes();
    }
