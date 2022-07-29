package me.shimado.simpleplugin

import com.mojang.authlib.GameProfile
import com.mojang.authlib.properties.Property
import org.apache.commons.codec.binary.Base64
import org.bukkit.Material
import org.bukkit.attribute.Attribute
import org.bukkit.attribute.AttributeModifier
import org.bukkit.enchantments.Enchantment
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta
import java.util.UUID

class SItem {

    /**
     * ДАЕТ ИГРОКУ КАСТОМНЫЙ ПРЕДМЕТ ПО МАТЕРИАЛУ
     * **/

    companion object {

        fun create(material: Material, name: String, lore: List<String>, enchant: Boolean): ItemStack {
            return create(ItemStack(material), name, lore, enchant)!!
        }

        /**
         * ДАЕТ ИГРОКУ КАСТОМНЫЙ ПРЕДМЕТ ПО ПРЕДМЕТУ
         * **/

        fun create(item: ItemStack, name: String, lore: List<String>, enchant: Boolean): ItemStack? {
            var meta: ItemMeta = item.itemMeta!!
            meta.setDisplayName(SText.getColor(name))
            if (meta.getLore() != null) {
                meta.getLore()!!.clear()
            }
            meta.setLore(SText.getColorList(lore))
            if (enchant) {
                meta.addEnchant(Enchantment.KNOCKBACK, 1, true)
            }
            meta.addItemFlags(*ItemFlag.values())
            item.setItemMeta(meta)
            return item;
        }

        /**
         * ПОЛУЧАЕТ ГОЛОВУ ИГРОКА И ДЕЛАЕТ ОПИСАНИЕ
         **/

        fun getHead(url: String, name: String, lore: List<String>, enchant: Boolean): ItemStack? {
            var url = "http://textures.minecraft.net/texture/".plus(url)
            var skull = ItemStack(Material.PLAYER_HEAD)
            var skullMeta = skull.getItemMeta()
            var profile = GameProfile(UUID.randomUUID(), null)
            var encodedData = Base64.encodeBase64(String.format("{textures:{SKIN:{url:\"%s\"}}}", url).toByteArray())
            profile.getProperties().put("textures", Property("textures", String(encodedData)))
            var profileField = skullMeta!!.javaClass.getDeclaredField("profile")
            profileField.setAccessible(true);
            profileField.set(skullMeta, profile)
            skull.setItemMeta(skullMeta);
            return create(skull, name, lore, enchant);
        }
    }
}
//        fun createTag(url: Any, name: String, lore: List<String>, enchant: Boolean, tagname: String, tag: String): ItemStack {
//            var item = when(url){
//                url is Material -> create(ItemStack(url as Material), name, lore, enchant)
//                url is String -> getHead(url as String, name, lore, false)
//                else -> create(url as ItemStack, name, lore, enchant)
//            }!!
//
//            Object obj = NMS_ULTIMATE.getItemStack(item);
//            Object nbt = NMS_ULTIMATE.getOrCreateTag_(obj);
//            NMS_ULTIMATE.setTag(nbt, tagname, tag);
//            NMS_ULTIMATE.setTag(obj, nbt);
//            return NMS_ULTIMATE.getItemBack(obj);
//        }
//
//
//        /**
//         * РАСШИФРОВЫВАЕТ ОПИСАНИЕ ПРЕДМЕТА
//         * **/
//
//        fun getTag(item: ItemStack, tag: String): String? {
//            var obj = NMS_ULTIMATE.getItemStack(item);
//
//            if(NMS_ULTIMATE.getTag_(obj) == null){
//                return null;
//            }
//
//            String result = NMS_ULTIMATE.getTagTag(obj, tag);
//
//            if(result == null || result.length() == 0){
//                return  null;
//            }
//
//            return result;
//        }
//
//        /**
//         * СТАВИТ БРОНЮ
//         * **/
//
//        fun setArmor(item: ItemStack, armor: Double): ItemStack{
//            var modifier = AttributeModifier(UUID.randomUUID(), "geretic.armor", armor, AttributeModifier.Operation.ADD_NUMBER);
//            var meta = item.itemMeta;
//            meta!!.addAttributeModifier(Attribute.GENERIC_ARMOR, modifier);
//            item.setItemMeta(meta);
//            return item;
//        }
//
//        /**
//         * КОЖАННАЯ БРОНЯ
//         **/
//
//        public static ItemStack getColorArmor(Color color, int slot) {
//
//            Material material = null;
//            switch (slot) {
//                case 1:
//                material = Material.LEATHER_HELMET;
//                break;
//                case 2:
//                material = Material.LEATHER_CHESTPLATE;
//                break;
//                case 3:
//                material = Material.LEATHER_LEGGINGS;
//                break;
//                case 4:
//                material = Material.LEATHER_BOOTS;
//                break;
//            }
//
//            ItemStack item = ItemCreate.create(material, " ", new ArrayList<>(), false);
//            LeatherArmorMeta meta = (LeatherArmorMeta) item.getItemMeta();
//            meta.setColor(color);
//            item.setItemMeta(meta);
//
//            return item;
//
//        }
//
//    }



