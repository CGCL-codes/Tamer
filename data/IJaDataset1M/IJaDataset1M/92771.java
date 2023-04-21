package muvis.audio;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import maudio.exceptions.CannotRetrieveMP3TagException;
import java.io.ByteArrayOutputStream;
import maudio.entities.AudioMetadata;
import maudio.entities.Snippet;
import maudio.extractors.MP3AudioMetadataExtractor;
import maudio.extractors.interfaces.IAudioMetadataExtractor;
import maudio.extractors.interfaces.IAudioSnippetExtractor;
import muvis.Environment;

/**
 * Class that generates a snippet of an mp3 file.
 * This snippet is generated randomly, but always have 7-9s.
 * @author Ricardo
 */
public class MuVisMP3AudioSnippetExtractor implements IAudioSnippetExtractor {

    private static IAudioMetadataExtractor extractor = new MP3AudioMetadataExtractor();

    private static int getBitrate(String originalBitrate) {
        int bitrate = 0;
        String bitrateStr = "";
        for (int i = 0; i < originalBitrate.length(); i++) {
            if (Character.isDigit(originalBitrate.charAt(i))) {
                bitrateStr += originalBitrate.charAt(i);
            }
        }
        bitrate = Integer.parseInt(bitrateStr);
        return bitrate;
    }

    @Override
    public Snippet extractAudioSnippet(String filename) {
        try {
            AudioMetadata metadata = Environment.getEnvironmentInstance().getDatabaseManager().getTrackMetadata(filename);
            if (metadata == null) {
                metadata = extractor.getAudioMetadata(filename);
            }
            int aproxBitrate = getBitrate(metadata.getBitrate());
            File inputFile = new File(filename);
            FileInputStream fileInputStream = new FileInputStream(inputFile);
            BufferedInputStream bufferedInputStream = new BufferedInputStream(fileInputStream);
            int maxSize = (int) (12 * (aproxBitrate * 1024));
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream(maxSize);
            double cutStartPoint = 0;
            if (bufferedInputStream != null) {
                byte[] buffer = new byte[4 * 1024];
                int data = 0;
                int totDataRead = 0;
                int readedData = 0;
                cutStartPoint = inputFile.length() * (1f / 4f);
                while (data != -1 && readedData < cutStartPoint && data < inputFile.length()) {
                    data = bufferedInputStream.read(buffer);
                    readedData += data;
                }
                while (data != -1 && totDataRead < maxSize && readedData < inputFile.length()) {
                    outputStream.write(buffer, 0, data);
                    data = bufferedInputStream.read(buffer);
                    totDataRead += data * 8;
                    readedData += data * 8;
                }
                byte[] snippetBytes = outputStream.toByteArray();
                outputStream.flush();
                outputStream.close();
                return new Snippet(snippetBytes);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (CannotRetrieveMP3TagException ex) {
            ex.printStackTrace();
        }
        return null;
    }
}
