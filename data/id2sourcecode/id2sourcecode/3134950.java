    private void submitAnnotations() throws IOException {
        final URL url = new URL(this.getDocumentBase(), this.getParameter(PARAM_URL));
        System.out.println(url);
        System.out.flush();
        StringBuffer outBuff = new StringBuffer();
        outBuff.append(PARAM_IMAGEID).append("=").append(this.getParameter(PARAM_IMAGEID)).append("&");
        Iterator annos = this.annotations.iterator();
        while (annos.hasNext()) {
            Annotation anno = (Annotation) annos.next();
            outBuff.append("annotation=");
            outBuff.append(anno.getAnnotation() + ANNO_PART_SPLIT);
            outBuff.append((int) (anno.x / this.wscale) + ANNO_PART_SPLIT);
            outBuff.append((int) (anno.y / this.hscale) + ANNO_PART_SPLIT);
            outBuff.append((int) (anno.width / this.wscale) + ANNO_PART_SPLIT);
            outBuff.append((int) (anno.height / this.hscale));
            outBuff.append("&");
        }
        System.out.println("Posting to " + url.toString());
        System.out.println(outBuff.toString());
        final HttpURLConnection huc = (HttpURLConnection) url.openConnection();
        huc.setRequestMethod("POST");
        huc.setDoOutput(true);
        huc.setDoInput(true);
        huc.setUseCaches(false);
        Writer out = new BufferedWriter(new OutputStreamWriter(huc.getOutputStream()));
        out.write(outBuff.toString());
        out.flush();
        out.close();
        BufferedReader inbuf = new BufferedReader(new InputStreamReader(huc.getInputStream()));
        String inputLine;
        String sContent = "";
        while ((inputLine = inbuf.readLine()) != null) {
            if (inputLine.trim().length() > 0) sContent += inputLine + "\n";
        }
        inbuf.close();
        System.out.println("Returned:" + sContent);
    }
