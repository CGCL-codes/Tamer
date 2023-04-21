    protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) {
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        FileUploadBean bean = (FileUploadBean) command;
        MultipartFile file = multipartRequest.getFile("file");
        if (file == null) {
            logger.debug("Esta jodido");
            return new ModelAndView(new RedirectView("fallo.html"));
        } else try {
            InputStream inputStream = null;
            OutputStream outputStream = null;
            if (file.getSize() > 0) {
                inputStream = file.getInputStream();
                File realUpload = new File("/opt/tomcat-6/tmp/");
                outputStream = new FileOutputStream("/opt/tomcat-6/wtpwebapps/hafnerwebsite/images/" + file.getOriginalFilename());
                logger.debug("====22=========");
                logger.debug(file.getOriginalFilename());
                logger.debug("=============");
                int readBytes = 0;
                byte[] buffer = new byte[8192];
                while ((readBytes = inputStream.read(buffer, 0, 8192)) != -1) {
                    logger.debug("===ddd=======");
                    outputStream.write(buffer, 0, readBytes);
                }
                GetProductAction busca = new GetProductAction();
                Product obtenido = busca.getSelectedProductbyIDProduct(bean.getId());
                obtenido.setProduct_image(file.getOriginalFilename());
                UpdateProductoAction actualiza = new UpdateProductoAction();
                if (actualiza.setSelectedProduct(obtenido.getID())) {
                    actualiza.setUpdateProduct(obtenido);
                    actualiza.updateProduct();
                    outputStream.close();
                    inputStream.close();
                    return new ModelAndView("products4Users");
                } else return new ModelAndView(new RedirectView("fallo.html"));
            } else logger.debug("Esta jodido");
            return new ModelAndView(new RedirectView("fallo.html"));
        } catch (IOException e) {
            logger.debug("Esta jodido");
            return new ModelAndView(new RedirectView("fallo.html"));
        }
    }
