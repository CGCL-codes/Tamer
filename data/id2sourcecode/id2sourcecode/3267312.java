    byte[] elmFindResource(String name) {
        try {
            URL url = null;
            for (int i = 0; i < exportElmClassPath.length; i++) {
                if (exportElmClassPath[i].endsWith(".jar")) {
                    url = findResourceFromJarFile(new File(exportElmClassPath[i]), name);
                    if (url != null) break; else continue;
                } else {
                    url = findResourceFromFile(new File(exportElmClassPath[i]), name);
                    if (url != null) break; else continue;
                }
            }
            if (url == null) return null;
            URLConnection c = url.openConnection();
            int i = c.getContentLength();
            byte b[] = new byte[i];
            InputStream is = c.getInputStream();
            is.read(b);
            return b;
        } catch (Exception e) {
            return null;
        }
    }
