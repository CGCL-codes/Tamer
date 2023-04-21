    public List<NCBOSearchBean> search(String term) throws IOException, XMLStreamException {
        List<NCBOSearchBean> items = new ArrayList<NCBOSearchBean>(Math.max(1, getResultCount()));
        StringBuilder builder = new StringBuilder(NCBO.BIOPORTAL_URL);
        builder.append("/search/");
        builder.append(URLEncoder.encode(term, "UTF-8")).append("/?");
        builder.append("isexactmatch=").append(exactmatch ? 1 : 0);
        builder.append("&includeproperties=").append(includeproperties ? 1 : 0);
        if (getPageIndex() != 1) builder.append("&pagenum=" + getPageIndex());
        if (getResultCount() != -1) builder.append("&pagesize=" + getResultCount());
        if (!getOntologyId().isEmpty()) {
            builder.append("&ontologyids=");
            boolean comma = false;
            for (Integer id : getOntologyId()) {
                if (comma) builder.append(",");
                comma = true;
                builder.append(id);
            }
        }
        XMLInputFactory xf = XMLInputFactory.newInstance();
        xf.setProperty(XMLInputFactory.IS_NAMESPACE_AWARE, false);
        xf.setProperty(XMLInputFactory.IS_VALIDATING, false);
        URL url = new URL(builder.toString());
        InputStream in = url.openStream();
        XMLEventReader r = xf.createXMLEventReader(in);
        NCBOSearchBeanImpl bean = null;
        while (r.hasNext()) {
            XMLEvent evt = r.nextEvent();
            if (evt.isStartElement()) {
                String name = evt.asStartElement().getName().getLocalPart();
                if (name.equals("searchBean")) {
                    bean = new NCBOSearchBeanImpl();
                } else if (bean != null) {
                    String content = r.getElementText();
                    if (name.equals("ontologyVersionId")) {
                        bean.ontologyVersionId = Integer.parseInt(content);
                    } else if (name.equals("ontologyId")) {
                        bean.ontologyId = Integer.parseInt(content);
                    } else if (name.equals("ontologyDisplayLabel")) {
                        bean.ontologyLabel = content;
                    } else if (name.equals("recordType")) {
                        bean.recordType = content;
                    } else if (name.equals("conceptId")) {
                        bean.conceptId = content;
                    } else if (name.equals("conceptIdShort")) {
                        bean.conceptIdShort = content;
                    } else if (name.equals("preferredName")) {
                        bean.preferredName = content;
                    } else if (name.equals("contents")) {
                        bean.contents = content;
                    }
                }
            } else if (evt.isEndElement()) {
                String name = evt.asEndElement().getName().getLocalPart();
                if (name.equals("searchBean") && bean != null) {
                    items.add(bean);
                    bean = null;
                }
            }
        }
        r.close();
        return items;
    }
