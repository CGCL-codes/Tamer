    public void check() {
        updatesAvailable = false;
        try {
            int cnt;
            URL url = new URL(urlStr);
            URLConnection conn = url.openConnection();
            conn.setDoInput(true);
            conn.setDoOutput(false);
            InputStream is = conn.getInputStream();
            StringBuffer sb = new StringBuffer();
            byte[] buffer = new byte[4096];
            while ((cnt = is.read(buffer, 0, buffer.length)) != -1) sb.append(new String(buffer, 0, cnt));
            is.close();
            String[] arr = sb.toString().split("\n");
            String[] arr2;
            for (int i = 0; i < arr.length; i++) {
                arr2 = arr[i].split(" *: *");
                if ((arr2.length == 2) && ("UBCD Creator".equals(arr2[0]))) {
                    double ver = new Double(arr2[1]).doubleValue();
                    if (ver > AboutPanel.version) updatesAvailable = true;
                }
            }
        } catch (Exception ex) {
            updatesAvailable = false;
            ex.printStackTrace();
        }
    }
