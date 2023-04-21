    public void setDataInfo() throws LoadDataException {
        if (this.tag instanceof PagerTag) {
            String dataType = this.getData();
            if (statement != null && !statement.equals("")) {
                dataInfo = new DefaultDataInfoImpl();
                try {
                    dataInfo.initial(statement, dbname, getOffset(), getMaxPageItems(), ListMode(), request, this.getSQLParams());
                } catch (SetSQLParamException e) {
                    throw new LoadDataException(e);
                }
                if (!ListMode()) {
                    setItems(dataInfo.getItemCount());
                }
            } else {
                Object dataInfo_temp = request.getAttribute(dataType);
                if (dataInfo_temp instanceof DataInfo) {
                    dataInfo = (DataInfo) dataInfo_temp;
                    if (dataInfo == null) {
                        log.info("����DataInfo�����Ѿ�������ȷ");
                        return;
                    }
                    if (dataInfo instanceof DefaultDataInfoImpl) dataInfo.initial(null, null, getOffset(), getMaxPageItems(), this.ListMode(), request); else {
                        dataInfo.initial(getSortKey(), this.desc, getOffset(), getMaxPageItems(), this.ListMode(), request);
                    }
                } else if (PagerContext.isPagerMehtod(request)) {
                    if (dataInfo_temp != null && dataInfo_temp instanceof ListInfo) this.dataInfo = new ListInfoDataInfoImpl((ListInfo) dataInfo_temp);
                }
                if (dataInfo != null && !ListMode()) {
                    setItems(dataInfo.getItemCount());
                }
            }
        } else if (this.tag instanceof PagerDataSet) {
            if (getColName() != null) {
                try {
                    load(COLUMN_SCOPE);
                } catch (LoadDataException e) {
                    log.info(e.getMessage());
                    throw e;
                }
            } else if (statement != null && !statement.equals("")) {
                try {
                    load(DB_SCOPE);
                } catch (LoadDataException e) {
                    log.info(e.getMessage());
                    throw e;
                }
            } else if (requestKey != null) {
                try {
                    load(REQUEST_SCOPE);
                } catch (LoadDataException e) {
                    log.info(e.getMessage());
                    throw e;
                }
            } else if (sessionKey != null) try {
                load(SESSION_SCOPE);
            } catch (LoadDataException e1) {
                log.info(e1.getMessage());
                throw e1;
            } else if (pageContextKey != null) {
                try {
                    load(PAGECONTEXT_SCOPE);
                } catch (LoadDataException e2) {
                    log.info(e2.getMessage());
                    throw e2;
                }
            } else if (this.tag instanceof CMSListTag) {
                CMSListTag cmsListTag = (CMSListTag) tag;
                CMSBaseListData dataInfo = CMSTagUtil.getCMSBaseListData(cmsListTag.getDatatype());
                dataInfo.setOutlineInfo(cmsListTag.getSite(), cmsListTag.getChannel(), cmsListTag.getCount());
                if (cmsListTag.getDocumentid() != null) dataInfo.setDocumentid(cmsListTag.getDocumentid());
                dataInfo.initial(getSortKey(), this.desc, getOffset(), getMaxPageItems(), this.ListMode(), request);
                this.dataInfo = dataInfo;
                if (!ListMode()) {
                    setItems(dataInfo.getItemCount());
                }
            } else {
                try {
                    load(CELL_SCOPE);
                } catch (LoadDataException e2) {
                    log.info(e2.getMessage());
                    throw e2;
                }
            }
        }
    }
