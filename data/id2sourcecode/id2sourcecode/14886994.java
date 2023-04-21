    synchronized void startLoading() {
        if (negotiation == URL_BYTE_LOADER) {
            try {
                DataInputStream d;
                int contentLength = -1;
                URLConnection urlc = url.openConnection();
                urlc.connect();
                String content_type = urlc.getContentType();
                wrongMIMETypeException = null;
                long time0 = System.currentTimeMillis();
                if (content_type == null || content_type.equalsIgnoreCase("x-world/x-vrml") || content_type.equalsIgnoreCase("model/vrml") || content_type.equalsIgnoreCase("model/vrml;charset=ISO-8859-1")) {
                    InputStream is = urlc.getInputStream();
                    d = new DataInputStream(is);
                    contentLength = urlc.getContentLength();
                    buffer = new byte[contentLength];
                    content = buffer;
                    if (d != null) {
                        d.readFully(buffer, 0, contentLength);
                    }
                } else if (content_type.equalsIgnoreCase("model/vrml.gzip")) {
                    InputStream is = urlc.getInputStream();
                    GZIPInputStream gis = new GZIPInputStream(is);
                    StringBuffer sb = new StringBuffer();
                    BufferedReader zipReader = new BufferedReader(new InputStreamReader(gis));
                    char chars[] = new char[1024];
                    int len = 0;
                    while ((len = zipReader.read(chars, 0, chars.length)) >= 0) {
                        sb.append(chars, 0, len);
                    }
                    chars = null;
                    gis.close();
                    zipReader.close();
                    content = sb.toString().getBytes();
                } else if (content_type.equalsIgnoreCase("model/vrml.encrypted")) {
                    InputStream is = urlc.getInputStream();
                    StringBuffer sb = new StringBuffer();
                    Cipher pbeCipher = createCipher();
                    if (pbeCipher != null) {
                        CipherInputStream cis = new CipherInputStream(is, pbeCipher);
                        BufferedReader bufReader = new BufferedReader(new InputStreamReader(cis));
                        char chars[] = new char[1024];
                        int len = 0;
                        while ((len = bufReader.read(chars, 0, chars.length)) >= 0) {
                            sb.append(chars, 0, len);
                        }
                        chars = null;
                        cis.close();
                        bufReader.close();
                        content = sb.toString().getBytes();
                    }
                } else if (content_type.equalsIgnoreCase("model/vrml.gzip.encrypted")) {
                    InputStream is = urlc.getInputStream();
                    StringBuffer sb = new StringBuffer();
                    Cipher pbeCipher = createCipher();
                    if (pbeCipher != null) {
                        CipherInputStream cis = new CipherInputStream(is, pbeCipher);
                        GZIPInputStream gis = new GZIPInputStream(cis);
                        BufferedReader bufReader = new BufferedReader(new InputStreamReader(gis));
                        char chars[] = new char[1024];
                        int len = 0;
                        while ((len = bufReader.read(chars, 0, chars.length)) >= 0) {
                            sb.append(chars, 0, len);
                        }
                        chars = null;
                        bufReader.close();
                        gis.close();
                        cis.close();
                        content = sb.toString().getBytes();
                    }
                } else if (content_type.equalsIgnoreCase("text/html;charset=utf-8")) {
                    System.out.println("text/html;charset=utf-8");
                    wrongMIMETypeException = new WrongMIMETypeException();
                    wrongMIMETypeException.setMIMEType(content_type);
                    wrongMIMETypeException.setServerError(urlc.getHeaderField(null));
                } else {
                    System.err.println("ContentNegotiator.startLoading unsupported MIME type: " + content_type);
                }
                long time1 = System.currentTimeMillis();
                yield();
            } catch (IOException ie) {
                System.out.println("ContentNegotiator.startLoading ERROR");
                ie.printStackTrace();
            }
            locked = false;
            notify();
        } else if (negotiation == SOUND_LOADER) {
            content = sound;
            locked = false;
            notify();
        }
    }
