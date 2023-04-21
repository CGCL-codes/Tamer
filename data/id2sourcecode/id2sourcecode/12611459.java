    public int keyframe(int scene, int frame) {
        ArrayList<Position> temp = new ArrayList<Position>();
        int scene_index = getSceneIndex(scene);
        if (scene_index == -1) {
            String name = newSceneDialog();
            if (name == null) return -1;
            scenes.add(new Scene(name, "", scene));
            scene_index = getSceneIndex(scene);
            scene_name.setText(name);
        }
        int frame_index = scenes.get(scene_index).getFrameIndex(frame);
        boolean proceed = true;
        if (frame_index != -1) {
            if (JOptionPane.showConfirmDialog(canvas, "There is already a frame keyframed here, would you like to overwrite it?") == 0) {
                proceed = true;
            } else {
                proceed = false;
            }
        }
        if (proceed) {
            for (int i = 0; i < getTempItems().size(); i++) {
                temp.add(new Position(getTempItems().get(i).getIndex(), getTempItems().get(i).getX(), getTempItems().get(i).getY(), getTempItems().get(i).getWidth(), getTempItems().get(i).getHeight()));
            }
            for (int i = 0; i < getFrameItems().size(); i++) {
                temp.add(new Position(getFrameItems().get(i).getIndex(), getFrameItems().get(i).getX(), getFrameItems().get(i).getY(), getFrameItems().get(i).getWidth(), getFrameItems().get(i).getHeight()));
            }
            scenes.get(scene_index).keyframe(frame_index, new Keyframe(frame, temp));
            canvas.deleteBoundingRec();
            frame_index = scenes.get(scene_index).getFrameIndex(frame);
            if (frame_index != -1) scenes.get(scene_index).getKeyframes().get(frame_index).setPositions(temp);
            initialiseItems();
            setLastFrame(thespis.keyframe_bar.getFrame());
            setCurrentFrame(thespis.keyframe_bar.getFrame());
            canvas.repaint();
            return 1;
        }
        return -1;
    }
