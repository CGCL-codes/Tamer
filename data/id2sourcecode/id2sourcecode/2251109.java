        @Override
        protected void encodeAllChildren(FacesContext context, UIComponent component) throws IOException {
            ResponseWriter rw = context.getResponseWriter();
            RenderingContext rc = RenderingContext.getCurrentInstance();
            FacesBean bean = getFacesBean(component);
            ShuttleInfo shuttleInfo = _getShuttleInfo(rc);
            ContainerInfo containerInfo = _getContainerInfo(rc, _isLeading);
            rw.startElement("table", null);
            OutputUtils.renderLayoutTableAttributes(context, rc, null, null);
            if (_isLeading) {
                UIComponent filter = getFacet(component, CoreSelectManyShuttle.FILTER_FACET);
                if (filter != null) {
                    _startRow(context, 3);
                    encodeChild(context, filter);
                    _endRow(context, rc, _DEFAULT_FILTER_HEIGHT);
                }
            }
            _startRow(context, 1);
            delegateRenderer(context, rc, component, bean, _list);
            if (!_isLeading && isReorderable()) {
                rw.endElement("td");
                renderReorderButtons(context, rc, bean, containerInfo.id);
                rw.startElement("td", null);
            }
            boolean hasLeadingDesc = getLeadingDescShown(bean);
            boolean hasTrailingDesc = getTrailingDescShown(bean);
            boolean hasDescArea = hasLeadingDesc || hasTrailingDesc;
            _endRow(context, rc, 0);
            if (hasDescArea) {
                _startRow(context, 1);
                _endRow(context, rc, 8);
                _startRow(context, 3);
                rw.startElement("span", null);
                renderStyleClass(context, rc, SkinSelectors.INSTRUCTION_TEXT_STYLE_CLASS);
                String label = rc.getTranslatedString(_SELECT_MANY_DESCRIPTION_LABEL_KEY);
                rw.writeText(label, null);
                rw.endElement("span");
                rw.startElement("div", null);
                rw.endElement("div");
                rw.startElement("textarea", null);
                rw.writeAttribute("rows", "2", null);
                String textareaId = containerInfo.id + _DESC_FIELD_COMPLETE;
                rw.writeAttribute("id", textareaId, null);
                rw.writeAttribute("name", textareaId, null);
                rw.writeAttribute("readonly", Boolean.TRUE, null);
                rw.writeAttribute("cols", shuttleInfo.descWidth, null);
                rw.writeAttribute("wrap", "soft", null);
                rw.endElement("textarea");
                HiddenLabelUtils.outputHiddenLabelIfNeeded(context, rc, textareaId, label, component);
                _endRow(context, rc, _DEFAULT_DESC_AREA_HEIGHT);
            }
            UIComponent leadingFooter = getFacet(component, CoreSelectManyShuttle.LEADING_FOOTER_FACET);
            UIComponent trailingFooter = getFacet(component, CoreSelectManyShuttle.TRAILING_FOOTER_FACET);
            if ((leadingFooter != null) && (trailingFooter != null)) {
                _startRow(context, 1);
                _endRow(context, rc, 8);
                _startRow(context, 3);
                UIComponent footer = _isLeading ? leadingFooter : trailingFooter;
                if (footer != null) encodeChild(context, footer);
                _endRow(context, rc, _DEFAULT_FOOTER_HEIGHT);
            }
            rw.endElement("table");
        }
