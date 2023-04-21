    private void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String name = request.getParameter("pic");
        if (name != null && validate(name) == true) {
            if (name.indexOf('.') == -1) name += ".jpg";
            if (name.substring(name.indexOf('.')).equals("gif")) response.setContentType("image/gif"); else if (name.substring(name.indexOf('.')).equals("png")) response.setContentType("image/png"); else if (name.substring(name.indexOf('.')).equals("tiff")) response.setContentType("image/tiff"); else response.setContentType("image/jpeg");
            OutputStream os = response.getOutputStream();
            try {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                File f = new File(ConfigKeeper.getInstallDir() + ConfigKeeper.getStoragePath() + name);
                FileInputStream fis = new FileInputStream(f);
                byte buff[] = new byte[100000];
                int n;
                while ((n = fis.read(buff, 0, 100000)) > 0) baos.write(buff, 0, n);
                fis.close();
                baos.writeTo(os);
                baos.close();
            } catch (FileNotFoundException ex) {
                log.warning(ex.toString());
            } catch (Exception e) {
                log.log(Level.SEVERE, e.toString(), e);
            }
            os.close();
        }
    }
