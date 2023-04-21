package net.sf.contrail.vfs.cow;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.UUID;
import org.apache.commons.vfs.FileObject;
import org.apache.commons.vfs.FileSystemException;
import org.apache.commons.vfs.FileType;
import org.apache.commons.vfs.NameScope;

/**
 * A file stored in a COW file system that contains metadata that describes 
 * a particular revision of a file or folder.
 * Contains...
 * ...a list of the pages included in the revision.
 * ...the length of the file as of the revision
 * ...the last modified date in this revision 
 * 
 * @author Ted Stockwell
 *
 */
public class CowRevision implements Serializable {

    private static final long serialVersionUID = 1L;

    public static final String COWREVISION_NAME = ".cowrev";

    public static final int DEFAULT_PAGE_SIZE_BYTES = 512 * 1024;

    /**
	 * Opens the COW metadata file associated with the given revision.
	 * @param fileObject  The folder that contains the COW metadata file(s).
	 * @throws IOException 
	 */
    public static CowRevision openCowInfo(FileObject hostFolder, CowFileSystem cowFS) throws IOException {
        long revision = cowFS.getRevision();
        CowRevision info = new CowRevision();
        long lastRevision = findClosestRevision(hostFolder, cowFS);
        if (lastRevision < 0) {
            info._fileType = CowFileType.IMAGINARY;
        } else {
            FileObject lastFile = hostFolder.resolveFile(COWREVISION_NAME + "-" + lastRevision);
            ObjectInputStream in = new ObjectInputStream(lastFile.getContent().getInputStream());
            try {
                info = (CowRevision) in.readObject();
            } catch (ClassNotFoundException x) {
                throw new IOException("Error reading " + info._file, x);
            } finally {
                try {
                    in.close();
                } catch (Throwable t) {
                }
            }
        }
        info._file = hostFolder.resolveFile(COWREVISION_NAME + "-" + revision, NameScope.CHILD);
        info._folder = hostFolder;
        return info;
    }

    private static long findClosestRevision(FileObject hostFolder, CowFileSystem cowFS) throws FileSystemException {
        FileObject[] children = hostFolder.getChildren();
        long filerev = -1;
        long revision = cowFS.getRevision();
        for (FileObject child : children) {
            String baseName = child.getName().getBaseName();
            if (!baseName.startsWith(COWREVISION_NAME + "-")) continue;
            long rev = Long.parseLong(baseName.substring(COWREVISION_NAME.length() + 1));
            if (rev <= revision && filerev < rev) filerev = rev;
            if (filerev == revision) break;
        }
        if (filerev < 0) return -1;
        return filerev;
    }

    private CowFileType _fileType = CowFileType.IMAGINARY;

    private HashSet<String> _children = new HashSet<String>();

    private long _fileLength = 0;

    private long _lastModified = System.currentTimeMillis();

    private int _pageSize = DEFAULT_PAGE_SIZE_BYTES;

    private ArrayList<String> _pageNames = new ArrayList<String>();

    private transient FileObject _file;

    private transient FileObject _folder;

    public FileType getFileType() {
        return _fileType.getFileType();
    }

    public String[] listChildren() {
        String[] c = new String[_children.size()];
        _children.toArray(c);
        return c;
    }

    public long getFileLength() {
        return _fileLength;
    }

    public void setFileType(FileType type) throws FileSystemException {
        if (FileType.FILE.equals(type)) {
            _fileType = CowFileType.FILE;
        } else if (FileType.FOLDER.equals(type)) {
            _fileType = CowFileType.FOLDER;
        } else if (FileType.IMAGINARY.equals(type)) {
            _fileType = CowFileType.IMAGINARY;
        } else throw new IllegalArgumentException("File type must be FILE, FOLDER, or IMAGINARY");
    }

    public void truncateFile() {
        _fileLength = 0;
    }

    public long getLastModified() {
        return _lastModified;
    }

    public void setLastModified(long modtime) {
        _lastModified = modtime;
    }

    public void deleteChild(String baseName) {
        _children.remove(baseName);
    }

    /**
	 * @return name of page file in underlying file system
	 * 		if create flag is false then returns null if no page yet created for given position
	 */
    public FileObject getPage(long position, boolean create) throws FileSystemException {
        String pageName = null;
        ;
        int pageIndex = (int) (position / _pageSize);
        if (pageIndex < _pageNames.size()) pageName = _pageNames.get(pageIndex);
        if (pageName == null) {
            if (!create) return null;
            FileObject fileObject = createNewPage();
            while (_pageNames.size() <= pageIndex) _pageNames.add(null);
            _pageNames.set(pageIndex, fileObject.getName().getBaseName());
            return fileObject;
        }
        return _folder.resolveFile(pageName, NameScope.CHILD);
    }

    public int positionToOffset(long position) {
        return (int) (position - ((position / _pageSize) * _pageSize));
    }

    public int getPageSize() {
        return _pageSize;
    }

    public void save() throws IOException {
        ObjectOutputStream out = new ObjectOutputStream(_file.getContent().getOutputStream());
        try {
            out.writeObject(this);
        } finally {
            out.close();
        }
    }

    public void setFileLength(long position) {
        _fileLength = position;
    }

    public void clearAllPages() {
        _pageNames.clear();
    }

    public void setPage(long position, FileObject page) {
        int pageIndex = (int) (position / _pageSize);
        while (_pageNames.size() <= pageIndex) _pageNames.add(null);
        _pageNames.set(pageIndex, page.getName().getBaseName());
    }

    public FileObject createNewPage() throws FileSystemException {
        String pageName = ".cowpage-" + UUID.randomUUID().toString();
        FileObject pageFile = _folder.resolveFile(pageName, NameScope.CHILD);
        pageFile.createFile();
        return pageFile;
    }

    public int positionToPageIndex(long position) {
        return (int) (position / _pageSize);
    }
}
