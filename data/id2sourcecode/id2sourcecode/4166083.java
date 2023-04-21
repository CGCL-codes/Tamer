    public InputStream getResourceAsStream(String fileName) {
        ClassLoader parentClassLoader = getParent();
        try {
            int pos = -1;
            if ((basePath == null) && ((paths != null) || (urls != null))) {
                if (paths != null) {
                    for (int i = 0; i < paths.length; i++) {
                        String prefix = ((fileName.indexOf("\\") == 0) || (fileName.indexOf("/") == 0)) ? "" : paths[i] + File.separatorChar;
                        File f = new File(prefix + fileName);
                        if (f.exists()) {
                            FileInputStream is = new FileInputStream(f);
                            if (is != null) return is;
                        } else if ((pos = fileName.indexOf("!/")) > 0) {
                            String fn = fileName.substring(pos + 2);
                            InputStream is = parentClassLoader.getResourceAsStream(fn);
                            if (is != null) return is;
                        }
                    }
                } else {
                    for (int i = 0; i < urls.length; i++) {
                        try {
                            URL url = new URL(urls[i], fileName);
                            InputStream is = url.openStream();
                            if (is != null) return is;
                        } catch (MalformedURLException ex) {
                            ex.printStackTrace();
                        } catch (IOException ex) {
                        }
                    }
                }
            } else {
                pos = fileName.indexOf("!/");
                if (pos > 0) fileName = fileName.substring(pos + 2);
                if (paths != null) {
                    String strippedFileName = fileName;
                    pos = strippedFileName.lastIndexOf("\\");
                    if (pos > 0) strippedFileName = strippedFileName.substring(pos + 1);
                    pos = strippedFileName.lastIndexOf("/");
                    if (pos > 0) strippedFileName = strippedFileName.substring(pos + 1);
                    for (int i = 0; i < paths.length; i++) {
                        String fileStr = basePath + ((fileName.charAt(0) == '/') ? "" : "/");
                        String subPath = paths[i];
                        if (((pos = subPath.indexOf(basePath)) >= 0) && (subPath.length() >= basePath.length())) subPath = subPath.substring(pos + basePath.length());
                        if (subPath.length() > 0) {
                            char c0 = subPath.charAt(0);
                            if ((c0 == '\\') || (c0 == '/')) subPath = subPath.substring(1);
                            fileStr += subPath + "/";
                        }
                        fileStr += strippedFileName;
                        InputStream is = parentClassLoader.getResourceAsStream(fileStr);
                        if (is != null) return is;
                    }
                }
            }
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        }
        if (parentClassLoader != null) {
            if (currentProject.canAccessDefaultPackage(parentClassLoader, fileName)) {
                return parentClassLoader.getResourceAsStream(fileName);
            }
        }
        return null;
    }
