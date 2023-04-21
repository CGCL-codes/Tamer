    private void loadTileImage(final Tile tile) {
        String loadingError = null;
        Tile parentTile = null;
        boolean isNotifyObserver = true;
        boolean isParentFinal = false;
        try {
            fTileFactoryImpl.fireTileEvent(TileEventId.TILE_START_LOADING, tile);
            boolean isSaveImage = false;
            final TileImageCache tileImageCache = fTileFactoryImpl.getTileImageCache();
            final TileFactory tileFactory = tile.getTileFactory();
            final TileFactoryInfo factoryInfo = tileFactory.getInfo();
            ImageData[] tileImageData = tileImageCache.getOfflineTileImageData(tile);
            if (tileImageData == null) {
                isSaveImage = true;
                InputStream inputStream = null;
                try {
                    final ITileLoader tileLoader = factoryInfo.getTileLoader();
                    if (tileLoader instanceof ITileLoader) {
                        try {
                            inputStream = tileLoader.getTileImageStream(tile);
                        } catch (final Exception e) {
                            loadingError = e.getMessage();
                            StatusUtil.logStatus(loadingError, e);
                            throw e;
                        }
                    } else {
                        final URL url;
                        try {
                            url = fTileFactoryImpl.getURL(tile);
                        } catch (final Exception e) {
                            loadingError = e.getMessage();
                            throw e;
                        }
                        try {
                            inputStream = url.openStream();
                        } catch (final UnknownHostException e) {
                            loadingError = "Map image cannot be loaded from:\n" + tile.getUrl() + "\n\nUnknownHostException: " + e.getMessage();
                            StatusUtil.logStatus(loadingError, e);
                            throw e;
                        } catch (final FileNotFoundException e) {
                            loadingError = "Map image cannot be loaded from:\n" + tile.getUrl() + "\n\nFileNotFoundException: " + e.getMessage();
                            throw e;
                        } catch (final Exception e) {
                            loadingError = "Map image cannot be loaded from:\n" + tile.getUrl() + "\n" + e.getMessage();
                            StatusUtil.logStatus(loadingError, e);
                            throw e;
                        }
                    }
                    tileImageData = new ImageLoader().load(inputStream);
                } catch (final Exception e) {
                    try {
                        if (inputStream != null) {
                            inputStream.close();
                        }
                    } catch (final IOException e1) {
                        StatusUtil.logStatus(e.getMessage(), e);
                    }
                    fTileFactoryImpl.fireTileEvent(TileEventId.TILE_ERROR_LOADING, tile);
                }
            }
            boolean isCreateImage = true;
            Tile imageTile = tile;
            String imageTileKey = tile.getTileKey();
            if (tileImageData == null) {
                tile.setLoadingError(loadingError == null ? "Loaded image data is empty" : loadingError);
                isCreateImage = false;
            }
            if (tile.isChildTile()) {
                isNotifyObserver = false;
                if (tileImageData != null && isSaveImage) {
                    tileImageCache.saveOfflineImage(tile, tileImageData);
                }
                parentTile = tile.getParentTile();
                final ParentImageStatus parentImageStatus = tile.setChildImageData(tileImageData);
                if (parentImageStatus == null) {
                    parentTile.setLoadingError("Parent image cannot be created");
                } else {
                    if (parentImageStatus.isImageFinal) {
                        parentTile.setLoadingError(parentImageStatus.childLoadingError);
                        tileImageData = parentImageStatus.tileImageData;
                        imageTile = parentTile;
                        imageTileKey = parentTile.getTileKey();
                        isParentFinal = true;
                        isCreateImage = true;
                        isSaveImage = parentImageStatus.isSaveImage;
                    } else {
                        isCreateImage = false;
                    }
                }
            }
            if (isCreateImage) {
                final Image tileImage = tileImageCache.createImage(tileImageData, imageTile, imageTileKey, isSaveImage);
                if (imageTile.setMapImage(tileImage) == false) {
                    tile.setLoadingError("Image is invalid");
                }
            }
        } catch (final Exception e) {
            StatusUtil.logStatus("Exception occured when loading tile images", e);
        } finally {
            finalizeTile(tile, isNotifyObserver);
            if (isParentFinal) {
                finalizeTile(parentTile, true);
            }
        }
    }
