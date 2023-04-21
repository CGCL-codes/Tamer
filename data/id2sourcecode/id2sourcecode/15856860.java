        @Override
        public void run() {
            Calendar cal = Calendar.getInstance();
            String version = String.format("%1$04d.%2$02d.%3$02d", cal.get(Calendar.YEAR), cal.get(Calendar.MONTH) + 1, cal.get(Calendar.DAY_OF_MONTH));
            String remoteUrl = null;
            String remoteVersion = "0000.00.00";
            String path = System.getProperties().getProperty("user.dir");
            File propsFile = new File(path, ".ebadat.properties");
            Properties props = new Properties();
            try {
                FileInputStream fis = new FileInputStream(propsFile);
                props.load(fis);
                fis.close();
            } catch (FileNotFoundException ex) {
                logger.warn(ex, ex);
            } catch (IOException ex) {
                logger.warn(ex, ex);
            }
            if (!props.containsKey("ebadat.version")) {
                props.put("ebadat.version", version);
            }
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder;
            boolean proxyAuth = false;
            try {
                builder = factory.newDocumentBuilder();
                URL url = new URL(UPDATE_URL);
                URLConnection conn = url.openConnection(findProxy(new URI(UPDATE_URL)));
                conn.connect();
                Document doc = builder.parse(conn.getInputStream());
                NodeList nodeList = doc.getElementsByTagName("update");
                if (nodeList.getLength() > 0) {
                    Element updateTag = (Element) nodeList.item(0);
                    NodeList children = updateTag.getChildNodes();
                    Node node = updateTag.getFirstChild();
                    while (node != null) {
                        if (node instanceof Element) {
                            Element tag = (Element) node;
                            if (tag.getTagName().equals("version")) {
                                remoteVersion = tag.getTextContent();
                            } else if (tag.getTagName().equals("url")) {
                                remoteUrl = tag.getTextContent();
                            }
                        }
                        node = node.getNextSibling();
                    }
                    remoteUrl += "/" + remoteVersion + ".zip";
                }
                if (remoteVersion.compareTo(version) > 0) {
                    logger.info("descargando ultima version");
                    URL urlDownload = new URL(remoteUrl);
                    URLConnection connDownload = urlDownload.openConnection();
                    connDownload.connect();
                    DataInputStream dis = new DataInputStream(connDownload.getInputStream());
                    File downloadTemp = File.createTempFile("ebadat", "update");
                    downloadTemp.deleteOnExit();
                    FileOutputStream fos = new FileOutputStream(downloadTemp);
                    byte[] buffer = new byte[1024 * 512];
                    int cuenta = 0;
                    while ((cuenta = dis.read(buffer, 0, buffer.length)) >= 0) {
                        fos.write(buffer, 0, cuenta);
                    }
                    dis.close();
                    fos.close();
                    logger.info("descomprimiendo ultima version");
                    File targetDir = new File(path, "lib-update");
                    targetDir.mkdirs();
                    ZipInputStream zis = new ZipInputStream(new FileInputStream(downloadTemp));
                    ZipEntry zipEntry;
                    ZipFile zipFile = new ZipFile(downloadTemp);
                    while ((zipEntry = zis.getNextEntry()) != null) {
                        InputStream is = zipFile.getInputStream(zipEntry);
                        FileOutputStream fosx = new FileOutputStream(new File(targetDir, zipEntry.getName()));
                        while ((cuenta = is.read(buffer, 0, buffer.length)) >= 0) {
                            fosx.write(buffer, 0, cuenta);
                        }
                        fosx.close();
                        is.close();
                    }
                    zis.close();
                    props.put("ebadat.version", remoteVersion);
                }
            } catch (Exception ex) {
                logger.warn(ex);
            }
            if (proxyAuth) {
                try {
                    Proxy proxyUrl = findProxy(new URI(UPDATE_URL));
                    ProxySettingDialog dialog = new ProxySettingDialog(new Frame(), true);
                    InetSocketAddress addr = (InetSocketAddress) proxyUrl.address();
                    dialog.txtHttpProxy.setText(addr.getHostName());
                    dialog.txtHttpPuertoProxy.setText("" + addr.getPort());
                } catch (URISyntaxException ex) {
                    ex.printStackTrace();
                }
            }
            try {
                FileOutputStream fos = new FileOutputStream(propsFile);
                props.store(fos, "Version del Ebadat");
                fos.close();
            } catch (FileNotFoundException ex) {
                logger.warn(ex, ex);
            } catch (IOException ex) {
                logger.warn(ex, ex);
            }
            if (proxyAuth) {
                verificarVersion();
            }
        }
