    @Override
    protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
        FileUpload fileUpload = (FileUpload) command;
        MultipartFile multipartFile = fileUpload.getFile();
        String fileName = "";
        System.err.println("-------------------------------------------");
        try {
            InputStream inputStream = null;
            OutputStream outputStream = null;
            if (multipartFile.getSize() > 0) {
                inputStream = multipartFile.getInputStream();
                fileName = request.getRealPath("") + "/images/" + multipartFile.getOriginalFilename();
                outputStream = new FileOutputStream(fileName);
                int readBytes = 0;
                byte[] buffer = new byte[100000];
                while ((readBytes = inputStream.read(buffer, 0, 100000)) != -1) {
                    outputStream.write(buffer, 0, readBytes);
                }
                outputStream.close();
                inputStream.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        Map model = new HashMap();
        model.put("fileName", multipartFile.getOriginalFilename());
        model.put("filepath", "images/" + multipartFile.getOriginalFilename());
        return new ModelAndView(getSuccessView(), model);
    }
