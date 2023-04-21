    @RequestMapping(value = "add.htm", method = RequestMethod.POST)
    public String doAddUser(@ModelAttribute("systemUser") SystemUser systemUser, BindingResult result, @ModelAttribute("school") SchoolsDO school, @RequestParam(value = "isEnableUser", required = false) String isEnableUser, Model model) {
        addSchoolUserValidator.validate(systemUser, result);
        if (result.hasErrors()) {
            return "user/school/add";
        }
        SystemUser existsUser = systemUserManager.getSystemUserByAccount(systemUser.getLoginName());
        if (existsUser != null) {
            model.addAttribute("loginNameError", "��ǰ�û��Ѵ���!");
            return "user/school/add";
        }
        SchoolsDO existsSchool = schoolsManager.getSchoolByName(systemUser.getRealName());
        if (existsSchool != null) {
            model.addAttribute("realNameError", "ѧУ�Ѵ���!");
            return "user/school/add";
        }
        if (EnumUserStatus.isEnable(isEnableUser)) {
            systemUser.setUserStatus(EnumUserStatus.ENABLE.getValue());
        }
        systemUser.setPassword(passwordValidator.digest(systemUser.getPassword(), 1));
        Long userId = systemUserManager.addSchoolUser(systemUser, school);
        model.addAttribute("url", "/user/school/list.htm");
        if (userId == null || userId < 1) {
            return "error";
        }
        return "success";
    }
