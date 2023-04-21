    public int doEndTag() throws JspException {
        DSTempRefManageTag papa = (DSTempRefManageTag) TagSupport.findAncestorWithClass(this, DSTempRefManageTag.class);
        if (papa == null) {
            throw new JspException("DSTemplateRefTag:�����ҵ�����ǩ ces.platform.infoplat.taglib.ds.DSTempRefManageTag");
        }
        Channel channel = papa.getChannel();
        channelPath = papa.getChannelPath();
        Template template = Template.getInstance(templateId);
        if (template == null) {
            log.error("ͨ��templateId" + templateId + "�õ���templateΪnull");
            return SKIP_PAGE;
        }
        StringTokenizer tokens = new StringTokenizer(params, ";");
        StringBuffer newParams = new StringBuffer();
        while (tokens.hasMoreTokens()) {
            if (this.dsTagMap == null) {
                this.dsTagMap = new HashMap(2);
            }
            String newParam = paramToTag(tokens.nextToken());
            if (newParam != null) {
                newParams.append(newParam);
                newParams.append(";");
            }
        }
        if (this.dsTagMap == null || this.dsTagMap.size() == 0) {
            return SKIP_PAGE;
        }
        String content = template.getDefineFileContent();
        if (content != null) {
            String templateRes = template.getResDirPath();
            String channelRes = null;
            try {
                channelRes = channel.getDefineFilePath() + "res_" + template.getAsciiName();
            } catch (Exception ex) {
            }
            try {
                FileOperation.copyDir(templateRes, channelRes);
            } catch (Exception e) {
                log.error("�ƶ����Դ��ԴĿ¼" + templateRes + "��" + channelRes + "ʧ��", e);
            }
            content = replaceAllTags(content, this.dsTagMap);
            String siteAsciiName = null;
            try {
                siteAsciiName = channel.getSiteAsciiName();
            } catch (Exception ex1) {
                log.error("", ex1);
            }
            if (!isStatic) {
                String contentPath = new StringBuffer(ConfigInfo.getInstance().getInfoplatDataDir()).append(File.separator).append("pub").append(File.separator).append(siteAsciiName).append(File.separator).append(channel.getAsciiName()).append(File.separator).append("dynContent_").append(this.dstrId).append(".data").toString();
                try {
                    Function.writeTextFile(content, contentPath, true);
                } catch (Exception e) {
                    log.error("���涯̬���Դʧ��", e);
                }
                content = "<" + DSTR_PREFIX + ":content dstrId=\"" + this.dstrId + "\">";
            }
            JspWriter out = pageContext.getOut();
            try {
                out.print(content.toString());
            } catch (IOException ioexception) {
                log.error("���Դģ������Tag�������: �������content��ݵ�ҳ��");
            }
        }
        if (newParams.toString().trim() != "") {
            HashMap dstrMap = (HashMap) pageContext.getAttribute(DSTempRefManageTag.IP_DSTRMAP_KEYWORD);
            dstrMap.put(this.dstrId, newParams.toString());
        }
        return EVAL_PAGE;
    }
