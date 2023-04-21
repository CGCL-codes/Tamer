    protected LookupResult lookupNoCache(HttpServletRequest req) {
        final String path = getPath(req);
        if (isForbidden(path)) return new Error(HttpServletResponse.SC_FORBIDDEN, "Forbidden");
        final URL url;
        try {
            url = getServletContext().getResource(path);
        } catch (MalformedURLException e) {
            return new Error(HttpServletResponse.SC_BAD_REQUEST, "Malformed path");
        }
        if (url == null) return new Error(HttpServletResponse.SC_NOT_FOUND, "Not found");
        final String mimeType = getMimeType(path);
        final String realpath = getServletContext().getRealPath(path);
        if (realpath != null) {
            File f = new File(realpath);
            if (!f.isFile()) return new Error(HttpServletResponse.SC_FORBIDDEN, "Forbidden"); else return new StaticFile(f.lastModified(), mimeType, (int) f.length(), acceptsDeflate(req), url);
        } else {
            try {
                final ZipEntry ze = ((JarURLConnection) url.openConnection()).getJarEntry();
                if (ze != null) {
                    if (ze.isDirectory()) return new Error(HttpServletResponse.SC_FORBIDDEN, "Forbidden"); else return new StaticFile(ze.getTime(), mimeType, (int) ze.getSize(), acceptsDeflate(req), url);
                } else return new StaticFile(-1, mimeType, -1, acceptsDeflate(req), url);
            } catch (ClassCastException e) {
                return new StaticFile(-1, mimeType, -1, acceptsDeflate(req), url);
            } catch (IOException e) {
                return new Error(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Internal server error");
            }
        }
    }
