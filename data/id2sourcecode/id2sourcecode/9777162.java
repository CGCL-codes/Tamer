    private void processParameters() throws BadElementException, IOException {
        type = IMGTEMPLATE;
        originalType = ORIGINAL_PS;
        InputStream is = null;
        try {
            String errorID;
            if (rawData == null) {
                is = url.openStream();
                errorID = url.toString();
            } else {
                is = new java.io.ByteArrayInputStream(rawData);
                errorID = "Byte array";
            }
            String boundingbox = null;
            Reader r = new BufferedReader(new InputStreamReader(is));
            while (r.ready()) {
                char c;
                StringBuffer sb = new StringBuffer();
                while ((c = ((char) r.read())) != '\n') {
                    sb.append(c);
                }
                if (sb.toString().startsWith("%%BoundingBox:")) {
                    boundingbox = sb.toString();
                }
                if (sb.toString().startsWith("%%TemplateBox:")) {
                    boundingbox = sb.toString();
                }
                if (sb.toString().startsWith("%%EndComments")) {
                    break;
                }
            }
            if (boundingbox == null) return;
            StringTokenizer st = new StringTokenizer(boundingbox, ": \r\n");
            st.nextElement();
            String xx1 = st.nextToken();
            String yy1 = st.nextToken();
            String xx2 = st.nextToken();
            String yy2 = st.nextToken();
            int left = Integer.parseInt(xx1);
            int top = Integer.parseInt(yy1);
            int right = Integer.parseInt(xx2);
            int bottom = Integer.parseInt(yy2);
            int inch = 1;
            dpiX = 72;
            dpiY = 72;
            scaledHeight = (float) (bottom - top) / inch * 1f;
            scaledHeight = 800;
            setTop(scaledHeight);
            scaledWidth = (float) (right - left) / inch * 1f;
            scaledWidth = 800;
            setRight(scaledWidth);
        } finally {
            if (is != null) {
                is.close();
            }
            plainWidth = width();
            plainHeight = height();
        }
    }
