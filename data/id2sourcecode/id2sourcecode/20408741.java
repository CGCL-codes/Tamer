    @Override
    protected String generatePortMappingCode() throws KETLThreadException {
        StringBuilder sb = new StringBuilder();
        sb.append(this.getRecordExecuteMethodHeader() + "\n");
        if (this.mOutPorts != null) {
            sb.append("switch(pOutPath){\n");
            for (Map.Entry<String, Integer> entry : this.channelMap.entrySet()) {
                String channel = (String) entry.getKey();
                sb.append("case " + (entry.getValue()) + ": {\n");
                for (int i = 0; i < this.mOutPorts.length; i++) {
                    if (this.mOutPorts[i].getChannel().equals(channel)) {
                        sb.append(this.mOutPorts[i].generateCode(i));
                        sb.append(";\n");
                    }
                }
                sb.append("} break;\n");
            }
            sb.append("}\n");
        }
        sb.append(this.getRecordExecuteMethodFooter() + "\n");
        return sb.toString();
    }
