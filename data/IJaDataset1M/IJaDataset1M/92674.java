package com.sts.webmeet.build.uneartask;

import org.apache.tools.ant.Task;
import org.apache.tools.ant.taskdefs.Expand;
import org.apache.tools.ant.types.FileSet;
import org.apache.tools.ant.types.ZipFileSet;
import org.apache.tools.ant.DirectoryScanner;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.util.FileUtils;
import java.io.*;
import java.util.Vector;
import java.util.Date;

public class UnEarTask extends Expand {

    private File dest;

    private File source;

    public void setDest(File d) {
        super.setDest(d);
        this.dest = d;
    }

    /**
     * Set the path to zip-file.
     *
     * @param s Path to zip-file.
     */
    public void setSrc(File s) {
        super.setSrc(s);
        this.source = s;
    }

    public void execute() throws BuildException {
        try {
            if (source == null) {
                throw new BuildException("src attribute must be " + "specified");
            }
            if (dest == null) {
                throw new BuildException("Dest attribute must be specified");
            }
            if (dest.exists() && !dest.isDirectory()) {
                throw new BuildException("Dest must be a directory.", getLocation());
            }
            if (source != null) {
                if (source.isDirectory()) {
                    throw new BuildException("Src must not be a directory." + " Use nested filesets instead.", getLocation());
                } else {
                    File fileEarDir = new File(dest, source.getName() + File.separator);
                    fileEarDir.mkdir();
                    super.setDest(fileEarDir);
                    super.execute();
                    expandSubArchives(fileEarDir);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new BuildException("Error unearing: " + e);
        } finally {
        }
    }

    private void expandSubArchives(File fileDir) throws Exception {
        File[] aFileArchives = fileDir.listFiles(new FilenameFilter() {

            public boolean accept(File dir, String name) {
                return name.endsWith(".jar") || name.endsWith(".war");
            }
        });
        for (int i = 0; i < aFileArchives.length; i++) {
            expandSubArchive(aFileArchives[i]);
        }
    }

    private static final String TEMP_SUFFIX = "tmpzzzzz";

    public void expandSubArchive(File archive) throws Exception {
        File fileTempDir = new File(archive.getParentFile(), archive.getName() + TEMP_SUFFIX + File.separator);
        fileTempDir.mkdir();
        super.setSrc(archive);
        super.setDest(fileTempDir);
        super.execute();
        archive.delete();
        fileTempDir.renameTo(archive);
    }

    public static void main(String[] args) throws Exception {
        UnEarTask unEar = new UnEarTask();
        unEar.setSrc(new File(args[0]));
        unEar.setDest(new File(args[1]));
        unEar.execute();
    }
}
