    protected int exportTemplateResource(String rfsName, String vfsName, StringBuffer cookies) throws IOException {
        CmsStaticExportManager manager = OpenCms.getStaticExportManager();
        String exportUrlStr = manager.getExportUrl() + manager.getRfsPrefix(vfsName) + rfsName;
        if (LOG.isDebugEnabled()) {
            LOG.debug(Messages.get().getBundle().key(Messages.LOG_SENDING_REQUEST_2, rfsName, exportUrlStr));
        }
        URL exportUrl = new URL(exportUrlStr);
        HttpURLConnection.setFollowRedirects(false);
        HttpURLConnection urlcon = (HttpURLConnection) exportUrl.openConnection();
        urlcon.setRequestMethod(REQUEST_METHOD_GET);
        urlcon.setRequestProperty(CmsRequestUtil.HEADER_OPENCMS_EXPORT, CmsStringUtil.TRUE);
        if (manager.getAcceptLanguageHeader() != null) {
            urlcon.setRequestProperty(CmsRequestUtil.HEADER_ACCEPT_LANGUAGE, manager.getAcceptLanguageHeader());
        } else {
            urlcon.setRequestProperty(CmsRequestUtil.HEADER_ACCEPT_LANGUAGE, manager.getDefaultAcceptLanguageHeader());
        }
        if (manager.getAcceptCharsetHeader() != null) {
            urlcon.setRequestProperty(CmsRequestUtil.HEADER_ACCEPT_CHARSET, manager.getAcceptCharsetHeader());
        } else {
            urlcon.setRequestProperty(CmsRequestUtil.HEADER_ACCEPT_CHARSET, manager.getDefaultAcceptCharsetHeader());
        }
        String exportFileName = CmsFileUtil.normalizePath(manager.getExportPath(vfsName) + rfsName);
        File exportFile = new File(exportFileName);
        long dateLastModified = exportFile.lastModified();
        if (vfsName.startsWith(CmsWorkplace.VFS_PATH_SYSTEM)) {
            Iterator it = manager.getRfsRules().iterator();
            while (it.hasNext()) {
                CmsStaticExportRfsRule rule = (CmsStaticExportRfsRule) it.next();
                if (rule.match(vfsName)) {
                    exportFileName = CmsFileUtil.normalizePath(rule.getExportPath() + rfsName);
                    exportFile = new File(exportFileName);
                    if (dateLastModified > exportFile.lastModified()) {
                        dateLastModified = exportFile.lastModified();
                    }
                }
            }
        }
        urlcon.setIfModifiedSince(dateLastModified);
        if (LOG.isDebugEnabled()) {
            LOG.debug(Messages.get().getBundle().key(Messages.LOG_IF_MODIFIED_SINCE_SET_2, exportFile.getName(), new Long((dateLastModified / 1000) * 1000)));
        }
        if (cookies.length() > 0) {
            urlcon.setRequestProperty(REQUEST_PROPERTY_COOKIE, cookies.toString());
        }
        urlcon.connect();
        int status = urlcon.getResponseCode();
        if (cookies.length() == 0) {
            cookies.append(urlcon.getHeaderField(HEADER_FIELD_SET_COOKIE));
            if (LOG.isDebugEnabled()) {
                LOG.debug(Messages.get().getBundle().key(Messages.LOG_STATICEXPORT_COOKIES_1, cookies));
            }
        }
        urlcon.disconnect();
        if (LOG.isInfoEnabled()) {
            LOG.info(Messages.get().getBundle().key(Messages.LOG_REQUEST_RESULT_3, rfsName, exportUrlStr, new Integer(status)));
        }
        return status;
    }
