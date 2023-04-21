    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        PrintWriter out = response.getWriter();
        out.println("<html>");
        out.println("<head>");
        codeType = request.getParameter("codeType");
        out.println("</p>code type is: " + codeType);
        if (false) {
            String title = "Uploader";
            out.println("<title>" + title + "</title>");
            out.println("</head>");
            out.println("<body bgcolor=\"white\">");
            out.println("<h1> (POST) Upload your web service here </h1>");
            out.println("<p>");
            out.println("Please select the file you wish to upload and then click the Upload button");
            out.println("<form enctype=\"multipart/form-data\" action=\"UploaderChooser\" method=POST>");
            out.println("<input name=filename type=file size = 100");
            out.println("<br/>");
            out.println("<input type=submit value=Upload>");
            out.println("</form>");
            out.println("</body>");
            out.println("</html>");
            out.println("</form>");
        } else {
            boolean multipart = FileUpload.isMultipartContent(request);
            if (multipart) {
                try {
                    out.println("<title>" + "File Received" + "</title>");
                    out.println("</head>");
                    out.println("<body bgcolor=\"white\">");
                    ResourceBundle rb = ResourceBundle.getBundle("DynasoarUploader", request.getLocale());
                    String filepath = rb.getString("filesave.location");
                    Enumeration e = request.getAttributeNames();
                    FileItem item = null;
                    String storedFileName = null;
                    FileUpload upload = new FileUpload(new DefaultFileItemFactory());
                    List items = upload.parseRequest(request);
                    System.out.println("items.size() = " + items.size());
                    Iterator it = items.iterator();
                    String writeFilePath = null;
                    String parseFilePath = null;
                    while (it.hasNext()) {
                        item = (FileItem) it.next();
                        if (!(item.isFormField())) {
                            System.out.println("item name = " + item.getName());
                            InputStream in = item.getInputStream();
                            parseFilePath = parseFileName(item.getName(), "/");
                            System.out.println("Parsed file name after first pass =" + parseFilePath);
                            parseFilePath = parseFileName(item.getName(), "\\");
                            System.out.println("Parsed file name after second pass =" + parseFilePath);
                            writeFilePath = filepath + File.separator + parseFilePath;
                            System.out.println("Writing to =" + writeFilePath);
                            File tempFile = new File(writeFilePath);
                            System.out.println("filepath = " + filepath);
                            System.out.println("parseFilePath = " + parseFilePath);
                            System.out.println("parseFileName " + getFileName(parseFilePath));
                            String configFileName = filepath + File.separator + getFileName(parseFilePath) + "_conf.ser";
                            System.out.println("configFileName = " + configFileName);
                            writeProperties(assemblyWithSingleSPConf, configFileName);
                            addConfig(getFileName(parseFilePath) + "_conf.ser", rb);
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
                    }
                    AddServiceMsgType messageToSend = new AddServiceMsgType();
                    messageToSend.setServiceID(parseFilePath.substring(0, parseFilePath.lastIndexOf(".")));
                    URL codestoreEndpoint = new URL(rb.getString("codestore.URL"));
                    String uriEndpoint = rb.getString("URI.endpoint");
                    org.apache.axis.types.URI finalEndpoint = new org.apache.axis.types.URI(uriEndpoint + parseFilePath);
                    messageToSend.setServiceURL(finalEndpoint);
                    CodeStoreService css = new CodeStoreServiceLocator();
                    CodeStorePortType cspt = css.getCodeStoreService(codestoreEndpoint);
                    String result = cspt.addData(messageToSend);
                    System.out.println("Result of call " + result);
                    out.println("Your web service has been received. If you would like to submit another web service, please click the button below.");
                    out.println("<p>");
                    out.println("<form enctype=\"multipart/form-data\" action=\"CodeStoreUploader\" method=GET>");
                    out.println("<input type=submit value=\"Submit more files\">");
                    out.println("</form>");
                    out.println("</body>");
                    out.println("</html>");
                } catch (FileUploadException e1) {
                    e1.printStackTrace();
                } catch (ServiceException e1) {
                    e1.printStackTrace();
                }
            } else {
                if (codeType.equals(WAR)) {
                }
            }
        }
        out.println("</body>");
        out.println("</html>");
    }
