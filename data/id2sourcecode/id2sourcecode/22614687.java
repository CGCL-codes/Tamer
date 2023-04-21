	public static Permission readFolderPermission(final MetaInfo folder) {
		if (folder.isFSRoot())
			return Permission.DEFAULT;
			
		if (!folder.isFolder()) {
			MLogger.warning("fs", "Folder expected: %s", folder);
			//MLogger.trace();

			return Permission.DEFAULT;
		}
		
		Config config = folder.getConfig();

		if (config.read("permission.default", DEFAULT_DEFAULT))
			return Permission.DEFAULT;

		boolean read = config.read("permission.read", DEFAULT_READ);
		boolean write = config.read("permission.write", DEFAULT_WRITE);

		if (read && write)
			return Permission.READ_WRITE;

		if (read)
			return Permission.READ;
		
		return Permission.FORBIDDEN;
	}
