    private void processAssetRequest(String assetPath) {
        assetPath = assetPath.substring(6, assetPath.length());
        String packageName = assetPath.substring(0, assetPath.lastIndexOf("/"));
        String assetName = assetPath.replace(packageName, "");
        String mimeType = context.getMimeType(assetName);
        response.setContentType(mimeType);
        boolean developmentMode = false;
        if (context.getAttribute("wheelDevelopmentMode") != null) developmentMode = true;
        int expires = 0;
        if (!developmentMode) {
            String expiresS = request.getParameter("expires");
            try {
                expires = Integer.parseInt(expiresS);
            } catch (NumberFormatException e) {
                log.warn("Could not get a valid expiration time for an asset. Recieved value was '" + expiresS + "'.");
            }
            if (expires > 0) {
                Calendar cal = new GregorianCalendar();
                response.setDateHeader("Date", cal.getTimeInMillis());
                cal.add(Calendar.HOUR_OF_DAY, expires);
                response.setDateHeader("Expires", cal.getTimeInMillis());
                response.setHeader("Cache-Control", "cache");
                response.setHeader("Pragma", "cache");
            }
        }
        Map<String, byte[]> assetCache = (Map<String, byte[]>) context.getAttribute("wheelAssetCache");
        if (assetCache == null) assetCache = new HashMap<String, byte[]>();
        InputStream is = null;
        try {
            if (assetCache.containsKey(assetPath)) {
                is = new ByteArrayInputStream(assetCache.get(assetPath));
                OutputStream out = response.getOutputStream();
                byte[] buffer = new byte[4096];
                int read = is.read(buffer);
                while (read > 0) {
                    out.write(buffer, 0, read);
                    read = is.read(buffer);
                }
                out.flush();
                out.close();
            } else {
                is = resourceManager.loadAsset(packageName + assetName);
                if (!mimeType.equals("text/css")) {
                    ByteArrayOutputStream toCache = new ByteArrayOutputStream();
                    OutputStream out = response.getOutputStream();
                    byte[] buffer = new byte[4096];
                    int read = is.read(buffer);
                    while (read > 0) {
                        out.write(buffer, 0, read);
                        toCache.write(buffer, 0, read);
                        read = is.read(buffer);
                    }
                    out.flush();
                    out.close();
                    assetCache.put(assetPath, toCache.toByteArray());
                } else {
                    OutputStream out = new ByteArrayOutputStream();
                    byte[] buffer = new byte[4096];
                    int read = is.read(buffer);
                    while (read > 0) {
                        out.write(buffer, 0, read);
                        read = is.read(buffer);
                    }
                    String cssString = ((ByteArrayOutputStream) out).toString(encoding);
                    AssetProcessor assetProcessor = new AssetProcessor();
                    byte[] result = assetProcessor.replaceUrls(cssString, request.getContextPath() + request.getServletPath() + "/asset/" + packageName, expires).getBytes();
                    assetCache.put(assetPath, result);
                    ByteArrayInputStream bin = new ByteArrayInputStream(result);
                    out = response.getOutputStream();
                    read = bin.read(buffer);
                    while (read > 0) {
                        out.write(buffer, 0, read);
                        read = bin.read(buffer);
                    }
                    out.flush();
                    out.close();
                }
            }
        } catch (Exception e) {
            log.error("Failed to load asset '" + assetPath + "'.", e);
        } finally {
            try {
                if (is != null) is.close();
            } catch (IOException e) {
            }
            context.setAttribute("wheelAssetCache", assetCache);
        }
    }
