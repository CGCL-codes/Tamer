    @Override
    protected boolean handleFileUploads(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (req.getPathInfo().startsWith("/fileupload")) {
            try {
                beginRequest(req);
                Map<String, String[]> parameters = req.getParameterMap();
                String containerName = parameters.get("container")[0];
                int controlId = Integer.parseInt(parameters.get("control")[0]);
                FileItemFactory factory = new DiskFileItemFactory(fileUploadThreshold, new File(fileUploadRepository));
                ServletFileUpload upload = new ServletFileUpload(factory);
                List<FileItem> items = upload.parseRequest(req);
                FileItem item = items.get(0);
                UploadedFile uploadedFile = new UploadedFile();
                uploadedFile.name = item.getName();
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                final int BUFFER_SIZE = 10240;
                InputStream is = item.getInputStream();
                int read = 0;
                byte[] buffer = new byte[BUFFER_SIZE];
                while (0 < (read = is.read(buffer, 0, BUFFER_SIZE))) {
                    bos.write(buffer, 0, read);
                }
                uploadedFile.content = bos.toByteArray();
                List<JSONObject> eventsToSend = dispatchEvent(getApplication(req), containerName, controlId, "uploaded", uploadedFile);
                resp.setCharacterEncoding("UTF-8");
                PrintWriter pw = resp.getWriter();
                String response = eventsToSend.toString();
                System.out.println("responding: " + response);
                pw.write("<html>");
                pw.write(response);
            } catch (FileUploadException e) {
                throw new RuntimeException(e);
            } finally {
                endRequest(req);
            }
            return true;
        }
        return false;
    }
