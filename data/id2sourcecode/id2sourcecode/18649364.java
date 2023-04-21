    public static void main(String[] args) throws InterruptedException {
        System.out.println("== Starting VLCExample ==");
        String[] vlcArgs = new String[] { "-vvv", "--plugin-path=vlc_exec\\plugins" };
        JVLC jvlc = new JVLC(vlcArgs);
        System.out.println("... done.");
        System.out.println("You are running VLC version: " + jvlc.getVLCVersion());
        MediaDescriptor mediaDescriptor = new MediaDescriptor(jvlc, "http://www.youtube.com/get_video?video_id=jqxENMKaeCU&t=vjVQa1PpcFM2CjZYyLJUUxUhAEs_OAKYKfYnbDjARSg%3D&fmt=18");
        MediaPlayer mediaPlayer = mediaDescriptor.getMediaPlayer();
        mediaPlayer.addListener(new MediaPlayerListener() {

            public void endReached(MediaPlayer mediaPlayer) {
                System.out.println("Media instance end reached. MRL: " + mediaPlayer.getMediaDescriptor().getMrl());
            }

            public void paused(MediaPlayer mediaPlayer) {
                System.out.println("Media instance paused. MRL: " + mediaPlayer.getMediaDescriptor().getMrl());
            }

            public void playing(MediaPlayer mediaPlayer) {
                System.out.println("Media instance played. MRL: " + mediaPlayer.getMediaDescriptor().getMrl());
            }

            public void positionChanged(MediaPlayer mediaPlayer) {
            }

            public void timeChanged(MediaPlayer mediaPlayer, long newTime) {
                System.out.println("new time: " + newTime);
            }

            public void stopped(MediaPlayer mediaPlayer) {
                System.out.println("Media player stopped. MRL: " + mediaPlayer.getMediaDescriptor().getMrl());
            }

            public void errorOccurred(MediaPlayer mediaPlayer) {
                System.out.println("An error has occurred.");
            }
        });
        mediaPlayer.play();
        while (!mediaPlayer.hasVideoOutput()) {
            Thread.sleep(100);
        }
        Video video = new Video(jvlc);
        System.out.print(video.getWidth(mediaPlayer));
        System.out.print("x");
        System.out.println(video.getHeight(mediaPlayer));
        System.out.print("Fullscreen... ");
        video.setFullscreen(mediaPlayer, false);
        Thread.sleep(3000);
        System.out.println("real size.");
        video.setFullscreen(mediaPlayer, false);
        System.out.print("Taking snapshot... ");
        video.getSnapshot(mediaPlayer, System.getProperty("user.dir") + "/snap.png", 0, 0);
        System.out.println("taken. (see " + System.getProperty("user.dir") + "/snap.png )");
        Thread.sleep(2000);
        System.out.println("Resizing to 300x300");
        video.setSize(300, 300);
        System.out.print("Muting...");
        Audio audio = new Audio(jvlc);
        audio.setMute(true);
        Thread.sleep(3000);
        System.out.println("unmuting.");
        audio.setMute(false);
        Thread.sleep(3000);
        System.out.println("Volume is: " + audio.getVolume());
        System.out.print("Setting volume to 150... ");
        audio.setVolume(150);
        System.out.println("done");
        System.out.println("== AUDIO INFO ==");
        System.out.println("Audio track number: " + audio.getTrack(mediaPlayer));
        System.out.println("Audio channel info: " + audio.getChannel());
        Thread.sleep(3000);
        System.out.println("MEDIA PLAYER INFORMATION");
        System.out.println("--------------------------");
        System.out.println("Total length (ms) :\t" + mediaPlayer.getLength());
        System.out.println("Input time (ms) :\t" + mediaPlayer.getTime());
        System.out.println("Input position [0-1]:\t" + mediaPlayer.getPosition());
        System.out.println("Input FPS :\t" + mediaPlayer.getFPS());
        System.out.println("Everything fine ;)");
        try {
            System.out.println("Press any key to quit!)");
            System.in.read();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return;
    }
