    public void digest(Element root) {
        super.digest(root);
        setRequestwithinms(Long.parseLong(root.getChildTextTrim("requestwithinms")));
    }
