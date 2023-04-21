    @RequestMapping("/item/o_update.do")
    public String update(CmsModelItem bean, HttpServletRequest request, ModelMap model) {
        WebErrors errors = validateUpdate(bean.getId(), bean, request);
        if (errors.hasErrors()) {
            return errors.showErrorPage(model);
        }
        bean = manager.update(bean);
        log.info("update CmsModelItem id={}.", bean.getId());
        model.addAttribute("modelId", bean.getModel().getId());
        model.addAttribute("isChannel", bean.getChannel());
        return "redirect:v_list.do";
    }
