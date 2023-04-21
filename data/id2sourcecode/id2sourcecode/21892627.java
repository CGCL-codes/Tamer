    public String uploadPhoto() {
        String filePath = ServletActionContext.getServletContext().getRealPath("/");
        Random random = new Random();
        String prefix = Integer.toString(random.nextInt(10000000));
        photoPath = "user\\images\\";
        filePath += photoPath;
        photoPath = photoPath.replace("\\", "/");
        photoPath += prefix + "-" + userImageFileName;
        userImageFileName = prefix + "-" + userImageFileName;
        File fileToCreate = new File(filePath, this.userImageFileName);
        try {
            FileUtils.copyFile(this.userImage, fileToCreate);
            user = userDao.findByUsername((String) session.get("username"));
            user.setPhotoName(filePath + userImageFileName);
            userDao.save(user);
            isUploaded = true;
        } catch (IOException e) {
            addActionError(e.getMessage());
            e.printStackTrace();
            return INPUT;
        }
        return "UPLOADED";
    }
