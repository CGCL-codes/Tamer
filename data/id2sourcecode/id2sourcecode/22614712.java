	private static void writePermission(final MetaInfo folder, final Permission permission) {
		if (folder.isFSRoot())
			return;

		if (!folder.isFolder()) {
			MLogger.warning("fs", "Folder expected: %s", folder);
			//MLogger.trace();
			
			return;
		}
		
		Config config = folder.getConfig();
		switch (permission) {
			case DEFAULT:
				config.write("permission.default", true);
				config.removeBoolean("permission.read");
				config.removeBoolean("permission.write");
				break;
			case FORBIDDEN:
				config.write("permission.default", false);
				config.write("permission.read", false);
				config.write("permission.write", false);
				break;
			case READ:
				config.write("permission.default", false);
				config.write("permission.read", true);
				config.write("permission.write", false);
				break;
			case READ_WRITE:
				config.write("permission.default", false);
				config.write("permission.read", true);
				config.write("permission.write", true);
				break;
		}
	}
