    public void initFromLocation() throws Throwable {
        try {
            StringBuffer output = new StringBuffer();
            File file = new File(getLocation());
            FileChannel inChannel = null;
            try {
                Charset encoding = Charset.forName("UTF-8");
                ByteBuffer buf = ByteBuffer.allocate(4096);
                inChannel = (new FileInputStream(file)).getChannel();
                while (inChannel.read(buf) != -1) {
                    buf.rewind();
                    output.append(encoding.decode(buf).toString());
                    buf.clear();
                }
            } catch (Exception e) {
            } finally {
                try {
                    if (inChannel != null) inChannel.close();
                } catch (Exception e) {
                }
            }
            String content = output.toString();
            if (StringUtils.isEmpty(content)) return;
            if (_regexData == null) _regexData = Pattern.compile("<DataBlock>(.*?)</DataBlock>", Pattern.DOTALL);
            Matcher matcher = _regexData.matcher(content);
            if (matcher.find()) {
                try {
                    String data = matcher.group(1);
                    DocumentBuilder db = (DocumentBuilderFactory.newInstance()).newDocumentBuilder();
                    InputSource source = new InputSource();
                    source.setCharacterStream(new StringReader("<document>" + data + "</document>"));
                    Document doc = db.parse(source);
                    Element root = getElement(doc.getDocumentElement(), "/document");
                    setGameType(getElementAttribute(root, "game", "type"));
                    setLevel(getElementAttributeAsInt(root, "level", "value"));
                    setSize(getElementAttribute(root, "size", "value"));
                    _abilityStr.setValue(getElementAttributeAsInt(root, "abilities/str", "value"), Types.Ability);
                    _abilityDex.setValue(getElementAttributeAsInt(root, "abilities/dex", "value"), Types.Ability);
                    _abilityCon.setValue(getElementAttributeAsInt(root, "abilities/con", "value"), Types.Ability);
                    _abilityInt.setValue(getElementAttributeAsInt(root, "abilities/int", "value"), Types.Ability);
                    _abilityWis.setValue(getElementAttributeAsInt(root, "abilities/wis", "value"), Types.Ability);
                    _abilityCha.setValue(getElementAttributeAsInt(root, "abilities/cha", "value"), Types.Ability);
                    _saveFort.setValue(getElementAttributeAsInt(root, "saves/fort", "value"), getElementAttributeAsInt(root, "saves/fort", "misc"), GameAbilities.Constitution, Types.Save);
                    _saveRef.setValue(getElementAttributeAsInt(root, "saves/ref", "value"), getElementAttributeAsInt(root, "saves/ref", "misc"), GameAbilities.Dexterity, Types.Save);
                    _saveWill.setValue(getElementAttributeAsInt(root, "saves/will", "value"), getElementAttributeAsInt(root, "saves/will", "misc"), GameAbilities.Wisdom, Types.Save);
                    setInitiativeModifier(getElementAttributeAsInt(root, "initiative", "value"));
                    setAttackBase(getElementAttributeAsInt(root, "attacks", "base"));
                    _attackGrapple.setValue(getElementAttributeAsInt(root, "attacks/grapple", "value"), GameResourceAttributeAttack.ATTACK_TYPE_MELEE, GameResourceAttributeAttack.ATTACK_TYPE_SECONDARY_NONE);
                    _attackMelee.setValue(getElementAttributeAsInt(root, "attacks/melee", "value"), GameResourceAttributeAttack.ATTACK_TYPE_MELEE, GameResourceAttributeAttack.ATTACK_TYPE_SECONDARY_NONE);
                    _attackRanged.setValue(getElementAttributeAsInt(root, "attacks/ranged", "value"), GameResourceAttributeAttack.ATTACK_TYPE_RANGED, GameResourceAttributeAttack.ATTACK_TYPE_SECONDARY_NONE);
                    _attackUnarmed.setValue(getElementAttributeAsInt(root, "attacks/unarmed", "value"), GameResourceAttributeAttack.ATTACK_TYPE_MELEE, GameResourceAttributeAttack.ATTACK_TYPE_SECONDARY_UNARMED);
                    _attackUnarmed.setFinessable(getElementAttributeAsInt(root, "attacks/unarmed", "finessable") > 0 ? true : false);
                    _attackUnarmed.setDamage(getElementAttribute(root, "attacks/unarmed", "damage_base"));
                    _attackUnarmed.setMisc(getElementAttributeAsInt(root, "attacks/unarmed", "damage_bonus"));
                    if (!_reload) {
                        _damagePrimary.setValueCurrent(getElementAttributeAsInt(root, "damage/primary", "current"));
                        _damagePrimary.setValue(getElementAttributeAsInt(root, "damage/primary", "value"));
                        _damageSecondary.setValueCurrent(getElementAttributeAsInt(root, "damage/secondary", "current"));
                        _damageSecondary.setValue(getElementAttributeAsInt(root, "damage/secondary", "value"));
                        _damagePrimary.setSecondary(_damageSecondary);
                        _damageReduction.setValue(getElementAttributeAsInt(root, "damage", "dr"));
                    }
                    _defense.setValue(getElementAttributeAsInt(root, "defense/primary", "value"));
                    _defense.setValueFlatFooted(getElementAttributeAsInt(root, "defense/flatfooted", "value"));
                    _defense.setValueTouch(getElementAttributeAsInt(root, "defense/touch", "value"));
                    _defense.setDexModifierIncludeFlatFooted(getElementAttributeAsInt(root, "defense/flatfooted", "include_dex_modifier"));
                    _defense.setDexModifierMax(getElementAttributeAsInt(root, "defense", "max_dex_modifier"));
                    Element skills = getElement(root, "skills");
                    NodeList nodes = skills.getChildNodes();
                    nodes = (NodeList) _xpath.evaluate("skills/skill", root, XPathConstants.NODESET);
                    for (int i = 0; i < nodes.getLength(); i++) {
                        Element element = (Element) nodes.item(i);
                        GameResourceAttributeSkill skill = new GameResourceAttributeSkill();
                        skill.setCharacter(this);
                        skill.setValue(element.getTextContent(), getElementAttribute(element, "ability"), getElementAttributeAsDecimal(element, "rank"), getElementAttributeAsInt(element, "misc"), getElementAttributeAsBoolean(element, "untrained"));
                        _skills.add(skill);
                    }
                    nodes = (NodeList) _xpath.evaluate("attacks/weapon", root, XPathConstants.NODESET);
                    for (int i = 0; i < nodes.getLength(); i++) {
                        Element element = (Element) nodes.item(i);
                        GameResourceWeapon weapon = new GameResourceWeapon();
                        weapon.setCharacter(this);
                        weapon.setValue(element.getTextContent(), getElementAttributeAsInt(element, "melee"), getElementAttributeAsInt(element, "ranged"), getElementAttributeAsInt(element, "hands"), getElementAttributeAsInt(element, "finessable"), getElementAttributeAsInt(element, "light"), getElementAttributeAsInt(element, "hit_bonus"), getElementAttributeAsInt(element, "twoweapon_modifier_primary"), getElementAttributeAsInt(element, "twoweapon_modifier_secondary"), getElementAttributeAsInt(element, "attack_separator"), getElementAttribute(element, "damage_base"), getElementAttributeAsInt(element, "damage_bonus"));
                        _weapons.add(weapon);
                    }
                    initAttributes();
                } catch (ParserConfigurationException e) {
                    Logger.e(TAG, "init - XML parse error: " + e.getMessage());
                    throw new GameResourceLoadException();
                } catch (SAXException e) {
                    Logger.e(TAG, "init - Wrong XML file structure: " + e.getMessage());
                    throw new GameResourceLoadException();
                }
            }
        } catch (Throwable tr) {
            Logger.e(TAG, "init", tr);
            throw tr;
        }
    }
