    @RequestMapping(value = "edit.htm", method = RequestMethod.POST)
    public String doEditUser(@ModelAttribute("systemUser") SystemUser systemUser, BindingResult result, @ModelAttribute("school") SchoolsDO school, BindingResult schoolResult, Model model) {
        editSchoolUserValidator.validate(systemUser, result);
        if (result.hasErrors()) {
            return "user/school/edit";
        }
        SystemUser existsUser = systemUserManager.getSystemUserById(systemUser.getId());
        if (!existsUser.getLoginName().equalsIgnoreCase(systemUser.getLoginName())) {
            existsUser = systemUserManager.getSystemUserByAccount(systemUser.getLoginName());
            if (existsUser != null) {
                model.addAttribute("loginNameError", "��ǰ�û��Ѵ���!");
                return "user/school/edit";
            }
        }
        SchoolsDO existsSchool = schoolsManager.selectSchoolsById(school.getSchoolId());
        if (!existsSchool.getName().equals(systemUser.getRealName())) {
            existsSchool = schoolsManager.getSchoolByName(systemUser.getRealName());
            if (existsSchool != null) {
                model.addAttribute("realNameError", "ѧУ�Ѵ���!");
                return "user/school/edit";
            }
        }
        systemUser.setPassword(passwordValidator.digest(systemUser.getPassword(), 1));
        boolean isSuccess = systemUserManager.editSchoolUser(systemUser, school);
        model.addAttribute("url", "/user/school/list.htm");
        if (!isSuccess) {
            return "error";
        }
        return "success";
    }
