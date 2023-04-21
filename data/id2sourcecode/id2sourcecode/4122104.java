    private void outputNfsV2(String header, Object nfs) {
        println(header + ":");
        printnfs(nfs, new String[] { "null", "getattr", "setattr", "root", "lookup", "readlink" });
        printnfs(nfs, new String[] { "read", "writecache", "write", "create", "remove", "rename" });
        printnfs(nfs, new String[] { "link", "symlink", "mkdir", "rmdir", "readdir", "fsstat" });
        println("");
        flush();
    }
