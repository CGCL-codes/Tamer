package moe.rename;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.regex.Pattern;
import moe.entity.Post;
import moe.util.UriMappedUtil;

public class RenameMD5ToTags extends BaseRename {

    public RenameMD5ToTags() {
        super(Pattern.compile("^(?:moe|yande\\.re) \\d+\\.(?:[0-9]|[a-f])+\\.\\w+$", Pattern.CASE_INSENSITIVE));
    }

    @Override
    protected long getId(String fileName) {
        String id = fileName.split(" ")[1];
        id = id.substring(0, id.indexOf('.'));
        return Long.valueOf(id);
    }

    @Override
    public String getNewFileName(String srcFileName, Post post) {
        String fileName = post.fileUrl.substring(post.fileUrl.lastIndexOf('/') + 1);
        fileName = UriMappedUtil.yandeToMoe(fileName);
        String srcExt = srcFileName.substring(srcFileName.lastIndexOf('.'));
        String targetExt = fileName.substring(fileName.lastIndexOf('.'));
        if (!srcExt.equalsIgnoreCase(targetExt)) {
            fileName = fileName.substring(0, fileName.lastIndexOf('.')) + srcExt;
        }
        try {
            fileName = URLDecoder.decode(fileName, "UTF-8");
        } catch (UnsupportedEncodingException e) {
        }
        if (isWindows()) {
            for (String key : escapeMap.keySet()) {
                fileName = fileName.replaceAll(key, escapeMap.get(key));
            }
        } else {
            fileName = fileName.replaceAll("/", escapeMap.get("/"));
        }
        return fileName;
    }
}
