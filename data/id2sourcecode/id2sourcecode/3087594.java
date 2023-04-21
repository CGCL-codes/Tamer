    @SuppressWarnings("unchecked")
    private static <T> void buildPdfDocument(Document document, Map<String, String> titlebeanNameMap, List<T> beans) throws FrameworkException {
        if (document == null) {
            throw new FrameworkException("pdf文档对象模型为null");
        }
        try {
            document.open();
            List<String> titles = new ArrayList();
            List<String> beanAttrNames = new ArrayList();
            int cnt = 0;
            for (Iterator i = titlebeanNameMap.keySet().iterator(); i.hasNext(); ) {
                String key = (String) i.next();
                String value = titlebeanNameMap.get(key);
                beanAttrNames.add(cnt, key);
                titles.add(cnt, value);
                cnt++;
            }
            PdfPTable table = new PdfPTable(titlebeanNameMap.size());
            for (int i = 0; i < titles.size(); i++) {
                setPdfTitleCellText(table, titles.get(i));
            }
            PropertyDescriptor[] props = getBeanInfo(beans.get(0).getClass()).getPropertyDescriptors();
            for (int i = 0; i < beans.size(); i++) {
                T bean = beans.get(i);
                for (int j = 0; j < beanAttrNames.size(); j++) {
                    String beanAttrName = beanAttrNames.get(j);
                    for (int k = 0; k < props.length; k++) {
                        String propName = props[k].getName();
                        if (propName.equals(beanAttrName)) {
                            Object cellValue = callGetter(bean, props[k]);
                            if (cellValue == null) {
                                cellValue = "";
                            }
                            setPdfDataCellText(table, cellValue.toString());
                        }
                    }
                }
            }
            document.add(table);
        } catch (DocumentException de) {
            throw new FrameworkException("创建pdf文档时产生异常", de);
        } finally {
            document.close();
        }
    }
