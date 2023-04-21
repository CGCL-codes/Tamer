    public void doAdmin() {
        HeaderParser requestHeader = getRequestHeader();
        MappingResult mapping = getRequestMapping();
        String selfPath = requestHeader.getRequestUri();
        String path = mapping.getResolvePath();
        if ("/".equals(path)) {
            if (!selfPath.endsWith("/")) {
                redirectAdmin();
                return;
            }
            path = "/admin.vsp";
            mapping.setResolvePath(path);
        }
        ParameterParser parameter = getParameterParser();
        if (path.endsWith(".vsp") || path.endsWith(".vsf")) {
            mapping.setDesitinationFile(config.getAdminDocumentRoot());
            forwardHandler(Mapping.VELOCITY_PAGE_HANDLER);
            return;
        } else if (path.startsWith("/storeDownload")) {
            forwardHandler(Mapping.STORE_HANDLER);
            return;
        } else if (path.startsWith("/viewAccessLog")) {
            viewAccessLog(parameter);
            return;
        }
        if (!checkToken(parameter)) {
            if (requestHeader.getMethod().equalsIgnoreCase(HeaderParser.POST_METHOD)) {
                logger.error("CSRF check error.path:" + path + ":cid:" + getChannelId());
                completeResponse("403", "token error.");
            } else {
                mapping.setOption(MappingResult.PARAMETER_VELOCITY_USE, "false");
                mapping.setDesitinationFile(config.getPublicDocumentRoot());
                forwardHandler(Mapping.FILE_SYSTEM_HANDLER);
            }
            return;
        }
        if ("/admin".equals(path)) {
            doCommand(parameter);
            return;
        } else if ("/accessLog".equals(path)) {
            forwardHandler(AdminAccessLogHandler.class);
            return;
        } else if (REPLAY_UPLOAD_PATH.equals(path)) {
            replayUpload(parameter);
            return;
        } else if (path.startsWith("/mapping")) {
            forwardHandler(AdminMappingHandler.class);
            return;
        } else if (path.startsWith("/realHost")) {
            forwardHandler(AdminRealHostHandler.class);
            return;
        } else if (path.startsWith("/user")) {
            forwardHandler(AdminUserHandler.class);
            return;
        } else if (path.startsWith("/perf")) {
            forwardHandler(AdminPerfHandler.class);
            return;
        } else if (path.startsWith("/filter")) {
            forwardHandler(AdminFilterHandler.class);
            return;
        } else if (path.startsWith("/commissionAuth")) {
            forwardHandler(AdminCommissionAuthHandler.class);
            return;
        }
        logger.error("admin not found error.path:" + path + ":cid:" + getChannelId());
        completeResponse("404");
    }
