    public String saveGood() throws IOException {
        Type type = classManager.get(goodsInfo.getType().getId());
        String path = "images/ftp/" + GB2Alpha.String2Alpha(type.getClassName());
        String targetDirectory = ServletActionContext.getServletContext().getRealPath(path);
        String targetFileName = generateFileName(fileName);
        File target = new File(targetDirectory, targetFileName);
        FileUtils.copyFile(file, target);
        goodsInfo.setGoodsUrl(path + "/" + targetFileName);
        goodsInfo.setAddDate(new Date());
        goodsInfoManager.save(goodsInfo);
        allClass = classManager.getAll();
        return SUCCESS;
    }
