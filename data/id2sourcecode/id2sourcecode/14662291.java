    private int load(URL url, File file, String out) {
        InputSource is = null;
        if (url != null) is = new InputSource(url); else if (file != null) is = new InputSource(file);
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(new File(out));
        } catch (FileNotFoundException fnfe) {
            fnfe.printStackTrace();
            return -1;
        }
        BufferedOutputStream bos = new BufferedOutputStream(fos);
        int idxDot = out.lastIndexOf(".");
        if (idxDot < 0) {
            System.out.println("Unknown destination file type");
            return -1;
        }
        String encoding = out.substring(idxDot + 1);
        System.out.println("File: " + file + " Encoding: " + encoding);
        if (encoding.equals("x3db")) {
            writer = new X3DBinaryRetainedDirectExporter(bos, 3, 1, console, compressionMethod, quantizeParam);
            ((X3DBinaryRetainedDirectExporter) writer).setConvertOldContent(upgradeContent);
        } else if (encoding.equals("x3dv")) {
            writer = new X3DClassicRetainedExporter(bos, 3, 1, console);
            ((X3DClassicRetainedExporter) writer).setConvertOldContent(upgradeContent);
        } else if (encoding.equals("x3d")) {
            writer = new X3DXMLRetainedExporter(bos, 3, 1, console);
            ((X3DXMLRetainedExporter) writer).setConvertOldContent(upgradeContent);
        } else {
            System.out.println("Unknown destination encoding");
            return -1;
        }
        reader.setContentHandler(writer);
        reader.setRouteHandler(writer);
        reader.setScriptHandler(writer);
        reader.setProtoHandler(writer);
        try {
            reader.parse(is);
        } catch (Exception e) {
            StringBuffer buf = new StringBuffer("Error: ");
            if (e instanceof FieldException) {
                FieldException fe = (FieldException) e;
                String name = fe.getFieldName();
                if (name != null) {
                    buf.append("Field name: ");
                    buf.append(name);
                }
            }
            if (e instanceof VRMLParseException) {
                buf.append(" Line: ");
                buf.append(((VRMLParseException) e).getLineNumber());
                buf.append(" Column: ");
                buf.append(((VRMLParseException) e).getColumnNumber());
                buf.append('\n');
            } else if (e instanceof InvalidFieldFormatException) {
                buf.append(" Line: ");
                buf.append(((InvalidFieldFormatException) e).getLineNumber());
                buf.append(" Column: ");
                buf.append(((InvalidFieldFormatException) e).getColumnNumber());
                buf.append('\n');
            }
            if (e != null) {
                String txt = e.getMessage();
                buf.append(txt);
            }
            System.out.println(buf);
            e.printStackTrace();
            System.out.println("Exiting with error code: -1");
            return -1;
        }
        return 0;
    }
