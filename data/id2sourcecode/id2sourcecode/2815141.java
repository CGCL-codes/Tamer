    public Content saveContent(String title, String txt, Integer acquId, AcquisitionResultType resultType, CmsAcquisitionTemp temp, CmsAcquisitionHistory history) {
        CmsAcquisition acqu = findById(acquId);
        Content c = new Content();
        c.setSite(acqu.getSite());
        ContentExt cext = new ContentExt();
        ContentTxt ctxt = new ContentTxt();
        cext.setTitle(title);
        ctxt.setTxt(txt);
        Content content = contentMng.save(c, cext, ctxt, null, null, null, null, null, null, null, null, null, acqu.getChannel().getId(), acqu.getType().getId(), false, acqu.getUser(), false);
        history.setTitle(title);
        history.setContent(content);
        history.setDescription(resultType.name());
        temp.setTitle(title);
        temp.setDescription(resultType.name());
        return content;
    }
