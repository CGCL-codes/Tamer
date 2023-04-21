    public void dispatch(String strurl, HttpServletResponse res) throws Exception {
        try {
            URL url = new URL(strurl);
            HttpURLConnection huc = (HttpURLConnection) (url.openConnection());
            int code = huc.getResponseCode();
            res.addHeader("Link", huc.getHeaderField("Link"));
            Map m = huc.getHeaderFields();
            if (code == 200) {
                res.setContentType(huc.getContentType());
                InputStream is = huc.getInputStream();
                OutputStream out = res.getOutputStream();
                byte[] bytes = new byte[1024];
                int len;
                while ((len = is.read(bytes)) != -1) {
                    out.write(bytes, 0, len);
                }
                out.close();
                is.close();
            } else {
                System.out.println("An error of type " + code + " occurred for:" + strurl);
                throw new Exception("Cannot get " + url.toString());
            }
        } catch (MalformedURLException e) {
            throw new Exception("A MalformedURLException occurred for:" + strurl);
        } catch (IOException e) {
            throw new Exception("An IOException occurred attempting to connect to " + strurl);
        }
    }
