    private void registerFont(final String path) {
        try {
            if (path.toLowerCase().endsWith(".ttf") || path.toLowerCase().endsWith(".otf") || path.toLowerCase().indexOf(".ttc,") > 0) {
                Object allNames[] = BaseFont.getAllFontNames(path, BaseFont.WINANSI, null);
                String[][] names = (String[][]) allNames[2];
                for (String[] name : names) {
                    registeredFontMap.put(name[3].toLowerCase(), path);
                }
            } else if (path.toLowerCase().endsWith(".ttc")) {
                String[] names = BaseFont.enumerateTTCNames(path);
                for (int i = 0; i < names.length; i++) {
                    registerFont(path + "," + i);
                }
            } else if (path.toLowerCase().endsWith(".afm") || path.toLowerCase().endsWith(".pfm")) {
                BaseFont bf = BaseFont.createFont(path, BaseFont.CP1252, false);
                String fullName = bf.getFullFontName()[0][3].toLowerCase();
                registeredFontMap.put(fullName, path);
            }
        } catch (DocumentException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
