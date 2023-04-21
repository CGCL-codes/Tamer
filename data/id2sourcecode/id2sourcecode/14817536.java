    private File download(String urlString) throws IOException, NotSupportedException {
        OutputStreamWriter osw = null;
        InputStream is = null;
        OutputStream fos = null;
        URL url = new URL(urlString);
        File file = new File(System.getProperty("user.home") + File.separator + "XExternalEditor" + File.separator + "tmp" + File.separator + new File(this.getFileName(url)).getName());
        if (file.exists()) {
            file.delete();
        } else if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        file.createNewFile();
        file.deleteOnExit();
        try {
            if (this.type.equalsIgnoreCase("edit file")) {
                this.text.setText(LC.tr("Establishing the connection"));
                this.bar.setIndeterminate(true);
                URLConnection con = url.openConnection();
                con.setUseCaches(false);
                con.addRequestProperty("User-Agent", "X-External-Editor");
                con.setRequestProperty("Cookie", this.cookie);
                con.connect();
                is = con.getInputStream();
                fos = new FileOutputStream(file);
                byte[] buf = new byte[1024 * 8];
                double len = -1, sumlen = 0, nano = System.nanoTime();
                double contentLength = con.getContentLength();
                this.text.setText(LC.tr("Downloading the file"));
                this.bar.setIndeterminate(false);
                this.bar.setValue(0);
                while ((len = is.read(buf)) != -1) {
                    fos.write(buf, 0, (int) len);
                    sumlen += len;
                    int percent = (int) ((sumlen / contentLength) * 100);
                    double sec = (System.nanoTime() - nano) / sumlen;
                    String secString = (int) sec + " B";
                    if (sec > 1024) {
                        secString = XExternalEditor.nf.format(sec / 1024) + " KB";
                    }
                    this.text.setText(LC.tr("Downloading the file").concat(" [" + percent + " %] [" + secString + "/s]"));
                    this.bar.setValue(percent);
                }
                this.bar.setValue(100);
                this.text.setText(LC.tr("File downloaded"));
            } else if (this.type.equalsIgnoreCase("edit text")) {
                this.text.setText(LC.tr("Downloading the wiki text"));
                GetMethod gethtmlpage = new GetMethod(urlString);
                int status = this.client.executeMethod(gethtmlpage);
                if (status != 200) {
                    this.showHTMLFile(gethtmlpage.getResponseBodyAsString());
                    throw new RuntimeException(LC.tr("Wiki page could not be loaded!"));
                }
                this.text.setText(LC.tr("Analyzing the wiki text"));
                String html = gethtmlpage.getResponseBodyAsString();
                Pattern pText = Pattern.compile(".*<textarea[^>]+name=\"wpTextbox1\"[^>]*>(.*)</textarea>.*", Pattern.DOTALL | Pattern.CASE_INSENSITIVE);
                Matcher mText = pText.matcher(html);
                String text = null;
                if (mText.find()) {
                    text = mText.group(1);
                    text = StringEscapeUtils.unescapeHtml(text);
                    if (!text.contains(System.getProperty("line.separator"))) {
                        _logger.warn("changing line ends to " + System.getProperty("line.separator"));
                        text = text.replace("\n", System.getProperty("line.separator"));
                    }
                } else {
                    throw new RuntimeException(LC.tr("Wiki text not found in HTML document!"));
                }
                Pattern pTime = Pattern.compile(".*<input type='hidden' value=\"(.*)\" name=\"wpEdittime\" />.*", Pattern.DOTALL | Pattern.CASE_INSENSITIVE);
                Matcher mTime = pTime.matcher(html);
                if (mTime.find()) {
                    this.time = mTime.group(1);
                } else {
                    throw new RuntimeException(LC.tr("Edit time not found in HTML document!"));
                }
                Pattern pStarttime = Pattern.compile(".*<input type='hidden' value=\"(.*)\" name=\"wpStarttime\" />.*", Pattern.DOTALL | Pattern.CASE_INSENSITIVE);
                Matcher mStarttime = pStarttime.matcher(html);
                if (mStarttime.find()) {
                    this.starttime = mStarttime.group(1);
                } else {
                    throw new RuntimeException(LC.tr("Start time not found in HTML document!"));
                }
                Pattern pToken = Pattern.compile(".*<input type=[\"']hidden[\"'] value=\"([^\"]*)\" name=\"wpEditToken\" />.*", Pattern.DOTALL | Pattern.CASE_INSENSITIVE);
                Matcher mToken = pToken.matcher(html);
                if (mToken.find()) {
                    this.token = mToken.group(1);
                } else {
                    throw new RuntimeException(LC.tr("Edit token not found in HTML document!"));
                }
                fos = new FileOutputStream(file);
                osw = new OutputStreamWriter(fos, this.responseCharSet);
                if (this.responseCharSet.equals("UTF-8")) {
                    if (!text.startsWith(new String(BOM))) {
                        fos.write(BOM);
                    }
                }
                osw.append(text);
                this.text.setText(LC.tr("Wiki text downloaded!"));
            } else {
                throw new NotSupportedException("type = " + this.type);
            }
        } finally {
            if (osw != null) {
                osw.close();
            }
            if (fos != null) {
                fos.close();
            }
            if (is != null) {
                is.close();
            }
        }
        return file;
    }
