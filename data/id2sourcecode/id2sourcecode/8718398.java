    @RequestMapping(value = "add.htm", method = RequestMethod.POST)
    public String doAddUser(@ModelAttribute("systemUser") SystemUser systemUser, BindingResult result, @ModelAttribute("userProfile") UserProfilesDO userProfile, BindingResult userProfileResult, @RequestParam(value = "isEnableUser", required = false) String isEnableUser, Model model) {
        addExpertUserValidator.validate(systemUser, result);
        if (result.hasErrors()) {
            model.addAttribute("genderList", EnumGender.values());
            return "user/expert/add";
        }
        addExpertUserExtValidator.validate(userProfile, userProfileResult);
        if (userProfileResult.hasErrors()) {
            model.addAttribute("genderList", EnumGender.values());
            return "user/expert/add";
        }
        systemUser.setUserProfiles(userProfile);
        if (EnumUserStatus.isEnable(isEnableUser)) {
            systemUser.setUserStatus(EnumUserStatus.ENABLE.getValue());
        }
        systemUser.setPassword(passwordValidator.digest("123456", 1));
        Long userId = systemUserManager.addExpert(systemUser);
        model.addAttribute("url", "/user/expert/list.htm");
        if (userId == null || userId < 1) {
            return "error";
        }
        return "success";
    }
