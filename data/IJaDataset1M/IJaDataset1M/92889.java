package org.geometerplus.fbreader.fbreader;

import java.util.List;
import org.geometerplus.zlibrary.core.filesystem.ZLFile;
import org.geometerplus.fbreader.Paths;

public abstract class WallpapersUtil {

    public static List<ZLFile> predefinedWallpaperFiles() {
        return ZLFile.createFileByPath("wallpapers").children();
    }

    public static List<ZLFile> externalWallpaperFiles() {
        return ZLFile.createFileByPath(Paths.WallpapersDirectoryOption().getValue()).children();
    }
}
