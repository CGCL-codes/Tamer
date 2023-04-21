package filemanager.vfs.impl;

import filemanager.vfs.ArchiveIfc;
import filemanager.vfs.PermissionIfc;
import filemanager.vfs.ReadableFileIfc;
import java.beans.PropertyChangeListener;
import java.net.URI;
import java.net.URISyntaxException;
import org.apache.commons.vfs2.*;

/**
 *
 * @author sahaqiel
 */
public class GzipArchive implements ReadableFileIfc, ArchiveIfc {

    private FileObject gzipArchive;

    public GzipArchive(String path) {
        try {
            FileSystemManager fsManager = VFS.getManager();
            gzipArchive = fsManager.resolveFile(path);
        } catch (FileSystemException ex) {
            throw new IllegalArgumentException();
        }
    }

    @Override
    public void addFileListener(PropertyChangeListener listener) {
    }

    @Override
    public boolean exists() {
        boolean exists = false;
        try {
            exists = gzipArchive.exists();
        } catch (FileSystemException ex) {
            ex.printStackTrace();
        }
        return exists;
    }

    @Override
    public Object getBackingObject() {
        return gzipArchive;
    }

    @Override
    public long getLastModified() {
        return 0;
    }

    @Override
    public String getMimeType() {
        return "";
    }

    @Override
    public String getName() {
        return gzipArchive.getName().getBaseName();
    }

    @Override
    public ReadableFileIfc getParent() {
        ReadableFileIfc parent = null;
        try {
            parent = new JarArchive(gzipArchive.getParent().getURL().toExternalForm());
        } catch (FileSystemException ex) {
            ex.printStackTrace();
        }
        return parent;
    }

    @Override
    public PermissionIfc getPermissions() {
        return new JavaPermission(this);
    }

    @Override
    public String getPrefix() {
        return "gzip://";
    }

    @Override
    public long getSize() {
        long size = 0;
        try {
            if (gzipArchive.getType().equals(FileType.FILE_OR_FOLDER)) {
                size = gzipArchive.getContent().getSize();
            }
        } catch (FileSystemException ex) {
            ex.printStackTrace();
        }
        return size;
    }

    @Override
    public FileTypes getType() {
        try {
            switch(gzipArchive.getType()) {
                case FILE:
                    return FileTypes.File;
                case FILE_OR_FOLDER:
                    return FileTypes.Archive;
                case FOLDER:
                    return FileTypes.Folder;
                case IMAGINARY:
                    return FileTypes.Imaginary;
            }
        } catch (FileSystemException ex) {
            ex.printStackTrace();
        }
        return FileTypes.Unknown;
    }

    @Override
    public URI getURI() {
        URI result = null;
        try {
            result = gzipArchive.getURL().toURI();
        } catch (URISyntaxException ex) {
            ex.printStackTrace();
        } catch (FileSystemException ex) {
            ex.printStackTrace();
        }
        return result;
    }

    @Override
    public boolean isHidden() {
        boolean hidden = false;
        try {
            hidden = gzipArchive.isHidden();
        } catch (FileSystemException ex) {
            ex.printStackTrace();
        }
        return hidden;
    }

    @Override
    public void removeListeners() {
    }

    @Override
    public int compareTo(ReadableFileIfc o) {
        return 0;
    }

    @Override
    public String[] getExtensions() {
        return new String[] { "gzip", "bzip2", "gz" };
    }

    @Override
    public boolean acceptsAllExtensions() {
        return false;
    }
}
