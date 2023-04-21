    @SuppressWarnings({ "rawtypes", "unchecked" })
    public Map<String, String> createHousePackage(Room room) throws IOException, JRException, SQLException {
        Map<String, String> housePackage = new HashMap<String, String>();
        System.out.println("读取房源包路径");
        URL s = HouseService.class.getClassLoader().getResource("");
        String tempDir = s.getPath().substring(0, s.getPath().lastIndexOf("WEB-INF")) + "//housePackage/";
        Room r = houseAndRoomMapper.selectByRoomId(room.getRoomId());
        House h = houseAndRoomMapper.selectHouseByCode(r.getSysHouseId());
        String fileName = h.getHouseCode() + r.getRoomName() + ".zip";
        String file = tempDir + "//" + fileName;
        housePackage.put("fileName", fileName);
        housePackage.put("downFilePath", file);
        FileOutputStream fos = new FileOutputStream(file);
        ZipOutputStream zos = new ZipOutputStream(new BufferedOutputStream(fos));
        zos.setEncoding("GBK");
        String imageUrl = ResourceManager.getString("image.url");
        List<RoomPictures> rps = houseAndRoomMapper.selectRoomPicturesByRoomId(r.getSysRoomId());
        int i = 0;
        long start = System.currentTimeMillis();
        for (RoomPictures rp : rps) {
            String path = rp.getImagepath();
            if (path != null && !path.equals("")) {
                int _a = path.lastIndexOf("/");
                path = path.substring(0, _a) + "//v480x360_" + path.substring(_a + 1);
                URL url = new URL(imageUrl + "//" + path);
                InputStream is = url.openStream();
                String _fileName = "室内图片/" + (i++) + ".jpg";
                CompressionUtil.compressFile(zos, is, _fileName);
                is.close();
            }
        }
        List<RoomPictures> hps = houseAndRoomMapper.selectRoomPicturesByHouseId(r.getSysHouseId());
        i = 0;
        for (RoomPictures rp : hps) {
            String path = rp.getImagepath();
            if (path != null && !path.equals("")) {
                int _a = path.lastIndexOf("/");
                path = path.substring(0, _a) + "//v480x360_" + path.substring(_a + 1);
                URL url = new URL(imageUrl + "//" + path);
                InputStream is = url.openStream();
                String _fileName = "公共区域图片/" + (i++) + ".jpg";
                CompressionUtil.compressFile(zos, is, _fileName);
                is.close();
            }
        }
        long end = System.currentTimeMillis();
        System.out.println("图片打包时间：" + (end - start) / 1000);
        Map map = new HashMap();
        map.put("sysRoomId", r.getSysRoomId());
        map.put("itemType", "jj4");
        map.put("sysHouseId", r.getSysHouseId());
        List<String> _c1 = houseAndRoomMapper.selectItemByRoom(map);
        List<String> _c2 = houseAndRoomMapper.selectItemByPub(map);
        map.put("itemType", "jd");
        List<String> _c3 = houseAndRoomMapper.selectItemByRoom(map);
        List<String> _c4 = houseAndRoomMapper.selectItemByPub(map);
        map.put("itemType", "jj1");
        List<String> _c5 = houseAndRoomMapper.selectItemByRoom(map);
        List<String> _c6 = houseAndRoomMapper.selectItemByPub(map);
        System.out.println("读取jasper文件");
        StringBuilder sb = new StringBuilder();
        List<Map> rooms = houseAndRoomMapper.selectOtherRoom(map);
        int count = 0;
        for (int j = 0; j < rooms.size(); j++) {
            Map rm = rooms.get(j);
            String rentstatus = rm.get("rentstatus") != null ? rm.get("rentstatus").toString() : "";
            String position = rm.get("roomposition") != null ? rm.get("roomposition").toString() : "";
            if (rentstatus.equals("ycz")) {
                String sex = rm.get("sex") != null ? rm.get("sex").toString() : "";
                String professional = rm.get("professional") != null ? rm.get("professional").toString() : "";
                String code = rm.get("code") != null ? rm.get("code").toString() : "";
                ;
                sb.append(!position.equals("") ? (position + "卧" + code + "住着一位") : "");
                sb.append(!sex.equals("") ? sex + "性" : "");
                sb.append(!professional.equals("") ? professional : "");
                sb.append(";");
            } else {
                sb.append(!position.equals("") ? (position + "卧待租 ;") : "卧室待租; ");
                count++;
            }
        }
        if (count == rooms.size()) {
            sb = new StringBuilder();
        }
        URL u = this.getClass().getClassLoader().getResource("cn/ziroom/house/service/package.jasper");
        System.out.println("文件路径：" + u.getPath());
        String reportFile = u.getFile();
        Map parameter = new HashMap();
        parameter.put("roomId", room.getRoomId());
        parameter.put("c1", StringUtils.split(_c1, ","));
        parameter.put("c2", StringUtils.split(_c2, ","));
        parameter.put("c3", StringUtils.split(_c3, ","));
        parameter.put("c4", StringUtils.split(_c4, ","));
        parameter.put("c5", StringUtils.split(_c5, ","));
        parameter.put("c6", StringUtils.split(_c6, ","));
        parameter.put("c7", sb.toString());
        start = System.currentTimeMillis();
        System.out.println("填充报表");
        String filePath = JasperreportsUtils.docx(reportFile, parameter, dataSource.getConnection());
        File _file = new File(filePath);
        FileInputStream fis = new FileInputStream(_file);
        CompressionUtil.compressFile(zos, fis, h.getHouseCode() + r.getRoomName() + ".docx");
        fis.close();
        _file.delete();
        String docFilePath = JasperreportsUtils.doc(reportFile, parameter, dataSource.getConnection());
        File _docfile = new File(docFilePath);
        FileInputStream docfis = new FileInputStream(_docfile);
        CompressionUtil.compressFile(zos, docfis, h.getHouseCode() + r.getRoomName() + ".doc");
        fis.close();
        _docfile.delete();
        byte[] html = JasperreportsUtils.html(reportFile, parameter, dataSource.getConnection());
        InputStream is = new ByteArrayInputStream(html);
        CompressionUtil.compressFile(zos, is, h.getHouseCode() + r.getRoomName() + ".html");
        end = System.currentTimeMillis();
        System.out.println("报表生成时间:" + (end - start) / 1000);
        is.close();
        zos.close();
        fos.close();
        return housePackage;
    }
