package me.shimado.simpleplugin

import org.bukkit.Material
import org.bukkit.inventory.ItemStack

class SItem {

    /**
     * ДАЕТ ИГРОКУ КАСТОМНЫЙ ПРЕДМЕТ ПО МАТЕРИАЛУ
     * **/

    fun create(material: Material, name: String, lore: List<String>, enchant: Boolean): ItemStack {
        return create(new ItemStack(material), name, lore, enchant);
    }

    /**
     * ДАЕТ ИГРОКУ КАСТОМНЫЙ ПРЕДМЕТ ПО ПРЕДМЕТУ
     * **/

    @SuppressWarnings("all")
    public static ItemStack create(ItemStack item, String name, List<String> lore, boolean enchant) {
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(CommonFunc.getColor(name));
        if(meta.getLore() != null) {
            meta.getLore().clear();
        }
        meta.setLore(CommonFunc.getColorList(lore));
        if(enchant) {
            meta.addEnchant(Enchantment.KNOCKBACK, 1, true);
        }
        meta.addItemFlags(ItemFlag.values());
        item.setItemMeta(meta);
        return item;
    }

    /**
     * ПОЛУЧАЕТ ГОЛОВУ ИГРОКА И ДЕЛАЕТ ОПИСАНИЕ
     **/

    public static ItemStack getHead(String url, String name, List<String> lore, boolean enchant) {
        url = "http://textures.minecraft.net/texture/" + url;
        ItemStack skull = new ItemStack(Material.PLAYER_HEAD, 1, (byte) 3);
        SkullMeta skullMeta = (SkullMeta) skull.getItemMeta();
        GameProfile profile = new GameProfile(UUID.randomUUID(), null);
        byte[] encodedData = Base64.encodeBase64(String.format("{textures:{SKIN:{url:\"%s\"}}}", url).getBytes());
        profile.getProperties().put("textures", new Property("textures", new String(encodedData)));
        Field profileField = null;
        try {
            profileField = skullMeta.getClass().getDeclaredField("profile");
        } catch (NoSuchFieldException | SecurityException e) {
            e.printStackTrace();
        }
        assert profileField != null;
        profileField.setAccessible(true);
        try {
            profileField.set(skullMeta, profile);
        } catch (IllegalArgumentException | IllegalAccessException e) {
            e.printStackTrace();
        }
        skull.setItemMeta(skullMeta);
        return create(skull, name, lore, enchant);
    }

    public static ItemStack createTag(Object url, String name, List<String> lore, boolean enchant, String tagname, String tag) {
        ItemStack item = null;
        if(url instanceof Material){
            item = create(new ItemStack((Material) url), name, lore, enchant);
        }else if(url instanceof ItemStack){
            item = create((ItemStack) url, name, lore, enchant);
        }else if(url instanceof String && ((String) url).length() > 15){
            item = getHead((String) url, name, lore, false);
        }
        Object obj = NMS_ULTIMATE.getItemStack(item);
        Object nbt = NMS_ULTIMATE.getOrCreateTag_(obj);
        NMS_ULTIMATE.setTag(nbt, tagname, tag);
        NMS_ULTIMATE.setTag(obj, nbt);
        return NMS_ULTIMATE.getItemBack(obj);
    }


    /**
     * РАСШИФРОВЫВАЕТ ОПИСАНИЕ ПРЕДМЕТА
     * **/

    public static String getTag(ItemStack item, String tag){
        Object obj = NMS_ULTIMATE.getItemStack(item);

        if(NMS_ULTIMATE.getTag_(obj) == null){
            return null;
        }

        String result = NMS_ULTIMATE.getTagTag(obj, tag);

        if(result == null || result.length() == 0){
            return  null;
        }

        return result;
    }

    /**
     * ПОЛУЧАЕТ КАЧЕСТВО
     * **/

    public static double getQualityTag(ItemStack item, String tag){
        String text = getTag(item, tag);
        if(text == null) return  -1;
        return Double.parseDouble(text.split("/")[1]);
    }

    /**
     * СТАВИТ БРОНЮ
     * **/

    public static ItemStack setArmor(ItemStack item, int armor){
        AttributeModifier modifier = new AttributeModifier(UUID.randomUUID(), "geretic.armor", armor, AttributeModifier.Operation.ADD_NUMBER);
        ItemMeta meta = item.getItemMeta();
        meta.addAttributeModifier(Attribute.GENERIC_ARMOR, modifier);
        item.setItemMeta(meta);
        return item;
    }

    /**
     * КОЖАННАЯ БРОНЯ
     **/

    public static ItemStack getColorArmor(Color color, int slot) {

        Material material = null;
        switch (slot) {
            case 1:
            material = Material.LEATHER_HELMET;
            break;
            case 2:
            material = Material.LEATHER_CHESTPLATE;
            break;
            case 3:
            material = Material.LEATHER_LEGGINGS;
            break;
            case 4:
            material = Material.LEATHER_BOOTS;
            break;
        }

        ItemStack item = ItemCreate.create(material, " ", new ArrayList<>(), false);
        LeatherArmorMeta meta = (LeatherArmorMeta) item.getItemMeta();
        meta.setColor(color);
        item.setItemMeta(meta);

        return item;

    }

}