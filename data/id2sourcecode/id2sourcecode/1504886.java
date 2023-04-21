        public String getItemAsXML(int index) {
            Node node = null;
            if ("RDF".equals(this.type)) {
                node = getRDFNodeAt(index, null);
            } else {
                node = this.feed.selectSingleNode(getChannelPrefix() + "item[" + (index + 1) + "]");
            }
            if (node != null) {
                return node.asXML();
            }
            return null;
        }
