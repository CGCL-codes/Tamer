    public void run() {
        screen.setIcon(null);
        KeyStates.key_typed = 0;
        stop_duration = (int) (Settings.GAME_DURATION * 60000);
        System.out.println("Will stop in ~" + stop_duration / 1000 + " seconds.");
        frames = 0;
        unfed_fish = 0;
        frame_duration_avg = 0;
        long frame_start;
        long frame_duration = 0;
        long add_every = Settings.FISH_ADD_TIME;
        long last_added_time = -add_every;
        long game_time = 0;
        LetterNode cur_letter = stree.root;
        String cur_string = "";
        int cur_tone = 5;
        start_time = System.currentTimeMillis();
        for (; ; ) {
            frame_start = System.currentTimeMillis();
            drawSprite(background, 0, 0);
            boolean hasActiveFish = false;
            for (int i = 0; i < fish.length; i++) {
                if (fish[i].active && fish[i].draw_food) {
                    Image f = fish_food[fish[i].food_frame];
                    drawSprite(f, fish[i].food_x - f.getWidth(null) / 2, fish[i].food_y - f.getHeight(null) / 2);
                }
            }
            if (frame_duration > 100) frame_duration = 100;
            if (Settings.MOVIE_MODE) frame_duration = 40;
            game_time += frame_duration;
            for (int i = 0; i < fish.length; i++) {
                if (fish[i].active) {
                    hasActiveFish = true;
                    int f = fish[i].current_frame;
                    drawSprite(fish[i].anim[f], fish[i].current_x, fish[i].current_y);
                    drawSprite(fish[i].character, fish[i].current_x + Settings.CHAR_OFFSET_X, fish[i].current_y + Settings.CHAR_OFFSET_Y, theta_table[f], Settings.CHAR_ROT_AXIS_X, Settings.CHAR_ROT_AXIS_Y);
                    fish[i].updateLocation(frame_duration);
                    if (fish[i].current_y + Settings.FISH_HEIGHT - fish[i].food_y > Settings.EAT_LOCATION) fish[i].draw_food = false;
                    if (fish[i].current_y > Settings.SCREEN_HEIGHT) {
                        fish[i].active = false;
                        if (!fish[i].fed) {
                            String pronounce = fish[i].pinyin + fish[i].tone;
                            swimming.remove(pronounce);
                            incorrectc[fish[i].char_num]++;
                            unfed_fish--;
                        }
                    }
                } else if ((game_time - last_added_time > add_every || unfed_fish == 0) && game_time < stop_duration) {
                    int c = (int) (Math.random() * num_chars);
                    String pronounce = chars_pinyin[c] + chars_tones[c];
                    if (!swimming.contains(pronounce)) {
                        int speed = (int) (Math.random() * (Settings.FISH_MAX_SPEED - Settings.FISH_MIN_SPEED)) + Settings.FISH_MIN_SPEED;
                        int randx = (int) (Math.random() * (Settings.SCREEN_WIDTH - Settings.FISH_WIDTH));
                        boolean ok_to_add = true;
                        for (int j = 0; j < fish.length; j++) {
                            if (fish[j].active && Math.abs(fish[j].current_x - randx) <= Settings.FISH_WIDTH) {
                                long time_until_bottom = fish[j].getTimeUntil(Settings.SCREEN_HEIGHT);
                                int y_loc = Fish.getYLocationAt(speed, -Settings.FISH_HEIGHT, time_until_bottom) + Settings.FISH_HEIGHT;
                                if (fish[j].current_y <= Settings.FISH_START_SPACE || y_loc >= Settings.SCREEN_HEIGHT) {
                                    ok_to_add = false;
                                    break;
                                }
                            }
                        }
                        if (ok_to_add) {
                            swimming.add(pronounce);
                            last_added_time = game_time;
                            fish[i].activate(fish_anim[(int) (Math.random() * 7)], (int) (Math.random() * 25), speed, randx, -Settings.FISH_HEIGHT, c, chars[c], chars_pinyin[c], chars_tones[c]);
                            unfed_fish++;
                        }
                    }
                }
            }
            if (!hasActiveFish && game_time > stop_duration) {
                System.out.println("END GAME");
                int overall_i = 0;
                int overall_c = 0;
                chars_to_study = new int[10];
                num_chars_to_study = 0;
                int cur = 0;
                for (int i = 0; i < 1024; i++) {
                    overall_i += incorrectc[i];
                    overall_c += correctc[i];
                    if (incorrectc[i] != 0) {
                        double total = incorrectc[i] + correctc[i];
                        int rate = (int) (correctc[i] / total * 100);
                        System.out.println("Character #" + i + " (" + chars_pinyin[i] + chars_tones[i] + "): correct pinyin " + rate + "%");
                        if (num_chars_to_study != chars_to_study.length) {
                            chars_to_study[num_chars_to_study] = i;
                            num_chars_to_study++;
                        } else if (correctc[i] == 0) {
                            chars_to_study[cur] = i;
                            cur++;
                            if (cur == chars_to_study.length) cur = 0;
                        }
                    }
                }
                double total = overall_i + miss + overall_c;
                if (total != 0) {
                    int rate = (int) (overall_c / total * 100);
                    System.out.println("Overall: " + rate + "% correct.");
                    accuracy.setText(WeiLiYu.ws.overall_accuracy() + rate + "%");
                }
                if (num_chars_to_study == 0) {
                    drawSprite(congrats, 0, 0);
                    save_list.setEnabled(false);
                } else {
                    save_list.setEnabled(true);
                    drawSprite(hungry_cloud, Settings.SCREEN_WIDTH / 2 - hungry_cloud.getWidth(null) / 2, Settings.SCREEN_HEIGHT / 2 - hungry_cloud.getHeight(null) / 2);
                    drawSprite(hungry, Settings.SCREEN_WIDTH / 2 - hungry.getWidth(null) / 2, Settings.SCREEN_HEIGHT / 2 - hungry.getHeight(null) / 2 - 82);
                    if (num_chars_to_study == 1) {
                        drawPChar(chars_to_study[0], Settings.SCREEN_WIDTH / 2, Settings.SCREEN_HEIGHT / 2);
                    } else {
                        int chars_row1 = (num_chars_to_study + 1) / 2;
                        int chars_row2;
                        if (num_chars_to_study % 2 == 0) chars_row2 = chars_row1; else chars_row2 = chars_row1 - 1;
                        int draw_at_x;
                        draw_at_x = Settings.SCREEN_WIDTH / 2 - (Settings.STUDY_CHAR_WIDTH / 2 * (chars_row1 - 1));
                        for (int i = 0; i < chars_row1; i++) {
                            drawPChar(chars_to_study[i], draw_at_x, Settings.STUDY_ROW1_Y);
                            draw_at_x += Settings.STUDY_CHAR_WIDTH;
                        }
                        draw_at_x = Settings.SCREEN_WIDTH / 2 - (Settings.STUDY_CHAR_WIDTH / 2 * (chars_row2 - 1));
                        for (int i = chars_row1; i < chars_row1 + chars_row2; i++) {
                            drawPChar(chars_to_study[i], draw_at_x, Settings.STUDY_ROW2_Y);
                            draw_at_x += Settings.STUDY_CHAR_WIDTH;
                        }
                    }
                }
                permanent_swap();
                retry_panel.setVisible(true);
                System.out.println("Game end. Average frame duration: " + frame_duration_avg / frames + "ms");
                return;
            }
            if (KeyStates.key_typed != 0) {
                if (KeyStates.key_typed == '\n') {
                    if (cur_string.length() != 0) {
                        boolean correct = false;
                        for (int i = 0; i < fish.length; i++) {
                            if (fish[i].active && !fish[i].fed && fish[i].pinyin.equals(cur_string) && fish[i].tone == cur_tone) {
                                unfed_fish--;
                                fish[i].fed = true;
                                fish[i].draw_food = true;
                                fish[i].food_x = fish[i].current_x + Settings.FOOD_SPAWN_OFFSET_X;
                                fish[i].food_y = fish[i].start_food_y = fish[i].current_y + Settings.FOOD_SPAWN_OFFSET_Y;
                                if (fish[i].food_y < Settings.SCREEN_HEIGHT) {
                                    fish[i].food_y += (int) (Math.random() * (Settings.SCREEN_HEIGHT - fish[i].food_y));
                                }
                                fish[i].start_y = fish[i].current_y;
                                fish[i].start_frame = fish[i].current_frame;
                                fish[i].time = 0;
                                fish[i].speed = Settings.FISH_EAT_SPEED;
                                String pronounce = fish[i].pinyin + fish[i].tone;
                                swimming.remove(pronounce);
                                correctc[fish[i].char_num]++;
                                correct = true;
                                break;
                            }
                        }
                        if (!correct) {
                            miss++;
                        }
                        cur_string = "";
                        cur_tone = 5;
                        cur_letter = stree.root;
                    }
                } else if (KeyStates.key_typed == '\b') {
                    if (cur_string.length() != 0) {
                        if (cur_tone != 5) cur_tone = 5; else {
                            cur_string = cur_string.substring(0, cur_string.length() - 1);
                            cur_letter = cur_letter.parent;
                        }
                    }
                } else if (KeyStates.key_typed >= '1' && KeyStates.key_typed <= '5') {
                    if (cur_string.length() != 0) {
                        cur_tone = KeyStates.key_typed - '1' + 1;
                    }
                } else {
                    if (cur_letter.next[KeyStates.key_typed - 'a'] != null) {
                        cur_string += KeyStates.key_typed;
                        cur_tone = 5;
                        cur_letter = cur_letter.next[KeyStates.key_typed - 'a'];
                    }
                }
                KeyStates.key_typed = 0;
            }
            drawPinyinString(pinyin, tones, p_offset, 3, Settings.SCREEN_WIDTH / 2, Settings.SCREEN_HEIGHT - Settings.PINYIN_OFFSET_Y, cur_string, cur_tone);
            swap_buffers();
            frame_duration = System.currentTimeMillis() - frame_start;
            frame_duration_avg += frame_duration;
            frames++;
            if (frame_duration < 30 && !Settings.MOVIE_MODE) {
                try {
                    sleep(30 - frame_duration);
                } catch (Exception e) {
                }
            }
            frame_duration = System.currentTimeMillis() - frame_start;
        }
    }
