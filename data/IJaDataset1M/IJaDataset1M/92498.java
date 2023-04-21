package au.csiro.atnf;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import au.csiro.atnf.cli.Command;
import au.csiro.atnf.cli.CommandLineOptionsComparator;
import au.csiro.atnf.util.Util;
import au.csiro.atnf.voresource.PsrfitsConvert;
import au.csiro.atnf.voresource.RpfitsConvert;
import au.csiro.atnf.voresource.VoResourceGenerator;

/**
 * Implements a command line interface for converting xFITS files into IVOA files.
 * 
 * Copyright 2010, CSIRO Australia All rights reserved.
 * 
 * @author Robert Bridle on 19/05/2010
 * @version $Revision: 6 $ $Date: 2011-05-08 20:17:14 -0400 (Sun, 08 May 2011) $
 */
public class FitsConverter implements Command {

    /**
     * Constant that defines the logger to be used.
     */
    private static final Logger LOG = LoggerFactory.getLogger(FitsConverter.class);

    /**
     * The command name.
     */
    public static final String COMMAND_NAME = "genvores";

    /**
     * Command options
     */
    private Options options;

    /**
     * Constructor
     */
    public FitsConverter() {
        this.options = createOptions();
    }

    /**
     * Returns the commands usage as a <code>String</code>
     * 
     * @param options
     *            the command line options.
     * @return the usage for this command.
     */
    public String getUsageString() {
        String header = "Converts an xFITS file into a VO resource xml file.";
        String footer = "\n Example: genvores --inputFile 2001-12-12_1903.C787 --outputFile 2001-12-12_1903.xml --rpfits [optional]--projectCode ABC\n" + "Will convert the RPFITS file to an VO resource file called 2001-12-12_1903.xml. " + "The file will be created if it doesn't already exist. ";
        StringWriter sw = new StringWriter();
        HelpFormatter formatter = new HelpFormatter();
        formatter.setOptionComparator(new CommandLineOptionsComparator());
        formatter.printHelp(new PrintWriter(sw), PRINT_WIDTH, COMMAND_NAME, header, options, 0, 1, footer);
        return sw.toString();
    }

    /**
     * Creates the command line options.
     * 
     * @return the command line options.
     */
    @Override
    @SuppressWarnings("static-access")
    public Options createOptions() {
        Options options = new Options();
        options.addOption(OptionBuilder.withArgName("file").hasArg().withDescription(" 1: a psrfits or rpfits file to be processed.").isRequired(true).withLongOpt("inputFile").create("i"));
        options.addOption(OptionBuilder.withArgName("file").hasArg().withDescription(" 2: the name of the voresource xml file to be written.").isRequired(true).withLongOpt("outputFile").create("o"));
        options.addOption(OptionBuilder.withDescription(" 3: input file is in psrfits format.").isRequired(false).withLongOpt("psrfits").create("p"));
        options.addOption(OptionBuilder.withDescription(" 4: input file is in rpfits format.").isRequired(false).withLongOpt("rpfits").create("r"));
        options.addOption(OptionBuilder.withArgName("text").hasArg().withDescription(" 5: (optional)the project code to be used in the generated files (overrides the data in the file).").isRequired(false).withLongOpt("projectCode").create("c"));
        return options;
    }

    /**
     * Run the FitsConverter command.
     * 
     * @param args
     *            command line arguments.
     * @throws ParseException
     *             thrown if the command line arguments can not be parsed.
     * @throws IOException
     *             thrown if netCDF can to be written to or read from.
     */
    @Override
    public void execute(String[] args) throws ParseException, IOException {
        try {
            CommandLine parsedCommandLine = new GnuParser().parse(options, args);
            String inputFilenameArg = parsedCommandLine.hasOption("inputFile") ? parsedCommandLine.getOptionValue("inputFile") : "";
            String outputFilenameArg = parsedCommandLine.hasOption("outputFile") ? parsedCommandLine.getOptionValue("outputFile") : "";
            boolean isPsrfits = parsedCommandLine.hasOption("psrfits");
            boolean isRpfits = parsedCommandLine.hasOption("rpfits");
            String projectCodeArg = parsedCommandLine.hasOption("projectCode") ? parsedCommandLine.getOptionValue("projectCode") : "";
            File inputFile = Util.getExistingFile(inputFilenameArg);
            VoResourceGenerator converter = null;
            if (isPsrfits) {
                converter = new PsrfitsConvert();
            } else if (isRpfits) {
                converter = new RpfitsConvert();
            } else {
                throw new IllegalStateException("Unknown fits file type.");
            }
            String result = converter.buildVOResource(inputFile, projectCodeArg);
            Util.writeTextFile(new File(outputFilenameArg), result);
            LOG.info("Success");
            System.out.println("Success");
            System.exit(0);
        } catch (Exception e) {
            LOG.error(e.getMessage());
            System.out.println(e.getMessage());
            System.out.println(getUsageString());
            System.exit(1);
        }
    }

    /**
     * @return the command name for this command.
     */
    @Override
    public String getCommandName() {
        return COMMAND_NAME;
    }

    /**
     * Performs validation of the command line options.
     * <p>
     * Performs the validation that can not be performed by simple "parsing" of the command line string.
     * 
     * @param command line args
     */
    @Override
    public String validCommand(String[] commandLine) {
        StringBuffer errorMsg = new StringBuffer();
        CommandLine parsedCommandLine = null;
        try {
            parsedCommandLine = new GnuParser().parse(options, commandLine);
        } catch (ParseException ex) {
            return ex.getMessage();
        }
        String inputFile = parsedCommandLine.hasOption("inputFile") ? parsedCommandLine.getOptionValue("inputFile") : "";
        boolean isPsrfits = parsedCommandLine.hasOption("psrfits");
        boolean isRpfits = parsedCommandLine.hasOption("rpfits");
        if (isPsrfits == isRpfits) {
            errorMsg.append("The fits file type must be specified, i.e. either psrfits or rpfits");
        }
        if (inputFile.length() > 0) {
            errorMsg.append(Util.getExistingFile(inputFile) == null ? inputFile + " - File could not be found" : "");
        }
        return errorMsg.toString();
    }
}
