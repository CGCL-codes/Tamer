package org.intellij.vcs.mks.realtime;

import com.intellij.openapi.diagnostic.Logger;
import org.intellij.vcs.mks.MksCLIConfiguration;
import java.io.*;

/**
 * Asbtract clas for commands ran with the --persist flag that keep running until destroyed. <br/>
 * <p/>
 * Provides {@link #start()}, {@link #stop()}, {@link #isAlive()}  features.
 *
 * @author Thibaut Fagart
 */
public abstract class AbstractMKSSynchronizer implements LongRunningTask {

    protected final Logger LOGGER = Logger.getInstance(getClass().getName());

    private final MksCLIConfiguration mksCLIConfiguration;

    private String command;

    private String[] args;

    private Process process;

    private boolean isAlive = false;

    private volatile boolean stop = false;

    /**
     * the thread from which the command is actually launched
     */
    private Thread runnerThread;

    public AbstractMKSSynchronizer(String command, MksCLIConfiguration mksCLIConfiguration, String... args) {
        this.command = command;
        this.mksCLIConfiguration = mksCLIConfiguration;
        if (args.length > 0) {
            this.args = new String[args.length + 1];
            System.arraycopy(args, 0, this.args, 0, args.length);
            this.args[args.length] = "--persist";
        } else {
            this.args = new String[] { "--persist" };
        }
    }

    /**
     * Starts the command in a new thread
     */
    public void start() {
        runnerThread = new Thread(new Runnable() {

            public void run() {
                do {
                    LOGGER.info("started");
                    executeCommand();
                    if (!stop) {
                        LOGGER.warn("synchronizer terminated unexpectedly, restarting");
                    }
                } while (!stop);
            }
        }, "MksSynchronizer(" + getClass().getName() + "");
        runnerThread.start();
        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {

            public void run() {
                stop();
            }
        }));
    }

    /**
     * stops the running command if it has been started, does nothing otherwise
     */
    public void stop() {
        if (process != null) {
            LOGGER.info("stopping");
            stop = true;
            process.destroy();
        }
    }

    protected void executeCommand() {
        String[] processArgs = new String[args.length + 2];
        processArgs[0] = "si";
        processArgs[1] = command;
        System.arraycopy(args, 0, processArgs, 2, args.length);
        ProcessBuilder builder = new ProcessBuilder(processArgs);
        builder.redirectErrorStream(true);
        isAlive = true;
        try {
            process = builder.start();
        } catch (IOException e) {
            LOGGER.error("unable to start MKS persistent synchronizer", e);
            return;
        }
        new Thread(new Runnable() {

            public void run() {
                try {
                    process.waitFor();
                    isAlive = false;
                } catch (InterruptedException e) {
                    LOGGER.warn("interrupted while waiting for MKS persistent synchronizer process to terminate");
                    try {
                        process.exitValue();
                    } catch (IllegalThreadStateException e1) {
                        LOGGER.warn("terminating process as we were interrupted before it completed");
                        process.destroy();
                    }
                    Thread.currentThread().interrupt();
                }
            }
        }, "MksSynchronizer (" + getClass().getName() + ") death notifier").start();
        InputStream is = process.getInputStream();
        InputStreamReader streamReader;
        try {
            streamReader = new InputStreamReader(is, mksCLIConfiguration.getMksSiEncoding(command));
        } catch (UnsupportedEncodingException e) {
            LOGGER.warn("unsupported encoding specified for reading MKS process output", e);
            stop();
            return;
        }
        BufferedReader reader = new BufferedReader(streamReader);
        try {
            String line;
            try {
                while ((line = reader.readLine()) != null) {
                    handleLine(line);
                }
            } catch (IOException e) {
                LOGGER.error("error reading process output, stopping synchronizer", e);
                stop();
            }
        } finally {
            try {
                reader.close();
            } catch (IOException e) {
                LOGGER.warn("error closing MKS process reader stream", e);
            }
            LOGGER.info("stopped");
        }
    }

    protected abstract void handleLine(String line);

    protected boolean shoudIgnore(String line) {
        return line.startsWith("Reconnecting") || line.startsWith("Connecting");
    }

    public boolean isAlive() {
        return isAlive;
    }

    public void restart() {
        if (isAlive()) {
            stop();
            try {
                runnerThread.join(1000);
                if (runnerThread.isAlive()) {
                    LOGGER.info("command did not stop after 1s");
                }
            } catch (InterruptedException e) {
                LOGGER.debug("interrupted");
                Thread.currentThread().interrupt();
            }
            stop = false;
            start();
        }
    }
}
