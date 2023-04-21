package net.sf.dvorcode;

import org.apache.commons.cli.Parser;
import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.ParseException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import javax.swing.UIManager;

/**
 * Main-class for dvorcode transcode utility.
 * @author mike
 * @see Dvorcode
 */
public class Main {

    /** Program Banner string and copyright/warranty */
    static final String EOL = System.getProperty("line.separator");

    static final String RELEASE = "0.4";

    static final String BANNER = "Dvorcode, a Dvorak decoder/encoder, rel " + RELEASE + EOL + "Copyright (C) 2008, 2009 Michael James Lockhart, B.AppComp(HONS), SCJP" + EOL + EOL + "This is free software, licensed to you under the terms of the GNU" + EOL + "General Public Licence, version 3.  See http://www.gnu.org/licenses/" + EOL + EOL + "This program is distributed WITHOUT ANY WARRANTY; without even the" + EOL + "implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE." + EOL;

    /** The name of the program, for printing in messages */
    static final String PROGNAME = "dvorcode";

    /** Exit codes and descriptions for different exit conditions */
    private static enum ExitCode {

        /** Indicates worng options supplied */
        BAD_OPTION(1, "Argument error"), /** Indicates a problem processing input file */
        FILE_INPUT(2, "Problem with input file"), /** Indicates a problem processing output file */
        FILE_OUTPUT(3, "Problem with output file"), /** Indicates a problem with I/O during trans-code */
        XCODE_IO(4, "I/O Problem during trans-code"), /** Indicates a general, non-specific problem */
        GENERAL_ERROR(128, "Unexpected error");

        /** System.exit() code */
        private final int exitCode;

        /** ExitCode description String */
        private final String description;

        /** Initialises the exit code and description for an ExitCode.*/
        ExitCode(int code, String description) {
            this.exitCode = code;
            this.description = description;
        }

        /** @return the exit code for this ExitCode */
        int getCode() {
            return exitCode;
        }

        /** @return this ExitCode's description */
        String getDescription() {
            return description;
        }
    }

    ;

    /** when true, trace output will be printed */
    private static boolean beVerbose = false;

    /**
     * Main entry for Dvorcode program when called as a stand-alone
     * JAR.  This method will parse command-line arguments and act
     * accordingly.
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Options opt = new Options();
        opt.addOption("h", "help", false, "Displays this help message");
        opt.addOption("g", "gui", false, "Use graphics mode (default)");
        opt.addOption("j", "java", false, "Use Java Look&Feel mode");
        opt.addOption("t", "text", false, "Use text mode");
        opt.addOption("f", "file", true, "File to transcode (default is stdin)");
        opt.addOption("o", "output", true, "Output to file (default is stdout)");
        opt.addOption("e", "encode", false, "Encode the input");
        opt.addOption("d", "decode", false, "Decode the input (default)");
        opt.addOption("v", "verbose", false, "Print trace information to stderr");
        Parser parser = new BasicParser();
        try {
            CommandLine cl = parser.parse(opt, args);
            if (cl.hasOption('v')) {
                beVerbose = true;
            }
            if (cl.hasOption('h')) {
                showUsage(opt);
            } else if (((cl.getOptions().length > 0) && !cl.hasOption('g') && !cl.hasOption('j')) || cl.hasOption('t')) {
                traceln(BANNER);
                BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
                BufferedWriter out = new BufferedWriter(new OutputStreamWriter(System.out));
                Dvorcode.Codec oper = Dvorcode.Codec.DECODE;
                if (cl.hasOption('d')) {
                    oper = Dvorcode.Codec.DECODE;
                } else if (cl.hasOption('e')) {
                    oper = Dvorcode.Codec.ENCODE;
                }
                if (cl.hasOption('f')) {
                    String inFile = cl.getOptionValue('f');
                    traceln(breadCrumb(), "Using input file: \t", inFile);
                    try {
                        in = new BufferedReader(new FileReader(inFile));
                    } catch (IOException e) {
                        reportAbort(ExitCode.FILE_INPUT, inFile, e);
                    }
                }
                if (cl.hasOption('o')) {
                    String outFile = cl.getOptionValue('o');
                    traceln(breadCrumb(), "Using output file:\t", outFile);
                    try {
                        out = new BufferedWriter(new FileWriter(outFile));
                    } catch (IOException e) {
                        reportAbort(ExitCode.FILE_OUTPUT, outFile, e);
                    }
                }
                try {
                    launchFilter(in, out, oper);
                } catch (IOException e) {
                    reportAbort(ExitCode.XCODE_IO, null, e);
                }
            } else if (cl.hasOption('j')) {
                try {
                    UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
                } catch (Exception e) {
                    traceln(breadCrumb(), "Exception loading Nimbus look-and-feel");
                    e.printStackTrace(System.err);
                    traceln(breadCrumb(), "Falling back to Metal / default...");
                }
                launchGui();
            } else {
                assert (((0 == cl.getOptions().length) || cl.hasOption('g')) && (!cl.hasOption('j') && !cl.hasOption('t'))) : Arrays.asList(cl.getOptions());
                try {
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                } catch (Exception e) {
                    traceln(breadCrumb(), "Exception loading host OS look-and-feel:");
                    e.printStackTrace(System.err);
                    traceln(breadCrumb(), "Falling back to default look-and-feel...");
                }
                launchGui();
            }
        } catch (ParseException e) {
            ExitCode err = ExitCode.BAD_OPTION;
            reportError(err, null, e);
            System.err.println();
            showUsage(opt);
            abort(err);
        } catch (Exception e) {
            reportAbort(ExitCode.GENERAL_ERROR, null, e);
        }
    }

    /** prints usage information to stdout. This explains the
     *  command-line arguments.
     *
     *  @param opt the command-line options
     */
    private static void showUsage(Options opt) {
        String cmd = "java -jar " + PROGNAME + ".jar [option]|[option <arg>] ...";
        HelpFormatter formatter = new HelpFormatter();
        System.out.println(BANNER);
        formatter.printHelp(cmd, opt);
    }

    /** prints an exit message to System.err, which contains the
     *  message from the exception
     *
     *  @param exit the exit to report
     *  @param detail additional diagnostic detail string (may be null)
     *  @param excp an exception
     *
     *  @throws NullPointerException if parameters are null
     */
    private static void reportError(ExitCode exit, String detail, Exception excp) {
        if ((null == detail) || ("".equals(detail))) {
            detail = "";
        } else {
            detail = ":\t" + detail;
        }
        System.err.println(breadCrumb() + exit.getDescription() + detail);
        System.err.println(breadCrumb() + "Message: " + excp.getMessage());
    }

    /** prints an exit message to stderr and then exits the JVM with
     *  supplied exit code
     *
     *  @param exit the ExitCode enumerating the exit code
     */
    private static void abort(ExitCode exit) {
        traceln(breadCrumb(), "*** Aborting ***");
        System.exit(exit.getCode());
    }

    /** prints an exit message to System.err, which contains the
     *  message from the exception, then exits with supplied exit code
     * 
     *  @param exit the exit to report
     *  @param detail additional diagnostic detail string (may be null)
     *  @param excp an exception
     * 
     *  @throws NullPointerException if parameters are null
     * 
     *  @see #reportError(net.sf.dvorcode.Main.ExitCode,
     *                    java.lang.String, java.lang.Exception) 
     *  @see #abort(exit)
     */
    private static void reportAbort(ExitCode exit, String detail, Exception excp) {
        reportError(exit, detail, excp);
        abort(exit);
    }

    /** tests if tracing is turned on, and if so, prints the supplied
     *  message to System.err stream
     *
     *  @param message the trace message to print
     */
    static void trace(Object... message) {
        if (beVerbose) {
            StringBuilder msg = new StringBuilder();
            for (Object m : message) {
                msg.append(m.toString());
            }
            System.err.print(msg);
        }
    }

    /** tests if tracing is turned on, and if so, prints the supplied
     *  message to System.err stream, followed by a newline.
     *
     *  @param message the trace message to print
     *  @see #trace(java.lang.Object[]) 
     */
    static void traceln(Object... message) {
        if (beVerbose) {
            trace(message);
            System.err.println();
        }
    }

    /** @return a String containing the breadcrumb identity of the
     *          calling object, together with this program's name.
     *          Useful for debugging trace calls.
     * 
     *  @param caller the object sending the message
     */
    static String breadCrumb(Object caller) {
        return PROGNAME + ":" + Thread.currentThread().getName() + ":" + caller.getClass().getSimpleName() + ": ";
    }

    /** @return a String containing the breadcrumb identity of the
     *          caller (static), together with this program's name.
     *          Useful for debugging trace calls from static methods
     */
    static String breadCrumb() {
        return PROGNAME + ":" + Thread.currentThread().getName() + ":<Main>: ";
    }

    /** Starts a net.sf.dvorcode.MainFrame Swing frame to provide a
     *  graphic interface.
     */
    static void launchGui() {
        traceln(breadCrumb(), "launching GUI...");
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                traceln(breadCrumb(this), "running GUI...");
                MainFrame mf = new MainFrame();
                mf.setLocationByPlatform(true);
                mf.setVisible(true);
                traceln(breadCrumb(this), "GUI running");
            }
        });
    }

    /** Implements a Dvorak encode/decode filter, with progress tracing
     *  for every few lines trans-coded.
     *
     *  @param in the input stream to be filtered.
     *  @param out the stream for filtered output.
     *  @param operation the trans-code operation (encode/decode). If
     *         null, then the decode operation is chosen be default.
     *  @throws IOException if there's a problem reading/writing the
     *          streams
     *  @see Dvorcode#transcode(java.lang.String, net.sf.dvorcode.Dvorcode.Codec) 
     */
    static void launchFilter(BufferedReader in, BufferedWriter out, Dvorcode.Codec operation) throws IOException {
        if ((null != in) && (null != out)) {
            String txMsg = breadCrumb() + "Trans-coding: ";
            String txDone = " done";
            int txFreq = 8192;
            int txMargin;
            try {
                txMargin = Integer.parseInt(System.getenv("COLUMNS")) - 2;
            } catch (NumberFormatException e) {
                txMargin = 78;
            }
            if (null == operation) {
                operation = Dvorcode.Codec.DECODE;
            }
            String opMsg = (operation == Dvorcode.Codec.DECODE ? "Operation:\t\tDECODE" : "Operation:\t\tENCODE");
            traceln(breadCrumb(), opMsg);
            int lines = 0;
            int column = txMsg.length() + 1;
            trace(txMsg);
            PrintWriter outwr = new PrintWriter(out);
            try {
                String msg;
                while (null != (msg = in.readLine())) {
                    outwr.println(Dvorcode.transcode(msg, operation));
                    if (beVerbose && (0 == lines++ % txFreq)) {
                        trace(".");
                        column++;
                    }
                    if (beVerbose && column > txMargin) {
                        traceln();
                        column = 1;
                    }
                }
            } finally {
                in.close();
                outwr.close();
                out.close();
            }
            if (beVerbose) {
                if ((column + txDone.length()) > txMargin) {
                    traceln();
                }
                traceln(txDone);
                traceln(breadCrumb(), "Trans-coded " + lines + " lines to output");
            }
        }
    }
}
