    public void start(PipelineContext pipelineContext) {
        ExternalContext externalContext = (ExternalContext) pipelineContext.getAttribute(org.orbeon.oxf.pipeline.api.PipelineContext.EXTERNAL_CONTEXT);
        ExternalContext.Response response = externalContext.getResponse();
        try {
            Node config = readCacheInputAsDOM4J(pipelineContext, INPUT_CONFIG);
            String imageDirectoryString = XPathUtils.selectStringValueNormalize(config, "/config/image-directory");
            imageDirectoryString = imageDirectoryString.replace('\\', '/');
            if (!imageDirectoryString.endsWith("/")) imageDirectoryString = imageDirectoryString + '/';
            URL imageDirectoryURL = URLFactory.createURL(imageDirectoryString);
            String cacheDirectoryString = XPathUtils.selectStringValueNormalize(config, "/config/cache/directory");
            File cacheDir = (cacheDirectoryString == null) ? null : new File(cacheDirectoryString);
            if (cacheDir != null && !cacheDir.isDirectory()) throw new IllegalArgumentException("Invalid cache directory: " + cacheDirectoryString);
            float defaultQuality = selectFloatValue(config, "/config/default-quality", DEFAULT_QUALITY);
            if (defaultQuality < 0.0f || defaultQuality > 1.0f) throw new IllegalArgumentException("default-quality must be comprised between 0.0 and 1.0");
            boolean useSandbox = selectBooleanValue(config, "/config/use-sandbox", DEFAULT_USE_SANDBOX);
            String cachePathEncoding = XPathUtils.selectStringValueNormalize(config, "/config/cache/path-encoding");
            Node imageConfig = readCacheInputAsDOM4J(pipelineContext, INPUT_IMAGE);
            String urlString = XPathUtils.selectStringValueNormalize(imageConfig, "/image/url");
            if (urlString == null) {
                urlString = XPathUtils.selectStringValueNormalize(imageConfig, "/image/path");
            }
            float quality = selectFloatValue(imageConfig, "/image/quality", defaultQuality);
            boolean useCache = cacheDir != null && selectBooleanValue(imageConfig, "/image/use-cache", DEFAULT_USE_CACHE);
            int transformCount = XPathUtils.selectIntegerValue(imageConfig, "count(/image/transform)").intValue();
            Object transforms = XPathUtils.selectObjectValue(imageConfig, "/image/transform");
            if (transforms != null && transforms instanceof Node) transforms = Collections.singletonList(transforms);
            Iterator transformIterator = XPathUtils.selectIterator(imageConfig, "/image/transform");
            URLConnection urlConnection = null;
            InputStream urlConnectionInputStream = null;
            try {
                URL newURL = null;
                try {
                    newURL = URLFactory.createURL(imageDirectoryURL, urlString);
                    boolean relative = NetUtils.relativeURL(imageDirectoryURL, newURL);
                    if (useSandbox && !relative) {
                        response.setStatus(ExternalContext.SC_NOT_FOUND);
                        return;
                    }
                    urlConnection = newURL.openConnection();
                    urlConnectionInputStream = urlConnection.getInputStream();
                    if (!urlConnectionInputStream.markSupported()) urlConnectionInputStream = new BufferedInputStream(urlConnectionInputStream);
                    String contentType = URLConnection.guessContentTypeFromStream(urlConnectionInputStream);
                    if (!"image/jpeg".equals(contentType)) {
                        response.setStatus(ExternalContext.SC_NOT_FOUND);
                        return;
                    }
                } catch (IOException e) {
                    response.setStatus(ExternalContext.SC_NOT_FOUND);
                    return;
                }
                long lastModified = NetUtils.getLastModified(urlConnection);
                String cacheFileName = useCache ? computeCacheFileName(cachePathEncoding, urlString, (List) transforms) : null;
                File cacheFile = useCache ? new File(cacheDir, cacheFileName) : null;
                boolean cacheInvalid = !useCache || !cacheFile.exists() || lastModified == 0 || lastModified > cacheFile.lastModified() || cacheFile.length() == 0;
                boolean mustProcess = cacheInvalid;
                boolean updateCache = useCache && cacheInvalid;
                response.setCaching(lastModified, false, false);
                if ((transformCount == 0 || !mustProcess) && !response.checkIfModifiedSince(lastModified, false)) {
                    response.setStatus(ExternalContext.SC_NOT_MODIFIED);
                    return;
                }
                response.setContentType("image/jpeg");
                if (transformCount == 0) {
                    NetUtils.copyStream(urlConnectionInputStream, response.getOutputStream());
                    return;
                }
                if (mustProcess) {
                    boolean closeOutputStream = false;
                    OutputStream os = null;
                    try {
                        Long cacheValidity = new Long(lastModified);
                        String cacheKey = "[" + newURL.toExternalForm() + "][" + cacheValidity + "]";
                        BufferedImage img1 = null;
                        synchronized (ImageServer.this) {
                            img1 = (cache == null) ? null : (BufferedImage) cache.get(cacheKey);
                            if (img1 == null) {
                                JPEGImageDecoder decoder = JPEGCodec.createJPEGDecoder(urlConnectionInputStream);
                                img1 = decoder.decodeAsBufferedImage();
                                if (cache == null) cache = new SoftCacheImpl(0);
                                cache.put(cacheKey, img1);
                            } else {
                                cache.refresh(cacheKey);
                                logger.info("Found decoded image in cache");
                            }
                        }
                        BufferedImage img2 = filter(img1, transformIterator);
                        if (updateCache) {
                            File outputDir = cacheFile.getParentFile();
                            if (!outputDir.exists() && !outputDir.mkdirs()) {
                                logger.info("Cannot create cache directory: " + outputDir.getCanonicalPath());
                                response.setStatus(ExternalContext.SC_INTERNAL_SERVER_ERROR);
                                return;
                            }
                            os = new FileOutputStream(cacheFile);
                            closeOutputStream = true;
                        } else {
                            os = response.getOutputStream();
                        }
                        JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(os);
                        JPEGEncodeParam params = encoder.getDefaultJPEGEncodeParam(img2);
                        params.setQuality(quality, false);
                        encoder.setJPEGEncodeParam(params);
                        encoder.encode(img2);
                    } catch (OXFException e) {
                        logger.error(OXFException.getRootThrowable(e));
                        response.setStatus(ExternalContext.SC_INTERNAL_SERVER_ERROR);
                        return;
                    } finally {
                        if (os != null && closeOutputStream) os.close();
                    }
                }
                if (useCache) {
                    InputStream is = new FileInputStream(cacheFile);
                    OutputStream os = response.getOutputStream();
                    try {
                        NetUtils.copyStream(is, os);
                    } finally {
                        is.close();
                    }
                }
            } finally {
                if (urlConnection != null && "file".equalsIgnoreCase(urlConnection.getURL().getProtocol())) {
                    if (urlConnectionInputStream != null) urlConnectionInputStream.close();
                }
            }
        } catch (OutOfMemoryError e) {
            logger.info("Ran out of memory while processing image");
            throw e;
        } catch (Exception e) {
            throw new OXFException(e);
        }
    }
