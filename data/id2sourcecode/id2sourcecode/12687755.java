    @RequestMapping("/item/o_save.do")
    public String save(CmsModelItem bean, Integer modelId, Boolean isChannel, HttpServletRequest request, ModelMap model) {
        WebErrors errors = validateSave(bean, modelId, request);
        if (errors.hasErrors()) {
            return errors.showErrorPage(model);
        }
        bean = manager.save(bean, modelId);
        log.info("update CmsModelItem id={}.", bean.getId());
        model.addAttribute("modelId", bean.getModel().getId());
        model.addAttribute("isChannel", bean.getChannel());
        return "redirect:v_list.do";
    }
