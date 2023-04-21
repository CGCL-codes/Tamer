    public static void downloadPlugin(Artifact artifact, File pluginDir) throws IOException {
        if (!pluginDir.isDirectory()) throw new IOException(pluginDir.getAbsolutePath() + ": is not a valid directory");
        URL url = new URL(NexusURLs.NEXUS_SERVICE + "/local/artifact/maven/redirect?" + artifact.getNexusOptions().replaceAll("&", "&amp;").replaceAll("workflow-component-description", "jar-with-dependencies").replaceAll("p=zip", "p=jar"));
        URLConnection uc = url.openConnection();
        String contentType = uc.getContentType();
        int contentLength = uc.getContentLength();
        if (contentType.startsWith("text/") || contentLength == -1) {
            throw new IOException("Expected binary file, found text!");
        }
        InputStream rawinStream = url.openStream();
        InputStream in = new BufferedInputStream(rawinStream);
        byte[] data = new byte[contentLength];
        int bytesRead = 0;
        int offset = 0;
        while (offset < contentLength) {
            bytesRead = in.read(data, offset, data.length - offset);
            if (bytesRead == -1) break;
            offset += bytesRead;
        }
        in.close();
        if (offset != contentLength) {
            throw new IOException("Only read " + offset + " bytes; Expected " + contentLength + " bytes");
        }
        String filename = url.getFile().substring(url.getFile().lastIndexOf('/') + 1);
        FileOutputStream out = new FileOutputStream(pluginDir.getAbsoluteFile().getAbsolutePath() + File.separator + filename);
        out.write(data);
        out.flush();
        out.close();
    }
