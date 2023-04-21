package cn.edu.wuse.musicxml.demo;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import javax.sound.midi.MidiSystem;
import cn.edu.wuse.musicxml.parser.MidiRenderer;
import cn.edu.wuse.musicxml.parser.MusicParser;
import cn.edu.wuse.musicxml.parser.MusicXmlParser;
import cn.edu.wuse.musicxml.parser.PartwiseParser;

public class ParserDemo {

    public static void main(String[] args) {
        MusicXmlParser parser = new PartwiseParser();
        parser.deBug(MusicParser.DEBUG_ON);
        MidiRenderer render = new MidiRenderer();
        parser.addMusicParserListener(render);
        try {
            InputStream ins = new FileInputStream(TestData.ActorPreludeSample);
            parser.parse(ins);
            MidiSystem.write(render.getSequence(), 1, new File(TestData.Save_ActorPreludeSample));
            ins.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
