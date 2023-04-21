    void setNodes(DomainResponse<Node> response, Collection<Node> nodes) {
        response.setResults(nodes);
        String md5Hash = Registry.md5Digest().digest(nodes.toString().getBytes()).toString();
        response.setMD5Hash(md5Hash);
    }
