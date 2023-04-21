    void handleFileRequest() throws IOException {
        String path = request.getPathInfo();
        File file = new File(dispatcher.fileBase, path);
        Log.debug("transferFile full path: " + file);
        if (!file.exists() || file.isDirectory()) throw new RuntimeException("requested file does not exist or is a directory");
        OutputStream out = response.getOutputStream();
        int dot = path.lastIndexOf('.');
        String ext = dot == -1 ? "" : path.substring(dot).toLowerCase();
        String type;
        if (ext.startsWith(".htm")) type = "text/html"; else if (ext.equals(".xml") || ext.equals(".txt")) type = "text/" + ext; else if (ext.equals(".gif") || ext.equals(".jpg") || ext.equals(".png")) type = "image/" + ext; else if (ext.equals(".jar")) type = "application/java-archive"; else type = "binary/" + ext;
        InputStream in = new FileInputStream(file);
        byte[] buffer = new byte[4096];
        while (true) {
            int read = in.read(buffer);
            if (read == -1) break;
            out.write(buffer, 0, read);
        }
        out.flush();
    }
