package org.occ.TileCache;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper.Context;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.SequenceFileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.SequenceFileOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;
import org.occ.Image.TimeStampImage;
import org.occ.utilities.GeoSpatialUtilities;
import com.bbn.openmap.proj.coords.BoundingBox;

public class BuildTileCacheFromTimeStamp extends Configured implements Tool {

    private static Path inputPath = null;

    private static Path outputPath = null;

    private static String zoomLevelsStr = null;

    /**
	 * MapPictures is the class that does the mapping of source imagery to destination
	 * imagery.
	 * 
	 * @author alevine@texeltek.com
	 *
	 */
    public static class MapPictures extends org.apache.hadoop.mapreduce.Mapper<Text, TimeStampImage, Text, BytesWritable> {

        int[] zoomLevels = null;

        int width = 256;

        int height = 256;

        /**
		 * setup() is run before any mappers are run
		 */
        public void setup(Context context) {
            Configuration conf = context.getConfiguration();
            zoomLevels = getZoomLevels(conf.get("zoomLevelsStr"));
            width = Integer.parseInt(conf.get("imageWidth"));
            height = Integer.parseInt(conf.get("imageHeight"));
        }

        /**
		 * map() is the mapper function for pictures
		 * 
		 * as of 20100609 we are looking at Plate Carree only
		 * 
		 */
        public void map(Text key, TimeStampImage img, Context context) throws IOException, InterruptedException {
            BoundingBox srcBox = GeoSpatialUtilities.getBoundingBoxFromFileName(key.toString());
            byte[] imgBytes = img.getImage().getBytes();
            InputStream is = new ByteArrayInputStream(imgBytes);
            BufferedImage srcImg = ImageIO.read(is);
            is.close();
            double srcVertDPP = (srcBox.getMaxY() - srcBox.getMinY()) / ((double) srcImg.getHeight());
            double srcHorzDPP = (srcBox.getMaxX() - srcBox.getMinX()) / ((double) srcImg.getWidth());
            for (int x = 0; x < zoomLevels.length; x++) {
                ArrayList<BoundingBox> boxes = GeoSpatialUtilities.getBoundingBoxSet(zoomLevels[x], srcBox);
                if (boxes == null || boxes.size() == 0) {
                    continue;
                }
                double destVertStepDeg = boxes.get(0).getMaxY() - boxes.get(0).getMinY();
                double destHorzStepDeg = boxes.get(0).getMaxX() - boxes.get(0).getMinX();
                double destVertDPP = destVertStepDeg / ((double) height);
                double destHorzDPP = destHorzStepDeg / ((double) width);
                for (int y = 0; y < boxes.size(); y++) {
                    BoundingBox overlap = GeoSpatialUtilities.getOverlapBox(boxes.get(y), srcBox);
                    if (overlap == null) {
                        continue;
                    }
                    double srcMinDegX = overlap.getMinX() - srcBox.getMinX();
                    int srcMinPX = (int) (srcMinDegX / srcHorzDPP);
                    double srcMaxDegX = srcBox.getMaxX() - overlap.getMaxX();
                    int srcMaxPX = srcImg.getWidth() - (int) (srcMaxDegX / srcHorzDPP);
                    double srcMaxDegY = srcBox.getMaxY() - overlap.getMaxY();
                    int srcMinPY = (int) (srcMaxDegY / srcVertDPP);
                    double srcMinDegY = overlap.getMinY() - srcBox.getMinY();
                    int srcMaxPY = srcImg.getHeight() - (int) (srcMinDegY / srcVertDPP);
                    double destMinDegX = overlap.getMinX() - boxes.get(y).getMinX();
                    int destMinPX = (int) (destMinDegX / destHorzDPP);
                    double destMaxDegX = boxes.get(y).getMaxX() - overlap.getMaxX();
                    int destMaxPX = width - (int) (destMaxDegX / destHorzDPP);
                    double destMaxDegY = boxes.get(y).getMaxY() - overlap.getMaxY();
                    int destMinPY = (int) (destMaxDegY / destVertDPP);
                    double destMinDegY = overlap.getMinY() - boxes.get(y).getMinY();
                    int destMaxPY = height - (int) (destMinDegY / destVertDPP);
                    BufferedImage outImage = new BufferedImage(width, height, BufferedImage.TYPE_4BYTE_ABGR);
                    Graphics2D outGr = outImage.createGraphics();
                    outGr.drawImage(srcImg, destMinPX, destMinPY, destMaxPX, destMaxPY, srcMinPX, srcMinPY, srcMaxPX, srcMaxPY, null);
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    ImageIO.write(outImage, "png", baos);
                    BytesWritable outValue = new BytesWritable(baos.toByteArray());
                    Text outKey = new Text(GeoSpatialUtilities.getBoundingBoxString(boxes.get(y)) + "_" + width + "x" + height);
                    context.write(outKey, outValue);
                }
            }
        }
    }

    /**
	 * ReducePictures is the class that combiles the pieces of image files into the
	 * final images.
	 * 
	 * @author alevine@texeltek.com
	 *
	 */
    public static class ReducePictures extends org.apache.hadoop.mapreduce.Reducer<Text, BytesWritable, Text, BytesWritable> {

        int width = 256;

        int height = 256;

        /**
		 * setup() is run before any reducer is run
		 */
        public void setup(Context context) {
            Configuration conf = context.getConfiguration();
            width = Integer.parseInt(conf.get("imageWidth"));
            height = Integer.parseInt(conf.get("imageHeight"));
        }

        /**
		 * reduce() is the reduce for putting images together
		 */
        public void reduce(Text key, Iterable<BytesWritable> values, Context context) throws IOException, InterruptedException {
            BufferedImage outImage = null;
            Graphics2D outGr = null;
            BytesWritable outValue = new BytesWritable();
            Text outKey = new Text();
            for (BytesWritable img : values) {
                byte[] imgBytes = img.getBytes();
                InputStream is = new ByteArrayInputStream(imgBytes);
                BufferedImage curImg = ImageIO.read(is);
                is.close();
                if (outImage == null) {
                    outImage = new BufferedImage(width, height, BufferedImage.TYPE_4BYTE_ABGR);
                    outGr = outImage.createGraphics();
                }
                outGr.drawImage(curImg, 0, 0, curImg.getWidth(), curImg.getHeight(), null);
            }
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(outImage, "png", baos);
            outValue.set(new BytesWritable(baos.toByteArray()));
            baos.close();
            outKey.set(key.toString() + ".png");
            context.write(outKey, outValue);
        }
    }

    /**
	 * getZoomLevels() will parse the value from the configuration and
	 * create an array of the integers of the zoom levels to work on
	 * 
	 * @param str is expected to be in the form 1-2,8,10
	 * @return an integer array of zoom levels
	 */
    public static int[] getZoomLevels(String str) {
        ArrayList<Integer> retList = new ArrayList<Integer>();
        String[] els = str.split(",");
        for (int x = 0; x < els.length; x++) {
            if (els[x].contains("-")) {
                String[] els2 = els[x].split("-");
                int val0 = Integer.parseInt(els2[0]);
                int val1 = Integer.parseInt(els2[1]);
                for (int y = val0; y <= val1; y++) {
                    if (y > 0 && y <= 30) {
                        retList.add(new Integer(y));
                    }
                }
            } else {
                int val = Integer.parseInt(els[x]);
                if (val > 0 && val <= 30) {
                    retList.add(new Integer(val));
                }
            }
        }
        int[] ret = new int[retList.size()];
        for (int x = 0; x < retList.size(); x++) {
            ret[x] = retList.get(x).intValue();
        }
        return ret;
    }

    /**
	 * showUse() will display the correct way to call the program
	 * 
	 * @param err is the error message
	 */
    public void showUse(String err) {
        if (err != null) {
            System.out.println("Error: " + err);
        }
        System.out.println("Usage: hadoop jar Matsu.jar org.occ.TileCache.BuildTileCacheFromTimeStamp -i inputDir -o outputDir -zl 1-2,4,8 -r 4");
    }

    /**
	 * run() is the function that runs the map reduce job
	 */
    public int run(String[] args) throws Exception {
        int numberReducers = 4;
        for (int x = 0; x < args.length; x++) {
            if (args[x].equals("-i")) {
                inputPath = new Path(args[x + 1]);
            } else if (args[x].equals("-o")) {
                outputPath = new Path(args[x + 1]);
            } else if (args[x].equals("-zl")) {
                zoomLevelsStr = args[x + 1];
            } else if (args[x].equals("-r")) {
                numberReducers = Integer.parseInt(args[x + 1]);
            }
        }
        if (inputPath == null || outputPath == null) {
            showUse("Problem with directory settings");
            return -1;
        }
        if (zoomLevelsStr == null) {
            showUse("No zoom levels to work with");
            return -1;
        }
        int[] zoomLevels = getZoomLevels(zoomLevelsStr);
        System.out.println("Zoom Levels:");
        for (int x = 0; x < zoomLevels.length; x++) {
            System.out.print(" " + zoomLevels[x]);
        }
        System.out.println();
        Configuration conf = new Configuration(true);
        conf.set("zoomLevelsStr", zoomLevelsStr);
        conf.set("srcprojection", "EPSG:4326");
        conf.set("destprojection", "EPSG:4326");
        conf.set("imageWidth", "512");
        conf.set("imageHeight", "256");
        Job tileCacheJob = new Job(conf, "Tile Cache for ZL=" + zoomLevelsStr);
        tileCacheJob.setInputFormatClass(SequenceFileInputFormat.class);
        tileCacheJob.setOutputFormatClass(SequenceFileOutputFormat.class);
        tileCacheJob.setJarByClass(BuildTileCacheFromTimeStamp.class);
        tileCacheJob.setMapperClass(BuildTileCacheFromTimeStamp.MapPictures.class);
        tileCacheJob.setCombinerClass(BuildTileCacheFromTimeStamp.ReducePictures.class);
        tileCacheJob.setReducerClass(BuildTileCacheFromTimeStamp.ReducePictures.class);
        tileCacheJob.setMapOutputKeyClass(Text.class);
        tileCacheJob.setMapOutputValueClass(BytesWritable.class);
        tileCacheJob.setOutputKeyClass(Text.class);
        tileCacheJob.setOutputValueClass(BytesWritable.class);
        tileCacheJob.setNumReduceTasks(numberReducers);
        FileInputFormat.setInputPaths(tileCacheJob, inputPath);
        FileOutputFormat.setOutputPath(tileCacheJob, outputPath);
        int retVal = tileCacheJob.waitForCompletion(true) ? 0 : -1;
        return retVal;
    }

    /**
	 * main will run the Map-Reduce job
	 * 
	 * @param args are the command line arguments
	 */
    public static void main(String[] args) {
        try {
            ToolRunner.run(new Configuration(), new BuildTileCacheFromTimeStamp(), args);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
