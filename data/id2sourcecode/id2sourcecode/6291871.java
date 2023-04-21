    private static File getFile(URL url, String mappedLibName) {
        String path = url.toString();
        if (path.startsWith("file:")) {
            return new File(path.substring("file:".length(), path.length()));
        } else {
            File dir = getDeploymentDir();
            File f = new File(dir, mappedLibName);
            if (f.isFile()) return f;
            InputStream in = null;
            BufferedXOutputStream out = null;
            try {
                Logger.global.config("transferring library from url to file:\n  url =" + url + "\n  file = " + f + "\n");
                in = url.openStream();
                out = new BufferedXOutputStream(Files.createLockedOutputStream(f, 3, TimeUnit.SECONDS));
                out.transferFrom(in, -1);
                f.deleteOnExit();
                dir.deleteOnExit();
                return f;
            } catch (Throwable ex) {
                Logger.global.log(Level.WARNING, "error transferring library from url to file:\n  url =" + url + "\n  file = " + f + "\n", ex);
                if (f != null) f.delete();
                throw (UnsatisfiedLinkError) new UnsatisfiedLinkError("error installing library to temp dir").initCause(ex);
            } finally {
                IO.tryClose(in);
                IO.tryClose(out);
            }
        }
    }
