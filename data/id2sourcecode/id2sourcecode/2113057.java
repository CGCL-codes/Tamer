    @Test
    public void mediaInfos() {
        try {
            Infos infos = fr.bibiche.mediaInfos.MediaInfos.getMediaInfos(new File(getClass().getResource("/test.avi").getPath()));
            assertEquals("320", infos.getVideo().getWidth());
            assertEquals("240", infos.getVideo().getHeight());
            assertEquals("Progressive", infos.getVideo().getScanType());
            assertEquals("MPEG Audio", infos.getAudio().getFormat());
            assertEquals("2", infos.getAudio().getChannel());
        } catch (MediaInfosException e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
    }
