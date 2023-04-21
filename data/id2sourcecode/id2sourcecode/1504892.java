        private String getItemNodeValue(int index, String nodeName) {
            String value = null;
            if ("RDF".equals(this.type)) {
                value = getRDFNodeAt(index, nodeName).getText();
            } else {
                value = this.feed.selectSingleNode(getChannelPrefix() + "item[" + (index + 1) + "]/" + nodeName).getText();
            }
            return value;
        }
