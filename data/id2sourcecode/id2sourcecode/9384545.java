    private void handleFile(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        try {
            mLog.debug("processing upload request...");
            PrintWriter out = response.getWriter();
            out.println("<html>");
            out.println("<head>");
            out.println("<title>" + "File Received" + "</title>");
            out.println("</head>");
            out.println("<body bgcolor=\"white\">");
            ResourceBundle rb = ResourceBundle.getBundle("DynasoarUploader", request.getLocale());
            String filepath = System.getenv("CATALINA_HOME") + "/" + rb.getString("filesave.location");
            Enumeration e = request.getAttributeNames();
            boolean multipart = FileUpload.isMultipartContent(request);
            FileItem item = null;
            String storedFileName = null;
            FileUpload upload = new FileUpload(new DefaultFileItemFactory());
            List items = upload.parseRequest(request);
            mLog.debug("items.size() = " + items.size());
            Iterator it = items.iterator();
            String writeFilePath = null;
            String parseFilePath = null;
            String serviceURI = rb.getString("URI.endpoint");
            while (it.hasNext()) {
                item = (FileItem) it.next();
                if (!(item.isFormField())) {
                    try {
                        mLog.debug("item name = " + item.getName());
                        String fieldName = item.getFieldName();
                        if (item.getName().equals("")) {
                            continue;
                        }
                        if (fieldName.equals("descfile")) {
                            mLog.debug("this is the metadata file");
                            byte[] fileData = item.get();
                            metadata = new String(fileData);
                            mLog.debug("Metadata: " + metadata + "\n");
                        } else {
                            InputStream in = item.getInputStream();
                            parseFilePath = parseFileName(item.getName(), "/");
                            mLog.debug("Parsed file name after first pass =" + parseFilePath);
                            parseFilePath = parseFileName(item.getName(), "\\");
                            mLog.debug("Parsed file name after second pass =" + parseFilePath);
                            writeFilePath = filepath + File.separator + parseFilePath;
                            mLog.debug("Writing to =" + writeFilePath);
                            fileList.add(serviceURI + parseFilePath);
                            File tempFile = new File(writeFilePath);
                            if (!tempFile.exists()) {
                                tempFile.createNewFile();
                            }
                            FileOutputStream outStream = new FileOutputStream(tempFile);
                            ReadableByteChannel channelIn = Channels.newChannel(in);
                            FileChannel channelOut = outStream.getChannel();
                            channelOut.transferFrom(channelIn, 0, item.getSize());
                            channelIn.close();
                            channelOut.close();
                            outStream.flush();
                            outStream.close();
                        }
                    } catch (IOException ix) {
                        errorHandler();
                    }
                } else {
                    String name = item.getFieldName();
                    String value = item.getString();
                    if (name.equals("vmname") || name.equals("servicename")) {
                        mLog.debug("Service/VM Name: " + value);
                        serviceName = value;
                    } else if (name.equals("morefiles")) {
                        mLog.debug("Adding more files: " + value);
                        if (value.equals("Yes")) {
                            mLog.debug("More files will be uploaded corresponding to this VM");
                            moreFiles = true;
                        } else if (value.equals("No")) {
                            mLog.debug("No more files will be uploaded corresponding to this VM");
                            moreFiles = false;
                        }
                    }
                }
            }
            if (moreFiles) {
                init = true;
                WAR = false;
                VMWARE = true;
                doGet(request, response);
            } else {
                registerCode(request, response);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
