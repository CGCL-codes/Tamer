    public String register() {
        try {
            if (inputvalidate() == false) return INPUT; else {
                System.out.println("uploadFile execute");
                user.setUserimage(servletRequest.getContextPath() + "/head/" + filename);
                String realPath = ServletActionContext.getRequest().getRealPath(root);
                String targetDirectory = realPath;
                targetFileName = filename;
                setDir(targetDirectory + "\\" + targetFileName);
                if (!dir.substring(dir.length() - 3, dir.length()).equals("jpg")) {
                    resultmsg = "���ϴ�jpg���͵�ͼƬ";
                    System.out.println(resultmsg);
                    return INPUT;
                }
                File target = new File(targetDirectory, targetFileName);
                FileUtils.copyFile(imagefile, target);
            }
            user.setUsertype("common");
            manageUserService.insertUser(user);
            return SUCCESS;
        } catch (IOException e) {
            e.printStackTrace();
            return ERROR;
        }
    }
