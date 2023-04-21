    @RequestMapping(value = "add.htm", method = RequestMethod.POST)
    public String saveUser(@ModelAttribute("systemUser") SystemUser systemUser, BindingResult result, SystemAgent systemAgent, ModelMap model) throws Exception {
        SystemUser existsUser = systemUserManager.getSystemUserByAccount(systemUser.getLoginName());
        if (existsUser != null) {
            model.addAttribute("loginNameError", "��ǰ�û��Ѵ���!");
            return "/user/course/add";
        }
        systemUser.setPassword(passwordValidator.digest(systemUser.getPassword(), 1));
        UserProfilesDO profilesDO = userProfilesManager.selectUserProfilesByUserId(getLoginUser().getId());
        if (profilesDO != null) {
            UserProfilesDO newProfilesDO = new UserProfilesDO();
            newProfilesDO.setSchoolId(profilesDO.getSchoolId());
            systemUser.setUserProfiles(newProfilesDO);
        }
        Long id = systemUserManager.addCourseManager(systemUser);
        if (null == id) {
            model.put("result", "������Ŀ������ʧ�ܣ�");
            return "/error";
        }
        return "redirect:/user/course/list.htm";
    }
