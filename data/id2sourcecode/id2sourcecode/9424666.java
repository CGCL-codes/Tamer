    private void publishObject(RepositorySession session, UniquePK node, SimpleDeployer deployer, String root, String repositoryGroupName, ServletOutputStream out) throws IOException {
        if (node.isDirectory()) {
            UniquePK[] children = node.listChildren();
            for (int xy = 0; xy < children.length; xy++) {
                publishObject(session, children[xy], deployer, root, repositoryGroupName, out);
            }
        } else {
            try {
                String name = file.getName();
                if (!deployRoot.endsWith("/")) {
                    deployRoot += "/";
                }
                deployer.copy(file.getPath(), deployRoot + name);
                print(out, "publishing ... ", false);
                print(out, "successful", GREEN, true);
            } catch (Exception ex) {
                ex.printStackTrace();
                print(out, "failed", RED, true);
                print(out, "Reason: Content is null");
            }
        }
    }
