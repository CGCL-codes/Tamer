    @SuppressWarnings("unchecked")
    private static void setValues(Node n, Inline inline, HashMap<String, String> valueMap) {
        List<String> urls = null;
        if (valueMap.get("url") != null) {
            String allUrls = valueMap.get("url");
            urls = Util.splitStringToListOfStrings(allUrls);
        } else {
            urls = inline.getUrl();
            String tmpUrls = urls.toString();
            urls = Util.splitStringToListOfStrings(tmpUrls);
        }
        X3DImport importer = X3DImport.getTheImport();
        URL url = null;
        File f = null;
        int urlCount = urls.size();
        boolean loadedFromWeb = false;
        for (int urlIndex = 0; urlIndex < urlCount; urlIndex++) {
            try {
                String path = urls.get(urlIndex);
                if (path.startsWith("\"") && path.endsWith("\"")) path = path.substring(1, path.length() - 1);
                if (path.toLowerCase().startsWith("http://")) {
                    String filename = path.substring(path.lastIndexOf("/") + 1, path.lastIndexOf("."));
                    String fileext = path.substring(path.lastIndexOf("."), path.length());
                    f = File.createTempFile(filename, fileext);
                    url = new URL(path);
                    InputStream is = url.openStream();
                    FileOutputStream os = new FileOutputStream(f);
                    byte[] buffer = new byte[0xFFFF];
                    for (int len; (len = is.read(buffer)) != -1; ) os.write(buffer, 0, len);
                    is.close();
                    os.close();
                    url = f.toURI().toURL();
                    loadedFromWeb = true;
                } else {
                    if (path.startsWith("/") || (path.charAt(1) == ':')) {
                    } else {
                        File x3dfile = importer.getCurrentParser().getFile();
                        path = Util.getRealPath(x3dfile) + path;
                    }
                    f = new File(path);
                    url = f.toURI().toURL();
                    Object testContent = url.getContent();
                    if (testContent == null) continue;
                    loadedFromWeb = false;
                }
                X3DDocument x3dDocument = null;
                try {
                    x3dDocument = X3DDocument.Factory.parse(f);
                } catch (XmlException e) {
                    continue;
                } catch (IOException e) {
                    continue;
                }
                Scene scene = x3dDocument.getX3D().getScene();
                X3DParser sceneParser = new X3DParser(scene, f);
                importer.addNewParser(sceneParser);
                sceneParser.parseScene(n);
                importer.removeCurrentParser();
                break;
            } catch (MalformedURLException e) {
            } catch (IOException e) {
            } finally {
                if (loadedFromWeb && f != null) {
                    f.delete();
                }
            }
        }
    }
