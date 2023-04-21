    protected void find(PackageScanFilter test, String packageName, ClassLoader loader, Set<Class<?>> classes) {
        if (log.isTraceEnabled()) {
            log.trace("Searching for: {} in package: {} using classloader: {}", new Object[] { test, packageName, loader.getClass().getName() });
        }
        Enumeration<URL> urls;
        try {
            urls = getResources(loader, packageName);
            if (!urls.hasMoreElements()) {
                log.trace("No URLs returned by classloader");
            }
        } catch (IOException ioe) {
            log.warn("Cannot read package: " + packageName, ioe);
            return;
        }
        while (urls.hasMoreElements()) {
            URL url = null;
            try {
                url = urls.nextElement();
                log.trace("URL from classloader: {}", url);
                url = customResourceLocator(url);
                String urlPath = url.getFile();
                urlPath = URLDecoder.decode(urlPath, "UTF-8");
                if (log.isTraceEnabled()) {
                    log.trace("Decoded urlPath: {} with protocol: {}", urlPath, url.getProtocol());
                }
                if (urlPath.startsWith("file:")) {
                    try {
                        urlPath = new URI(url.getFile()).getPath();
                    } catch (URISyntaxException e) {
                    }
                    if (urlPath.startsWith("file:")) {
                        urlPath = urlPath.substring(5);
                    }
                }
                if (url.toString().startsWith("bundle:") || urlPath.startsWith("bundle:")) {
                    log.trace("Skipping OSGi bundle: {}", url);
                    continue;
                }
                if (urlPath.indexOf('!') > 0) {
                    urlPath = urlPath.substring(0, urlPath.indexOf('!'));
                }
                log.trace("Scanning for classes in: {} matching criteria: {}", urlPath, test);
                File file = new File(urlPath);
                if (file.isDirectory()) {
                    log.trace("Loading from directory using file: {}", file);
                    loadImplementationsInDirectory(test, packageName, file, classes);
                } else {
                    InputStream stream;
                    if (urlPath.startsWith("http:") || urlPath.startsWith("https:") || urlPath.startsWith("sonicfs:") || isAcceptableScheme(urlPath)) {
                        log.trace("Loading from jar using url: {}", urlPath);
                        URL urlStream = new URL(urlPath);
                        URLConnection con = urlStream.openConnection();
                        con.setUseCaches(false);
                        stream = con.getInputStream();
                    } else {
                        log.trace("Loading from jar using file: {}", file);
                        stream = new FileInputStream(file);
                    }
                    loadImplementationsInJar(test, packageName, stream, urlPath, classes, jarCache);
                }
            } catch (IOException e) {
                log.debug("Cannot read entries in url: " + url, e);
            }
        }
    }
