                protected InputStream getStream(URL urlObj) throws IOException {
                    final InputStream inStm = super.getStream(urlObj);
                    final ByteArrayOutputStream btArrOutStm = new ByteArrayOutputStream();
                    int b;
                    final byte[] buff = new byte[2048];
                    while ((b = inStm.read(buff, 0, buff.length)) > 0) btArrOutStm.write(buff, 0, b);
                    final String parsedStr = parseText(btArrOutStm.toString());
                    return new ByteArrayInputStream(parsedStr.getBytes());
                }
