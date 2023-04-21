    protected boolean downloadImage(String url, Hashtable params, String outputDirectory, String filename) {
        boolean success = true;
        log.debug("Requesting image URL " + url);
        try {
            PostMethod post = new PostMethod(url);
            Enumeration keys = params.keys();
            while (keys.hasMoreElements()) {
                String key = (String) keys.nextElement();
                post.setParameter(key, (String) params.get(key));
            }
            post.setRequestHeader("Referer", refererURL);
            post.setRequestHeader("Accept-Language", "en");
            post.setRequestHeader("User-Agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1; .NET CLR 1.1.4322)");
            post.setRequestHeader("Accept", "image/gif, image/x-xbitmap, image/jpeg, image/pjpeg, application/x-shockwave-flash, application/vnd.ms-excel, application/vnd.ms-powerpoint, application/msword, */*");
            post.setRequestHeader("Pragma", "no-cache");
            post.setFollowRedirects(true);
            client.executeMethod(post);
            InputStream in = post.getResponseBodyAsStream();
            byte[] data = new byte[1024];
            int length;
            java.io.FileOutputStream out = new java.io.FileOutputStream(outputDirectory + File.separator + filename);
            while ((length = in.read(data)) >= 0) out.write(data, 0, length);
            out.flush();
            in.close();
            out.close();
            post.releaseConnection();
        } catch (Exception e) {
            e.printStackTrace();
            success = false;
        }
        return (success);
    }
