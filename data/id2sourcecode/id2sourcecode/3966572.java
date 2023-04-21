    private java.awt.Font getFont(Feature feature, Font[] fonts) {
        if (fontFamilies == null) {
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            fontFamilies = new HashSet();
            List f = Arrays.asList(ge.getAvailableFontFamilyNames());
            fontFamilies.addAll(f);
            if (LOGGER.isLoggable(Level.FINEST)) {
                LOGGER.finest("there are " + fontFamilies.size() + " fonts available");
            }
        }
        java.awt.Font javaFont = null;
        int styleCode = 0;
        int size = 6;
        String requestedFont = "";
        for (int k = 0; k < fonts.length; k++) {
            requestedFont = fonts[k].getFontFamily().getValue(feature).toString();
            if (LOGGER.isLoggable(Level.FINEST)) {
                LOGGER.finest("trying to load " + requestedFont);
            }
            if (loadedFonts.containsKey(requestedFont)) {
                javaFont = (java.awt.Font) loadedFonts.get(requestedFont);
                String reqStyle = (String) fonts[k].getFontStyle().getValue(feature);
                if (fontStyleLookup.containsKey(reqStyle)) {
                    styleCode = ((Integer) fontStyleLookup.get(reqStyle)).intValue();
                } else {
                    styleCode = java.awt.Font.PLAIN;
                }
                String reqWeight = (String) fonts[k].getFontWeight().getValue(feature);
                if (reqWeight.equalsIgnoreCase("Bold")) {
                    styleCode = styleCode | java.awt.Font.BOLD;
                }
                size = ((Number) fonts[k].getFontSize().getValue(feature)).intValue();
                return javaFont.deriveFont(styleCode, size);
            }
            if (LOGGER.isLoggable(Level.FINEST)) {
                LOGGER.finest("not already loaded");
            }
            if (fontFamilies.contains(requestedFont)) {
                String reqStyle = (String) fonts[k].getFontStyle().getValue(feature);
                if (fontStyleLookup.containsKey(reqStyle)) {
                    styleCode = ((Integer) fontStyleLookup.get(reqStyle)).intValue();
                } else {
                    styleCode = java.awt.Font.PLAIN;
                }
                String reqWeight = (String) fonts[k].getFontWeight().getValue(feature);
                if (reqWeight.equalsIgnoreCase("Bold")) {
                    styleCode = styleCode | java.awt.Font.BOLD;
                }
                size = ((Number) fonts[k].getFontSize().getValue(feature)).intValue();
                if (LOGGER.isLoggable(Level.FINEST)) {
                    LOGGER.finest("requesting " + requestedFont + " " + styleCode + " " + size);
                }
                javaFont = new java.awt.Font(requestedFont, styleCode, size);
                loadedFonts.put(requestedFont, javaFont);
                return javaFont;
            }
            if (LOGGER.isLoggable(Level.FINEST)) {
                LOGGER.finest("not a system font");
            }
            InputStream is = null;
            if (requestedFont.startsWith("http") || requestedFont.startsWith("file:")) {
                try {
                    URL url = new URL(requestedFont);
                    is = url.openStream();
                } catch (MalformedURLException mue) {
                    if (LOGGER.isLoggable(Level.INFO)) {
                        LOGGER.info("Bad url in SLDStyleFactory " + requestedFont + "\n" + mue);
                    }
                } catch (IOException ioe) {
                    if (LOGGER.isLoggable(Level.INFO)) {
                        LOGGER.info("IO error in SLDStyleFactory " + requestedFont + "\n" + ioe);
                    }
                }
            } else {
                if (LOGGER.isLoggable(Level.FINEST)) {
                    LOGGER.finest("not a URL");
                }
                File file = new File(requestedFont);
                try {
                    is = new FileInputStream(file);
                } catch (FileNotFoundException fne) {
                    if (LOGGER.isLoggable(Level.INFO)) {
                        LOGGER.info("Bad file name in SLDStyleFactory" + requestedFont + "\n" + fne);
                    }
                }
            }
            if (LOGGER.isLoggable(Level.FINEST)) {
                LOGGER.finest("about to load");
            }
            if (is == null) {
                if (LOGGER.isLoggable(Level.INFO)) {
                    LOGGER.info("null input stream");
                }
                continue;
            }
            try {
                javaFont = java.awt.Font.createFont(java.awt.Font.TRUETYPE_FONT, is);
            } catch (FontFormatException ffe) {
                if (LOGGER.isLoggable(Level.INFO)) {
                    LOGGER.info("Font format error in SLDStyleFactory " + requestedFont + "\n" + ffe);
                }
                continue;
            } catch (IOException ioe) {
                if (LOGGER.isLoggable(Level.INFO)) {
                    LOGGER.info("IO error in SLDStyleFactory " + requestedFont + "\n" + ioe);
                }
                continue;
            }
            loadedFonts.put(requestedFont, javaFont);
            return javaFont;
        }
        return new java.awt.Font("Serif", java.awt.Font.PLAIN, 12);
    }
