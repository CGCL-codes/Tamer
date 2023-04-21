    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("image/jpeg");
        String filePath = SystemFilesLoader.getInstance().getNewgenlibProperties().getProperty("BOOK_COVERS_LOCATION");
        String isOk = SystemFilesLoader.getInstance().getNewgenlibProperties().getProperty("BOOK_COVERS", "ON");
        String isbn = request.getParameter("ISBN");
        final String id = request.getParameter("Id");
        final String libid = request.getParameter("LibId");
        final Utility utilityx = Utility.getInstance(null);
        if (id != null && libid != null && !id.equals("") && !libid.equals("")) {
            File bookcoversdir = new File(filePath);
            if (bookcoversdir.exists()) {
                File[] files = bookcoversdir.listFiles(new FileFilter() {

                    @Override
                    public boolean accept(File pathname) {
                        String fileName = pathname.getName();
                        String onlyfileName = "";
                        try {
                            onlyfileName = utilityx.getFileName(fileName);
                        } catch (Exception ex) {
                            System.out.println("in error is......" + fileName);
                        }
                        if (onlyfileName.equals(id + "_" + libid)) {
                            return true;
                        } else {
                            return false;
                        }
                    }
                });
                if (files.length > 0) {
                    File jpegFile = new File(files[0].getAbsolutePath());
                    BufferedImage jpegImage = ImageIO.read(jpegFile);
                    ImageIO.write((RenderedImage) jpegImage, "jpeg", response.getOutputStream());
                } else {
                    String[] isbnslist = null;
                    if (isbn == null || isbn.equals("") || isOk.equals("OFF")) {
                        File noPreview = new File(filePath + "/no-image.gif");
                        BufferedImage noImage = ImageIO.read(noPreview);
                        ImageIO.write((RenderedImage) noImage, "jpeg", response.getOutputStream());
                    } else {
                        if (isbn.indexOf(',') != -1) {
                            isbnslist = isbn.split(",");
                        } else {
                            isbnslist = new String[1];
                            isbnslist[0] = isbn;
                        }
                        for (int i = 0; i < isbnslist.length; i++) {
                            String string = isbnslist[i];
                            try {
                                File jpegFile = new File(filePath + "/" + string + ".jpeg");
                                if (jpegFile.exists()) {
                                    BufferedImage jpegImage = ImageIO.read(jpegFile);
                                    ImageIO.write((RenderedImage) jpegImage, "jpeg", response.getOutputStream());
                                    break;
                                } else {
                                    boolean proceedFlag = true;
                                    if (proceedFlag) {
                                        try {
                                            String syndeticStatus = SystemFilesLoader.getInstance().getNewgenlibProperties().getProperty("SYNDETICS", "OFF");
                                            String syndeticClientCode = SystemFilesLoader.getInstance().getNewgenlibProperties().getProperty("SYNDETICS_CLIENT_CODE");
                                            if (syndeticStatus.equals("ON")) {
                                                try {
                                                    URL url2 = new URL("http://www.syndetics.com/index.aspx?isbn=" + string + "/MC.GIF&client=" + syndeticClientCode);
                                                    URLConnection urlconn = url2.openConnection();
                                                    if (ProxySettings.getInstance().isProxyAvailable()) {
                                                        urlconn.setRequestProperty("Proxy-Authorization", "Basic " + ProxySettings.getInstance().getEncodedPassword());
                                                    }
                                                    BufferedImage image2 = ImageIO.read(urlconn.getInputStream());
                                                    if (image2.getWidth() != 1 && image2.getHeight() != 1) {
                                                        ImageIO.write(image2, "jpeg", response.getOutputStream());
                                                        ImageIO.write(image2, "jpeg", jpegFile);
                                                        proceedFlag = false;
                                                        break;
                                                    } else {
                                                        proceedFlag = true;
                                                    }
                                                } catch (Exception ex) {
                                                    proceedFlag = true;
                                                }
                                            }
                                        } catch (Exception e) {
                                            proceedFlag = true;
                                        }
                                    }
                                    if (proceedFlag) {
                                        try {
                                            String thumbnailURL = "";
                                            URL url = new URL("http://books.google.com/books?bibkeys=ISBN:" + string + "&jscmd=viewapi&callback=mycallback");
                                            URLConnection urlconngoogle = url.openConnection();
                                            if (ProxySettings.getInstance().isProxyAvailable()) {
                                                urlconngoogle.setRequestProperty("Proxy-Authorization", "Basic " + ProxySettings.getInstance().getEncodedPassword());
                                            }
                                            urlconngoogle.setRequestProperty("User-Agent", "I am a real browser like Mozilla or MSIE");
                                            BufferedReader br = new BufferedReader(new InputStreamReader(urlconngoogle.getInputStream(), "UTF-8"));
                                            String strjson = "";
                                            while (br.ready()) {
                                                strjson += br.readLine();
                                            }
                                            br.close();
                                            strjson = strjson.replace("mycallback(", "").trim();
                                            strjson = strjson.substring(0, (strjson.length() - 2));
                                            try {
                                                JSONObject json = new JSONObject(strjson);
                                                Iterator isbnvals = json.keys();
                                                while (isbnvals.hasNext()) {
                                                    String key = isbnvals.next().toString();
                                                    JSONObject job = json.getJSONObject(key);
                                                    thumbnailURL = job.getString("thumbnail_url");
                                                }
                                            } catch (Exception ex) {
                                                proceedFlag = true;
                                            }
                                            if (!thumbnailURL.equals("")) {
                                                URL url4 = new URL(thumbnailURL);
                                                URLConnection urlconny = url4.openConnection();
                                                if (ProxySettings.getInstance().isProxyAvailable()) {
                                                    urlconny.setRequestProperty("Proxy-Authorization", "Basic " + ProxySettings.getInstance().getEncodedPassword());
                                                }
                                                urlconny.setRequestProperty("User-Agent", "I am a real browser like Mozilla or MSIE");
                                                BufferedImage image4 = ImageIO.read(urlconny.getInputStream());
                                                if (image4.getWidth() != 1 && image4.getHeight() != 1) {
                                                    ImageIO.write(image4, "jpeg", response.getOutputStream());
                                                    ImageIO.write(image4, "jpeg", jpegFile);
                                                    proceedFlag = false;
                                                    break;
                                                }
                                            }
                                        } catch (Exception e) {
                                            proceedFlag = true;
                                        }
                                    }
                                    if (proceedFlag) {
                                        try {
                                            URL url2 = new URL("http://covers.librarything.com/devkey/cdri/medium/isbn/" + string);
                                            URLConnection urlconn = url2.openConnection();
                                            if (ProxySettings.getInstance().isProxyAvailable()) {
                                                urlconn.setRequestProperty("Proxy-Authorization", "Basic " + ProxySettings.getInstance().getEncodedPassword());
                                            }
                                            BufferedImage image2 = ImageIO.read(urlconn.getInputStream());
                                            if (image2.getWidth() == 1 && image2.getHeight() == 1) {
                                                proceedFlag = true;
                                            } else {
                                                ImageIO.write(image2, "jpeg", response.getOutputStream());
                                                ImageIO.write(image2, "jpeg", jpegFile);
                                                proceedFlag = false;
                                                break;
                                            }
                                        } catch (Exception e) {
                                            proceedFlag = true;
                                        }
                                    }
                                    if (proceedFlag) {
                                        try {
                                            URL url3 = new URL("http://covers.openlibrary.org/b/isbn/" + string + "-M.jpg");
                                            URLConnection urlconnx = url3.openConnection();
                                            if (ProxySettings.getInstance().isProxyAvailable()) {
                                                urlconnx.setRequestProperty("Proxy-Authorization", "Basic " + ProxySettings.getInstance().getEncodedPassword());
                                            }
                                            BufferedImage image3 = ImageIO.read(urlconnx.getInputStream());
                                            if (image3.getWidth() == 1 && image3.getHeight() == 1) {
                                                proceedFlag = true;
                                            } else {
                                                ImageIO.write(image3, "jpeg", response.getOutputStream());
                                                ImageIO.write(image3, "jpeg", jpegFile);
                                                proceedFlag = false;
                                                break;
                                            }
                                        } catch (Exception ex) {
                                            proceedFlag = true;
                                        }
                                    }
                                    if (proceedFlag) {
                                        try {
                                            File noPreview = new File(filePath + "/no-image.gif");
                                            BufferedImage noImage = ImageIO.read(noPreview);
                                            ImageIO.write((RenderedImage) noImage, "jpeg", response.getOutputStream());
                                        } catch (Exception ex) {
                                        }
                                    }
                                }
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            } finally {
                            }
                        }
                    }
                }
            }
        } else {
            String[] isbnslist = null;
            if (isbn == null || isbn.equals("") || isOk.equals("OFF")) {
                File noPreview = new File(filePath + "/no-image.gif");
                BufferedImage noImage = ImageIO.read(noPreview);
                ImageIO.write((RenderedImage) noImage, "jpeg", response.getOutputStream());
            } else {
                if (isbn.indexOf(',') != -1) {
                    isbnslist = isbn.split(",");
                } else {
                    isbnslist = new String[1];
                    isbnslist[0] = isbn;
                }
                for (int i = 0; i < isbnslist.length; i++) {
                    String string = isbnslist[i];
                    try {
                        File jpegFile = new File(filePath + "/" + string + ".jpeg");
                        if (jpegFile.exists()) {
                            BufferedImage jpegImage = ImageIO.read(jpegFile);
                            ImageIO.write((RenderedImage) jpegImage, "jpeg", response.getOutputStream());
                            break;
                        } else {
                            boolean proceedFlag = true;
                            if (proceedFlag) {
                                try {
                                    String syndeticStatus = SystemFilesLoader.getInstance().getNewgenlibProperties().getProperty("SYNDETICS", "OFF");
                                    String syndeticClientCode = SystemFilesLoader.getInstance().getNewgenlibProperties().getProperty("SYNDETICS_CLIENT_CODE");
                                    if (syndeticStatus.equals("ON")) {
                                        try {
                                            URL url2 = new URL("http://www.syndetics.com/index.aspx?isbn=" + string + "/MC.GIF&client=" + syndeticClientCode);
                                            URLConnection urlconn = url2.openConnection();
                                            if (ProxySettings.getInstance().isProxyAvailable()) {
                                                urlconn.setRequestProperty("Proxy-Authorization", "Basic " + ProxySettings.getInstance().getEncodedPassword());
                                            }
                                            BufferedImage image2 = ImageIO.read(urlconn.getInputStream());
                                            if (image2.getWidth() != 1 && image2.getHeight() != 1) {
                                                ImageIO.write(image2, "jpeg", response.getOutputStream());
                                                ImageIO.write(image2, "jpeg", jpegFile);
                                                proceedFlag = false;
                                                break;
                                            } else {
                                                proceedFlag = true;
                                            }
                                        } catch (Exception ex) {
                                            proceedFlag = true;
                                        }
                                    }
                                } catch (Exception e) {
                                    proceedFlag = true;
                                }
                            }
                            if (proceedFlag) {
                                try {
                                    String thumbnailURL = "";
                                    URL url = new URL("http://books.google.com/books?bibkeys=ISBN:" + string + "&jscmd=viewapi&callback=mycallback");
                                    URLConnection urlconngoogle = url.openConnection();
                                    if (ProxySettings.getInstance().isProxyAvailable()) {
                                        urlconngoogle.setRequestProperty("Proxy-Authorization", "Basic " + ProxySettings.getInstance().getEncodedPassword());
                                    }
                                    urlconngoogle.setRequestProperty("User-Agent", "I am a real browser like Mozilla or MSIE");
                                    BufferedReader br = new BufferedReader(new InputStreamReader(urlconngoogle.getInputStream(), "UTF-8"));
                                    String strjson = "";
                                    while (br.ready()) {
                                        strjson += br.readLine();
                                    }
                                    br.close();
                                    strjson = strjson.replace("mycallback(", "").trim();
                                    strjson = strjson.substring(0, (strjson.length() - 2));
                                    try {
                                        JSONObject json = new JSONObject(strjson);
                                        Iterator isbnvals = json.keys();
                                        while (isbnvals.hasNext()) {
                                            String key = isbnvals.next().toString();
                                            JSONObject job = json.getJSONObject(key);
                                            thumbnailURL = job.getString("thumbnail_url");
                                        }
                                    } catch (Exception ex) {
                                        proceedFlag = true;
                                    }
                                    if (!thumbnailURL.equals("")) {
                                        URL url4 = new URL(thumbnailURL);
                                        URLConnection urlconny = url4.openConnection();
                                        if (ProxySettings.getInstance().isProxyAvailable()) {
                                            urlconny.setRequestProperty("Proxy-Authorization", "Basic " + ProxySettings.getInstance().getEncodedPassword());
                                        }
                                        urlconny.setRequestProperty("User-Agent", "I am a real browser like Mozilla or MSIE");
                                        BufferedImage image4 = ImageIO.read(urlconny.getInputStream());
                                        if (image4.getWidth() != 1 && image4.getHeight() != 1) {
                                            ImageIO.write(image4, "jpeg", response.getOutputStream());
                                            ImageIO.write(image4, "jpeg", jpegFile);
                                            proceedFlag = false;
                                            break;
                                        }
                                    }
                                } catch (Exception e) {
                                    proceedFlag = true;
                                }
                            }
                            if (proceedFlag) {
                                try {
                                    URL url2 = new URL("http://covers.librarything.com/devkey/cdri/medium/isbn/" + string);
                                    URLConnection urlconn = url2.openConnection();
                                    if (ProxySettings.getInstance().isProxyAvailable()) {
                                        urlconn.setRequestProperty("Proxy-Authorization", "Basic " + ProxySettings.getInstance().getEncodedPassword());
                                    }
                                    BufferedImage image2 = ImageIO.read(urlconn.getInputStream());
                                    if (image2.getWidth() == 1 && image2.getHeight() == 1) {
                                        proceedFlag = true;
                                    } else {
                                        ImageIO.write(image2, "jpeg", response.getOutputStream());
                                        ImageIO.write(image2, "jpeg", jpegFile);
                                        proceedFlag = false;
                                        break;
                                    }
                                } catch (Exception e) {
                                    proceedFlag = true;
                                }
                            }
                            if (proceedFlag) {
                                try {
                                    URL url3 = new URL("http://covers.openlibrary.org/b/isbn/" + string + "-M.jpg");
                                    URLConnection urlconnx = url3.openConnection();
                                    if (ProxySettings.getInstance().isProxyAvailable()) {
                                        urlconnx.setRequestProperty("Proxy-Authorization", "Basic " + ProxySettings.getInstance().getEncodedPassword());
                                    }
                                    BufferedImage image3 = ImageIO.read(urlconnx.getInputStream());
                                    if (image3.getWidth() == 1 && image3.getHeight() == 1) {
                                        proceedFlag = true;
                                    } else {
                                        ImageIO.write(image3, "jpeg", response.getOutputStream());
                                        ImageIO.write(image3, "jpeg", jpegFile);
                                        proceedFlag = false;
                                        break;
                                    }
                                } catch (Exception ex) {
                                    proceedFlag = true;
                                }
                            }
                            if (proceedFlag) {
                                try {
                                    File noPreview = new File(filePath + "/no-image.gif");
                                    BufferedImage noImage = ImageIO.read(noPreview);
                                    ImageIO.write((RenderedImage) noImage, "jpeg", response.getOutputStream());
                                } catch (Exception ex) {
                                }
                            }
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    } finally {
                    }
                }
            }
        }
    }
