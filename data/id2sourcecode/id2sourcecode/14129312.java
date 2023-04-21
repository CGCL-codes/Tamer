    @SuppressWarnings("unchecked")
    public void returnStatic(ServletContext servletContext, String uri) throws UserException, ProgrammerException {
        InputStream is;
        try {
            res.setDateHeader("Expires", System.currentTimeMillis() + 24 * 60 * 60 * 1000);
            OutputStream os = res.getOutputStream();
            if (uri == null) {
                throw UserException.newNotFound();
            }
            if (uri.endsWith("/")) {
                uri = uri.substring(0, uri.length() - 1);
            }
            int lastSlash = uri.lastIndexOf("/");
            String resourcePath = uri.substring(0, lastSlash + 1);
            URL url;
            Set<String> paths = servletContext.getResourcePaths(resourcePath);
            if (paths == null) {
                throw UserException.newNotFound();
            }
            String potentialPath = null;
            for (String candidatePath : paths) {
                if (!candidatePath.endsWith("/") && candidatePath.startsWith(uri + ".")) {
                    potentialPath = candidatePath;
                    break;
                }
            }
            if (potentialPath == null) {
                throw UserException.newNotFound();
            }
            url = servletContext.getResource(potentialPath);
            if (url == null) {
                throw UserException.newNotFound();
            }
            URLConnection con = url.openConnection();
            String contentType = servletContext.getMimeType(url.toString());
            int c;
            con.connect();
            is = con.getInputStream();
            if (contentType != null) {
                res.setContentType(contentType);
            }
            while ((c = is.read()) != -1) {
                os.write(c);
            }
            os.close();
            is.close();
        } catch (MalformedURLException e) {
            throw UserException.newBadRequest();
        } catch (FileNotFoundException e) {
            throw UserException.newNotFound();
        } catch (IOException e) {
        }
    }
