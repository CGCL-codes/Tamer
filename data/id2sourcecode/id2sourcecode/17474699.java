    public String upload() throws IOException {
        if (!userLogined()) {
            return LOGIN;
        }
        try {
            item = itemService.getBy("id", "CLW0001", null).get(0);
            if (item == null || item.getPrice() == null) {
                msg = "目前无可订商品";
                docs = null;
                order = null;
                return INPUT;
            }
            if (docs == null || docs.size() != 12 || order == null) {
                msg = "订单信息不全, 请重新填写表单";
                docs = null;
                order = null;
                return INPUT;
            }
            String saving_path = URLConstants.get(NamingConstants.FILES_SAVE_PATH) + (new Date().getTime()) + "/";
            ExFileUtils.createFolder(saving_path);
            int i = 0;
            for (File doc : docs) {
                if (doc != null && docsFileName.get(i) != null && !"".equals(docsFileName.get(i))) {
                    String name = saving_path + "f" + generator.nextInt(100) + docsFileName.get(i++);
                    FileUtils.copyFile(doc, new File(name));
                } else {
                    msg = "上传文件少于指定数量, 请重新上传文件";
                    docs = null;
                    order = null;
                    return INPUT;
                }
            }
            String zipName = String.valueOf(new Date().getTime()) + ".zip";
            String zipLocalPath = URLConstants.get(NamingConstants.FILES_SAVE_PATH) + zipName;
            String zipUrlPath = URLConstants.get(NamingConstants.FILES_DOWNLOAD_URL) + zipName;
            ExFileUtils.createZipAnt(saving_path, zipLocalPath);
            order.setId("CLW" + String.valueOf(new Date().getTime()));
            order.setEbUser((EbUser) getSession().getAttribute("user"));
            order.setItem(item);
            order.setPicsPath(zipUrlPath);
            order.setStatus(new Integer(0));
            order.setCreateTime(new Date());
            order.setUpdateTime(new Date());
            orderService.save(order);
            docs = null;
            return SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            msg = "保存上传文件失败, 请重新提交";
            docs = null;
            order = null;
            return INPUT;
        }
    }
