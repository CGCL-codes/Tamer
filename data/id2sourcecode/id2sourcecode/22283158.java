    private Paper getPaperFromResult(WebSearchResult result, int cnt) {
        String contenttype = "";
        String content = "";
        WebPaper p = new WebPaper();
        try {
            URL url = new URL(result.getUrl());
            URLConnection urlConnection = url.openConnection();
            urlConnection.connect();
            contenttype = urlConnection.getContentType();
            if (contenttype.contains("text/html")) {
                InputStream input = url.openStream();
                Reader reader = new InputStreamReader(input);
                BufferedReader bufferedReader = new BufferedReader(reader);
                String strLine = "";
                int count = 0;
                while (count < 10000) {
                    strLine = bufferedReader.readLine();
                    if (strLine != null && strLine != "") {
                        content += strLine;
                    }
                    count++;
                }
                bufferedReader.close();
                p.setPaperText(content);
                Html2TextFilter filter = new Html2TextFilter();
                filter.execute(p);
            } else if (contenttype.contains("application/pdf")) {
                BufferedInputStream bis = new BufferedInputStream(urlConnection.getInputStream());
                BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream("./temp.pdf"));
                int i;
                while ((i = bis.read()) != -1) {
                    bos.write(i);
                }
                bis.close();
                bos.close();
                PDFTextStripper pts = new PDFTextStripper();
                content = pts.getText(PDDocument.load("temp.pdf"));
                File f = new File("temp.pdf");
                f.delete();
                p.setPaperText(content);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        p.setSource(result.getUrl());
        p.setAccess_date(new Date());
        p.setTitle((cnt + 1) + ", " + result.getTitle());
        p.setNachname((cnt + 1) + ", " + result.getTitle());
        logger.info("Paper erstellt: " + p.getTitle() + ", " + p.getSource());
        return p;
    }
