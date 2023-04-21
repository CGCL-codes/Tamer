    public static String getJavadocText(URL url, boolean pkg) {
        if (url == null) return null;
        HTMLEditorKit.Parser parser = null;
        InputStream is = null;
        String charset = null;
        for (; ; ) {
            try {
                is = url.openStream();
                parser = new ParserDelegator();
                String urlStr = url.toString();
                int offsets[] = new int[2];
                Reader reader = charset == null ? new InputStreamReader(is) : new InputStreamReader(is, charset);
                if (pkg) {
                    offsets = parsePackage(reader, parser, charset != null);
                } else if (urlStr.indexOf('#') > 0) {
                    String memberName = urlStr.substring(urlStr.indexOf('#') + 1);
                    if (memberName.length() > 0) offsets = parseMember(reader, memberName, parser, charset != null);
                } else {
                    offsets = parseClass(reader, parser, charset != null);
                }
                if (offsets != null && offsets[0] != -1 && offsets[1] > offsets[0]) {
                    return getTextFromURLStream(url, offsets[0], offsets[1], charset);
                }
                break;
            } catch (ChangedCharSetException e) {
                if (charset == null) {
                    charset = getCharSet(e);
                } else {
                    e.printStackTrace();
                    break;
                }
            } catch (IOException ioe) {
                break;
            } finally {
                parser = null;
                if (is != null) {
                    try {
                        is.close();
                    } catch (IOException ioe) {
                        ioe.printStackTrace();
                    }
                }
            }
        }
        return null;
    }
