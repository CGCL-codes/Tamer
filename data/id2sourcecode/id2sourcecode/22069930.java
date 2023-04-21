    public Object load(RRL location, Object loaderParams, CacheBehavior cache) throws ColladaLoadingException {
        Object data = null;
        if (cache.isCached(location)) {
            CachedItem cachedItem = cache.loadCachedItem(location);
            if (cachedItem != null) data = cachedItem.getLoadedData();
        }
        if (data == null) {
            Collada loader = new Collada();
            if (loaderParams instanceof LoaderParams) loader.applyConfiguration((LoaderParams) loaderParams); else loader.setLoadFlags(true, true, true, false, true);
            boolean zip = false;
            URL url = Repository.get().getResource(location);
            String path = url.toString();
            try {
                url = new URL(path + ".gzip");
                zip = true;
            } catch (MalformedURLException ex) {
                logger.log(Level.SEVERE, "MalformedURLException when trying to make: {0}\n{1}", new Object[] { path, ex });
            }
            boolean tryLoading = true;
            while (tryLoading) {
                try {
                    URLConnection conn = url.openConnection();
                    InputStream in = conn.getInputStream();
                    synchronized (ColladaRepoComponent.getJAXBContext()) {
                        unmarshaller = ColladaRepoComponent.getJAXBContext().createUnmarshaller();
                    }
                    COLLADA collada = null;
                    if (zip) collada = (org.collada.colladaschema.COLLADA) unmarshaller.unmarshal(new GZIPInputStream(in)); else collada = (org.collada.colladaschema.COLLADA) unmarshaller.unmarshal(in);
                    PScene loadingScene = new PScene();
                    loader.load(loadingScene, location, collada);
                    cache.writeToCache(location, loadingScene);
                    data = loadingScene;
                    System.out.println("Loaded from COLLADA: " + location.getRelativePath());
                    tryLoading = false;
                } catch (Exception ex) {
                    if (zip) {
                        zip = false;
                        url = Repository.get().getResource(location);
                        System.out.println("Didn't find gzip for " + url);
                    } else {
                        tryLoading = false;
                        logger.log(Level.SEVERE, "Problem trying to load file: {0}\n{1}", new Object[] { url, ex });
                        if (oneErrorBox) {
                            JOptionPane.showMessageDialog(Cosmic.getOnscreenRenderBuffer().getCanvas(), "Was not able to load collada file", "Error during collada load (error box will only appear once)", JOptionPane.ERROR_MESSAGE, new ImageIcon(Repository.get().getResource(new RRL("assets/textures/imilogo.jpg"))));
                            oneErrorBox = false;
                        }
                    }
                }
            }
        }
        return data;
    }
