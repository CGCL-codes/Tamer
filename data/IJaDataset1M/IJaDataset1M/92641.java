package net.sourceforge.jaad.mp4.boxes.impl;

import java.io.IOException;
import net.sourceforge.jaad.mp4.MP4InputStream;
import net.sourceforge.jaad.mp4.boxes.FullBox;

/**
 * This box within a Media Box declares the process by which the media-data in
 * the track is presented, and thus, the nature of the media in a track. For
 * example, a video track would be handled by a video handler.
 *
 * This box when present within a Meta Box, declares the structure or format of
 * the 'meta' box contents.
 *
 * There is a general handler for metadata streams of any type; the specific
 * format is identified by the sample entry, as for video or audio, for example.
 * If they are in text, then a MIME format is supplied to document their format;
 * if in XML, each sample is a complete XML document, and the namespace of the
 * XML is also supplied.
 * @author in-somnia
 */
public class HandlerBox extends FullBox {

    public static final int TYPE_VIDEO = 1986618469;

    public static final int TYPE_SOUND = 1936684398;

    public static final int TYPE_HINT = 1751740020;

    public static final int TYPE_META = 1835365473;

    public static final int TYPE_NULL = 1853189228;

    public static final int TYPE_ODSM = 1868854125;

    public static final int TYPE_CRSM = 1668445037;

    public static final int TYPE_SDSM = 1935962989;

    public static final int TYPE_M7SM = 1832350573;

    public static final int TYPE_OCSM = 1868788589;

    public static final int TYPE_IPSM = 1768977261;

    public static final int TYPE_MJSM = 1835692909;

    private long handlerType;

    private String handlerName;

    public HandlerBox() {
        super("Handler Box");
    }

    @Override
    public void decode(MP4InputStream in) throws IOException {
        super.decode(in);
        in.skipBytes(4);
        handlerType = in.readBytes(4);
        in.readBytes(4);
        in.readBytes(4);
        in.readBytes(4);
        handlerName = in.readUTFString((int) getLeft(in), MP4InputStream.UTF8);
    }

    /**
	 * When present in a media box, the handler type is an integer containing
	 * one of the following values:
	 * <ul>
	 * <li>'vide': Video track</li>
	 * <li>'soun': Audio track</li>
	 * <li>'hint': Hint track</li>
	 * <li>'meta': Timed Metadata track</li>
	 * </ul>
	 *
	 * When present in a meta box, it contains an appropriate value to indicate
	 * the format of the meta box contents. The value 'null' can be used in the
	 * primary meta box to indicate that it is merely being used to hold
	 * resources.
	 *
	 * @return the handler type
	 */
    public long getHandlerType() {
        return handlerType;
    }

    /**
	 * The name gives a human-readable name for the track type (for debugging
	 * and inspection purposes).
	 * 
	 * @return the handler type's name
	 */
    public String getHandlerName() {
        return handlerName;
    }
}
