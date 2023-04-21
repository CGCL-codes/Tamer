        public int size() {
            if ("RDF".equals(this.type)) {
                int size = 0;
                Iterator it = feed.getRootElement().elements().iterator();
                while (it.hasNext()) {
                    String qName = ((Element) it.next()).getQualifiedName();
                    if (qName.equals("item")) {
                        size++;
                    }
                }
                return size;
            }
            return this.feed.selectNodes(getChannelPrefix() + "item").size();
        }
