    public static final List<ImageInfo> getImageInfoList(final String urlStr) {
        ArrayList<ImageInfo> list = new ArrayList<ImageInfo>();
        byte[] line = new byte[1024];
        int byteSize = 0;
        try {
            URL url = new URL(urlStr);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.connect();
            InputStream is = con.getInputStream();
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            while ((byteSize = is.read(line)) > 0) {
                out.write(line, 0, byteSize);
            }
            String[] outList = out.toString().split("\n", 0);
            int i = 0;
            while (i < outList.length) {
                String[] outList2 = outList[i].toString().split("\t", 0);
                ImageInfo imageInfo = new ImageInfo();
                System.out.println(i + "番目の要素 = :" + outList2[0]);
                imageInfo.key = outList2[0];
                System.out.println(i + "番目の要素 = :" + outList2[1]);
                imageInfo.filename = outList2[1];
                i++;
                list.add(imageInfo);
            }
            is.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
