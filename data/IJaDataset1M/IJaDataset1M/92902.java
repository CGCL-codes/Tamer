package edu.usc.epigenome.uecgatk.YapingWriter;

/**
 * @author yaping
 * @contact lyping1986@gmail.com
 * @time 2012 Mar 14, 2012 4:31:38 PM
 * 
 */
public class NOMeSeqReads extends cpgReads {

    private String sampleContext;

    private String refContext;

    /**
	 * @param chr
	 * @param genomeLoc
	 * @param methyStatus
	 * @param baseQ
	 * @param strand
	 * @param readID
	 */
    public NOMeSeqReads(String chr, int genomeLoc, char methyStatus, byte baseQ, char strand, String readID) {
        super(chr, genomeLoc, methyStatus, baseQ, strand, readID);
    }

    public NOMeSeqReads(String chr, int genomeLoc, String sampleContext, String refContext, char methyStatus, byte baseQ, char strand, String readID) {
        super(chr, genomeLoc, methyStatus, baseQ, strand, readID);
        this.sampleContext = sampleContext;
        this.refContext = refContext;
    }

    public String getSampleContext() {
        return this.sampleContext;
    }

    public String getRefContext() {
        return this.refContext;
    }
}
