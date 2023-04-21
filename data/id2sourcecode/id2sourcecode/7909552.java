    public void sendFile(File file) throws IOException {
        if (!file.exists()) {
            response.sendError(404, "File not found.");
        } else {
            if (file.isFile()) {
                try {
                    String fileExtention = file.getName().substring(file.getName().lastIndexOf(".") + 1, file.getName().length());
                    if ("png".equalsIgnoreCase(fileExtention)) response.setContentType("image/png"); else if ("gif".equalsIgnoreCase(fileExtention)) response.setContentType("image/gif"); else if ("jpg".equalsIgnoreCase(fileExtention) || "jpeg".equalsIgnoreCase(fileExtention)) response.setContentType("image/jpeg"); else if ("txt".equalsIgnoreCase(fileExtention)) response.setContentType("text/plain"); else if ("css".equalsIgnoreCase(fileExtention)) response.setContentType("text/css"); else if ("html".equalsIgnoreCase(fileExtention) || "htm".equalsIgnoreCase(fileExtention)) response.setContentType("text/html");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                FileInputStream fis = new FileInputStream(file);
                int i;
                while ((i = fis.read()) != -1) response.getWriter().write(i);
                fis.close();
            } else if (file.isDirectory()) {
                new FileBrowser(file).doGet(request, response);
            }
        }
    }
