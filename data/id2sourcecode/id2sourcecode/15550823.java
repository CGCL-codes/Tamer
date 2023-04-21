    private void copy(Node srcParent, Node destParent, Tree src, List<Node> result, Map<String, String> parentVariables) throws CopyException {
        String name;
        Node dest;
        List<Map<String, String>> childVariablesList;
        boolean isDir;
        name = src.node.getName();
        dest = null;
        try {
            if (callPrefix != 0 && name.length() > 0 && name.charAt(0) == callPrefix) {
                result.add(call(name, src.node, destParent, parentVariables));
            } else {
                childVariablesList = new ArrayList<Map<String, String>>();
                name = splitContext(name, parentVariables, childVariablesList);
                isDir = src.node.isDirectory();
                for (Map<String, String> childVariables : childVariablesList) {
                    dest = destParent.join(path == null ? name : path.apply(name, childVariables));
                    if (isDir) {
                        dest.mkdirsOpt();
                    } else {
                        dest.getParent().mkdirsOpt();
                        if (content != null) {
                            dest.writeString(content.apply(src.node.readString(), childVariables));
                        } else {
                            src.node.copyFile(dest);
                        }
                    }
                    if (modes) {
                        dest.setMode(src.node.getMode());
                    }
                    result.add(dest);
                    for (Tree child : src.children) {
                        copy(src.node, dest, child, result, childVariables);
                    }
                }
            }
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            if (dest == null) {
                dest = destParent.join(name);
            }
            throw new CopyException(src.node, dest, e);
        }
    }
