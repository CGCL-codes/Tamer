    @Override
    public WOResponse handleRequest(WORequest request) {
        WOResponse result = new WOResponse();
        NSArray<String> pathArray = request.requestHandlerPathArray();
        String bundleName = (String) pathArray.objectAtIndex(0);
        int pathArrayCount = pathArray.count();
        String path = pathArrayCount >= 2 ? pathArray.subarrayWithRange(new NSRange(1, pathArrayCount - 2)).componentsJoinedByString("/") : pathArray.lastObject();
        String name = pathArray.lastObject();
        String pathName = path + "/" + name;
        String contentType = "text/html";
        String prefix = "/cgi-bin/WebObjects/" + WOApplication.application().name() + ".woa";
        if ("SproutCore".equals(bundleName)) {
            pathName = pathName.replaceAll("\\.\\.+", "");
            File file = new File(SCUtilities.scBase(), pathName);
            if (!file.exists()) {
                pathName = path + "/images/" + name;
                file = new File(SCUtilities.scBase(), pathName);
            }
            byte data[];
            try {
                data = ERXFileUtilities.bytesFromFile(file);
                if (pathName.endsWith(".css")) {
                    String code = new String(data);
                    code = code.replaceAll("static_url\\([\"\'](.*?\\..*?)[\"\']\\)", "url($1)");
                    code = code.replaceAll("static_url\\([\"\'](.*?)[\"\']\\)", "url($1.png)");
                    data = code.getBytes();
                    contentType = "text/css";
                } else if (pathName.endsWith(".js")) {
                    String code = new String(data);
                    code = code.replaceAll("static_url\\([\"\']blank[\"\']\\)", "'" + prefix + "/_sc_/SproutCore/sproutcore/english.lproj/blank.gif'");
                    code = code.replaceAll("static_url\\([\"\'](.*?\\..*?)[\"\']\\)", "'" + prefix + "/_sc_/SproutCore/sproutcore/english.lproj/$1'");
                    code = code.replaceAll("static_url\\([\"\'](.*?)[\"\']\\)", "'" + prefix + "/_sc_/SproutCore/sproutcore/english.lproj/$1" + ".png'");
                    code = code.replaceAll("sc_super\\((\\s*?)\\)", "arguments.callee.base.apply(this, arguments)");
                    code = code.replaceAll("sc_super\\((.*?)\\)", "arguments.callee.base.apply($1)");
                    data = code.getBytes();
                    contentType = "text/javascript";
                }
            } catch (IOException e) {
                throw NSForwardException._runtimeExceptionForThrowable(e);
            }
            result.setContent(new NSData(data));
        } else {
            NSBundle bundle = NSBundle.bundleForName(bundleName);
            if ("app".equals(bundleName)) {
                bundle = NSBundle.mainBundle();
            }
            if (bundle != null) {
                try {
                    URL url = bundle.pathURLForResourcePath(pathName);
                    if (url != null) {
                        InputStream is = url.openStream();
                        byte data[] = ERXFileUtilities.bytesFromInputStream(is);
                        if (pathName.endsWith(".css")) {
                            String code = new String(data);
                            code = code.replaceAll("static_url\\([\"\'](.*?\\..*?)[\"\']\\)", "url($1)");
                            code = code.replaceAll("static_url\\([\"\'](.*?)[\"\']\\)", "url($1.png)");
                            data = code.getBytes();
                            contentType = "text/css";
                        } else if (pathName.endsWith(".js")) {
                            String code = new String(data);
                            code = code.replaceAll("static_url\\([\"\']blank[\"\']\\)", "'" + prefix + "/_sc_/" + bundleName + "/english.lproj/blank.gif'");
                            code = code.replaceAll("static_url\\([\"\'](photos/.+)[\"\']\\)", "'" + prefix + "/_sc_/SproutCore/common_assets/english.lproj/$1'");
                            code = code.replaceAll("static_url\\([\"\'](.*?\\..*?)[\"\']\\)", "'" + prefix + "/_sc_/" + bundleName + "/english.lproj/$1'");
                            code = code.replaceAll("static_url\\([\"\'](.*?)[\"\']\\)", "'" + prefix + "/_sc_/" + bundleName + "/english.lproj/$1" + ".png'");
                            code = code.replaceAll("sc_super\\((\\s*?)\\)", "arguments.callee.base.apply(this, arguments)");
                            code = code.replaceAll("sc_super\\((.*?)\\)", "arguments.callee.base.apply($1)");
                            data = code.getBytes();
                            contentType = "text/javascript";
                        }
                        result.setContent(new NSData(data));
                    } else {
                        log.debug("No URL for pathName: " + pathName);
                    }
                } catch (IOException e) {
                    throw NSForwardException._runtimeExceptionForThrowable(e);
                }
            }
        }
        result.setHeader(contentType, "Content-Type");
        return result;
    }
