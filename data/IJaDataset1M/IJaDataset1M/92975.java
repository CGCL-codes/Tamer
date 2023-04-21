package org.cyberaide.ssh;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Vector;
import org.apache.log4j.Logger;
import org.cyberaide.util.Path;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;
import com.jcraft.jsch.ChannelSftp.LsEntry;

public class SSH implements Runnable {

    Logger logger = Logger.getLogger(this.getClass());

    public SSH(Session session, InputStream inputStream, OutputStream outputStream, OutputStream errorStream) {
        super();
        this.session = session;
        this.inputStream = inputStream;
        this.outputStream = outputStream;
        this.errorStream = errorStream;
    }

    InputStream inputStream = System.in;

    OutputStream errorStream = System.err;

    OutputStream outputStream = System.out;

    private boolean disconnect = false;

    private Session session;

    private Thread cmdExecution;

    public void kill() {
        disconnect = true;
    }

    public int execute(String command) {
        disconnect = false;
        try {
            logger.debug("executing " + command);
            if (!session.isConnected()) session.connect();
            ChannelExec channel = (ChannelExec) session.openChannel("exec");
            channel.setCommand(command);
            channel.setInputStream(inputStream, true);
            channel.setOutputStream(outputStream, true);
            channel.setErrStream(errorStream, true);
            channel.connect();
            while (!channel.isClosed() && !disconnect) {
                Thread.sleep(500);
            }
            channel.disconnect();
        } catch (Exception e) {
            try {
                errorStream.write(e.toString().getBytes());
            } catch (IOException e1) {
                logger.error(e1.getMessage());
            }
            return -1;
        }
        disconnect = false;
        return 0;
    }

    public void run() {
    }

    private void putAllFiles(ChannelSftp channel, String sourceDir, String destinationDir) {
        File src = new File(sourceDir);
        if (src.isDirectory()) {
            try {
                logger.debug("Created " + destinationDir);
                channel.cd(destinationDir);
                String[] files = src.list();
                for (int i = 0; i < files.length; i++) {
                    try {
                        File f = new File(sourceDir + Path.fs + files[i]);
                        logger.debug("put " + f);
                        if (f.isFile()) {
                            channel.put(f.getAbsolutePath(), f.getName());
                        } else if (f.isDirectory()) {
                            channel.mkdir(f.getName());
                            putAllFiles(channel, f.getAbsolutePath(), f.getName());
                            channel.cd("..");
                        }
                    } catch (SftpException e) {
                        logger.error("Unable to put directory " + destinationDir, e);
                    }
                }
            } catch (SftpException e) {
                logger.error("Unable to put directory " + destinationDir, e);
            }
        } else {
            try {
                channel.put(src.getAbsolutePath(), destinationDir + src.getName());
            } catch (SftpException e) {
                logger.error("Unable to put file " + sourceDir);
            }
        }
    }

    public void putFile(InputStream istream, String destinationFilename) {
        try {
            if (!session.isConnected()) session.connect();
            ChannelSftp channel = (ChannelSftp) session.openChannel("sftp");
            channel.connect();
            channel.put(istream, destinationFilename);
            channel.quit();
        } catch (JSchException e) {
            logger.error("Error opening session with " + session.getHost() + " using " + session.getUserName(), e);
        } catch (SftpException e) {
            logger.error("Error writing inputstream to " + destinationFilename, e);
        }
    }

    public void putFile(String sourceFilename, String destinationFilename) {
        try {
            String path = Path.getConfDirBase(sourceFilename);
            if (path == null) {
                logger.error("File not found:" + sourceFilename);
                return;
            }
            if (!session.isConnected()) session.connect();
            ChannelSftp channel = (ChannelSftp) session.openChannel("sftp");
            channel.connect();
            putAllFiles(channel, path + sourceFilename, destinationFilename);
            channel.quit();
        } catch (JSchException e) {
            logger.error(e);
        }
    }

    public void getFile(String sourceFilename, String destinationDir) {
        try {
            if (!session.isConnected()) session.connect();
            ChannelSftp channel = (ChannelSftp) session.openChannel("sftp");
            channel.connect();
            getAllFiles(channel, sourceFilename, destinationDir);
            channel.quit();
        } catch (SftpException e) {
            logger.error(e);
        } catch (JSchException e) {
            logger.error(e);
        }
    }

    private static void getAllFiles(ChannelSftp channel, String sourceFilename, String destinationDir) throws SftpException {
        File dest = new File(destinationDir);
        if (!dest.exists()) dest.mkdirs();
        Vector<ChannelSftp.LsEntry> lst = channel.ls(sourceFilename);
        if (lst.get(0).getFilename().equalsIgnoreCase(".")) channel.cd(sourceFilename);
        for (LsEntry f : lst) {
            if (f.getFilename().equalsIgnoreCase(".") || f.getFilename().equalsIgnoreCase("..")) continue;
            if (f.getAttrs().isDir()) {
                channel.cd(f.getFilename());
                getAllFiles(channel, ".", destinationDir + Path.fs + f.getFilename());
                channel.cd("..");
            } else if (f.getAttrs().isLink()) ; else channel.get(lst.get(0).getFilename().equalsIgnoreCase(".") ? f.getFilename() : sourceFilename, Path.generic(destinationDir + Path.fs + f.getFilename()));
        }
    }

    public static void main(String args[]) {
    }
}
