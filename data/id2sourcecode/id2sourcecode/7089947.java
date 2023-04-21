    @Override
    public void parse() {
        BufferedReader br = null;
        try {
            URL url = new URL(this.getView().getUrl());
            br = new BufferedReader(new InputStreamReader(url.openStream()));
            handler.process(br);
        } catch (MalformedURLException ex) {
            this.getView().log("不正确的URL地址！");
        } catch (IOException ioe) {
            this.getView().log("无法读取该网址！");
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException ex) {
                    this.getView().log("无法关闭打开的URL输入流！");
                }
            }
        }
    }
