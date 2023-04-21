        }
    }

    /**  Description of the Method */
    void doChangeUrl() {
        if (impl != null) {
            Texture new_texture = loadTexture();
            impl = new_texture;
            if (parentAppearances != null) {
                int numApp = parentAppearances.size();
                for (int i = 0; i < numApp; i++) {
                    Appearance app = parentAppearances.get(i);
                    if (app != null) {
                        app.impl.setTexture(impl);
                    }
                }
            }
        }
    }

    public Texture loadTexture() {
        Object key = Thread.currentThread();
        VrmlLoaderSettings settings = VrmlLoader.loaderSettings.get(key);
        if (settings == null) {
            settings = new VrmlLoaderSettings();
            VrmlLoader.loaderSettings.put(key, settings);
        }
        if (settings.loadedTextures == null) {
            settings.loadedTextures = new HashMap<String, Texture2D>();
        }
        Texture2D texture = null;
        if (url.strings != null && url.strings.length > 0) {
            for (int i = 0; i < url.strings.length; i++) {
                String urlString = url.strings[i];
                boolean hasAlpha = false;
                texture = settings.loadedTextures.get(url.strings[i]);
                if (texture == null) {
                    try {
                        TextureLoader loader = null;
                        try {
                            if (settings.loadTextures) {
                                urlObj = null;
                                try {
                                    urlObj = this.loader.stringToURL(urlString);
                                } catch (MalformedURLException url_e) {
                                    System.err.println("wrong URL format: " + urlString);
                                }
                                if (settings.verbose) System.out.println("loading texture " + urlObj + " .. ");
                                String suffix = url.strings[i].substring(url.strings[i].lastIndexOf('.') + 1).toLowerCase();
                                String ldr_format = "RGBA";
                                if (suffix.equalsIgnoreCase("jpg") || suffix.equalsIgnoreCase("jpeg") || suffix.equalsIgnoreCase("jp2") || suffix.equalsIgnoreCase("j2c")) {
                                    ldr_format = "RGB";
                                }
                                if (settings.generateMipMaps) {
                                    if (false) {
                                        loader = new TextureLoader(urlObj, ldr_format, TextureLoader.BY_REFERENCE | TextureLoader.GENERATE_MIPMAP, this.observer);
                                        if (loader != null) {
                                            texture = (Texture2D) loader.getTexture();
                                        }
                                    } else {
                                        loader = new TextureLoader(urlObj, ldr_format, TextureLoader.BY_REFERENCE, this.observer);
                                        if (loader != null) {
                                            texture = (Texture2D) loader.getTexture();
                                            texture = createMipMapTexture(texture);
                                        }
                                    }
                                    texture.setMagFilter(Texture.BASE_LEVEL_LINEAR);
                                    texture.setMinFilter(Texture.MULTI_LEVEL_LINEAR);
                                } else {
                                    encoding = settings.encoding;
                                    BufferedImage bufferedImage = (BufferedImage) java.security.AccessController.doPrivileged(new java.security.PrivilegedAction() {

                                        public Object run() {
                                            try {
                                                URLConnection connection = urlObj.openConnection();
                                                if (encoding != null) {
                                                    connection.setRequestProperty("Authorization", "Basic " + encoding);
                                                }
                                                InputStream inputStream = connection.getInputStream();
                                                BufferedImage image = ImageIO.read(inputStream);
                                                inputStream.close();
                                                return image;
                                            } catch (IOException e) {
                                                throw new ImageException(e);
                                            }
                                        }
                                    });
                                    loader = new TextureLoader(bufferedImage, ldr_format, TextureLoader.BY_REFERENCE, this.observer);
                                    if (loader != null) {
                                        texture = (Texture2D) loader.getTexture();
                                    }
                                }
                            } else {
                            }
                        } catch (javax.media.ding3d.utils.image.ImageException img_e) {
                            img_e.printStackTrace();
                        }
                        if (texture != null) {
                            texture.setUserData(urlString);
                            texture.setCapability(Texture2D.ALLOW_ENABLE_WRITE);
                            ImageComponent[] images = texture.getImages();
                            for (ImageComponent image : images) {
                                image.setCapability(ImageComponent.ALLOW_IMAGE_WRITE);
                            }
                            if (repeatS.value == true) {
                                texture.setBoundaryModeS(Texture.WRAP);
                            } else {
                                texture.setBoundaryModeS(Texture.CLAMP_TO_EDGE);
                            }
                            if (repeatT.value == true) {
                                texture.setBoundaryModeT(Texture.WRAP);
                            } else {
                                texture.setBoundaryModeT(Texture.CLAMP_TO_EDGE);
                            }
                            texture.setMagFilter(Texture2D.NICEST);
                            if (settings.loadTextures) {
                                ImageComponent2D imageComponent = (ImageComponent2D) texture.getImage(0);
                                ColorModel cm = imageComponent.getImage().getColorModel();
                                hasAlpha = cm.hasAlpha();
                                this.setTransparency(hasAlpha);
                                if (settings.anisotropicFiltering) {
                                    texture.setAnisotropicFilterMode(Texture2D.ANISOTROPIC_SINGLE_VALUE);
                                } else {
                                    texture.setAnisotropicFilterMode(Texture2D.ANISOTROPIC_NONE);
                                }
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
