    public String addCategory() throws Exception {
        try {
            List<Category> categories = categoryService.findByName(newCategoryName);
            if (categories.size() != 0) {
                this.addFieldError("newCategoryName", "该分类已存在");
                return INPUT;
            }
            String targetFileName;
            String targetDirectory = ServletActionContext.getServletContext().getRealPath("/" + savePath);
            if (upload != null) {
                targetFileName = generateFileName(uploadFileName);
                File target = new File(targetDirectory, targetFileName);
                FileUtils.copyFile(upload, target);
            } else {
                targetFileName = "default.png";
            }
            Category category = new Category();
            category.setName(newCategoryName);
            category.setSummary(newCategoryDescription);
            category.setPriority(0);
            category.setIcon(savePath + "/" + targetFileName);
            categoryService.save(category);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return SUCCESS;
    }
