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
