    public void actionShow() throws Exception {
        String resourceStr = getParamResource();
        if (CmsStringUtil.isNotEmpty(getParamVersion())) {
            byte[] result = getHistoricalResourceContent(getCms(), resourceStr, getParamVersion());
            if (result != null) {
                String contentType = OpenCms.getResourceManager().getMimeType(resourceStr, getCms().getRequestContext().getEncoding());
                HttpServletResponse res = getJsp().getResponse();
                HttpServletRequest req = getJsp().getRequest();
                res.setHeader(CmsRequestUtil.HEADER_CONTENT_DISPOSITION, new StringBuffer("attachment; filename=\"").append(resourceStr).append("\"").toString());
                res.setContentLength(result.length);
                CmsFlexController controller = CmsFlexController.getController(req);
                res = controller.getTopResponse();
                res.setContentType(contentType);
                try {
                    res.getOutputStream().write(result);
                    res.getOutputStream().flush();
                } catch (IOException e) {
                    if (LOG.isInfoEnabled()) {
                        LOG.info(e.getLocalizedMessage());
                    }
                    return;
                }
            }
        } else {
            CmsResource resource = null;
            try {
                resource = getCms().readResource(resourceStr, CmsResourceFilter.ALL);
            } catch (CmsVfsResourceNotFoundException e) {
            }
            if (resource != null) {
                if (resource.getState().isDeleted()) {
                    throw new CmsVfsResourceNotFoundException(Messages.get().container(Messages.ERR_RESOURCE_DELETED_2, resourceStr, getCms().getRequestContext().currentProject().getName()));
                }
                autoTimeWarp(resource);
                String url = getJsp().link(resourceStr);
                if ((url.indexOf("://") < 0) && getCms().getRequestContext().currentProject().isOnlineProject()) {
                    String site = getCms().getRequestContext().getSiteRoot();
                    if (CmsStringUtil.isEmptyOrWhitespaceOnly(site)) {
                        site = OpenCms.getSiteManager().getDefaultUri();
                        if (CmsStringUtil.isEmptyOrWhitespaceOnly(site)) {
                            url = OpenCms.getSiteManager().getWorkplaceServer() + url;
                        } else if (OpenCms.getSiteManager().getSiteForSiteRoot(site) == null) {
                            url = OpenCms.getSiteManager().getWorkplaceServer() + url;
                        } else {
                            url = OpenCms.getSiteManager().getSiteForSiteRoot(site).getUrl() + url;
                        }
                    } else {
                        url = OpenCms.getSiteManager().getSiteForSiteRoot(site).getUrl() + url;
                    }
                    try {
                        CmsStaticExportManager manager = OpenCms.getStaticExportManager();
                        HttpURLConnection.setFollowRedirects(false);
                        URL exportUrl = new URL(manager.getExportUrl() + manager.getRfsName(getCms(), resourceStr));
                        HttpURLConnection urlcon = (HttpURLConnection) exportUrl.openConnection();
                        urlcon.setRequestMethod("GET");
                        urlcon.setRequestProperty(CmsRequestUtil.HEADER_OPENCMS_EXPORT, Boolean.TRUE.toString());
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
                        urlcon.connect();
                        urlcon.getResponseCode();
                        urlcon.disconnect();
                    } catch (Exception e) {
                        if (LOG.isDebugEnabled()) {
                            LOG.debug(e.getLocalizedMessage(), e);
                        }
                    }
                }
                getJsp().getResponse().sendRedirect(url);
            } else {
                throw new CmsVfsResourceNotFoundException(Messages.get().container(Messages.ERR_RESOURCE_DOES_NOT_EXIST_3, resourceStr, getCms().getRequestContext().currentProject().getName(), getCms().getRequestContext().getSiteRoot()));
            }
        }
    }
