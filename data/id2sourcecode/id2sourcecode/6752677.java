    private static void buildSourceFile(String type, File inFile, String path) throws IOException {
        File outFolder = new File(path);
        if ((!outFolder.isDirectory()) && (!outFolder.mkdirs())) throw new IOException("not a directory: " + outFolder.getAbsolutePath());
        File outFile = new File(outFolder, inFile.getName() + ".html");
        InputStream in = null;
        OutputStream out = null;
        try {
            in = new FileInputStream(inFile);
            out = new ByteArrayOutputStream();
            String tmpPath = path.substring(path.indexOf("/source/") + 1).replaceAll("[^/]+", "..") + "/";
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(out));
            bw.write("<html>\n");
            bw.write("<head>\n");
            bw.write("<link rel='stylesheet' href='" + tmpPath + "js/sh/SyntaxHighlighter.css' type='text/css' />\n");
            bw.write("<script src='" + tmpPath + "js/sh/shCore.js'></script>\n");
            if ("css".equals(type)) bw.write("<script src='" + tmpPath + "js/sh/shBrushCss.js'></script>\n");
            if ("java".equals(type)) bw.write("<script src='" + tmpPath + "js/sh/shBrushJava.js'></script>\n");
            if ("js".equals(type)) bw.write("<script src='" + tmpPath + "js/sh/shBrushJScript.js'></script>\n");
            if ("xml".equals(type)) bw.write("<script src='" + tmpPath + "js/sh/shBrushXml.js'></script>\n");
            bw.write("<style>\n");
            bw.write("* {\n");
            bw.write("font-family:Courier New,monospace;\n");
            bw.write("  padding: 0;\n");
            bw.write("  margin: 0;\n");
            bw.write("  white-space: nowrap;\n");
            bw.write("  font-size: 11px;\n");
            bw.write("}\n");
            bw.write(".dp-highlighter {\n");
            bw.write("  white-space: nowrap;\n");
            bw.write("  overflow: visible;\n");
            bw.write("  width: 600px;\n");
            bw.write("  font-size: 11px;\n");
            bw.write("  font-family:Courier New,monospace;\n");
            bw.write("}\n");
            bw.write("</style>\n");
            bw.write("</head>\n");
            bw.write("<body>\n");
            bw.write("<textarea name='code' class='" + type + ":nogutter' rows='15' cols='120'>\n\n");
            String line = null;
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            while (null != (line = br.readLine())) bw.write(line + "\n");
            bw.write("\n</textarea>\n");
            bw.write("<script class='javascript'>\n");
            bw.write("dp.SyntaxHighlighter.HighlightAll(\"code\");\n");
            bw.write("</script>\n");
            bw.write("</body>\n");
            bw.write("</html>");
            bw.close();
        } finally {
            try {
                if (out != null) out.close();
            } catch (IOException e) {
            }
            try {
                if (in != null) in.close();
            } catch (IOException e) {
            }
        }
    }
