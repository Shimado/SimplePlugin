package me.shimado.simpleplugin

import com.mojang.authlib.GameProfile
import com.mojang.authlib.properties.Property
import org.apache.commons.codec.binary.Base64
import org.bukkit.Color
import org.bukkit.Material
import org.bukkit.attribute.Attribute
import org.bukkit.attribute.AttributeModifier
import org.bukkit.enchantments.Enchantment
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta
import org.bukkit.inventory.meta.LeatherArmorMeta
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


        /**
         * СОЗДАЕТ ПРЕДМЕТ С ТЕГОМ
         * **/

        fun createTag(url: Any, name: String, lore: List<String>, enchant: Boolean, tagname: String, tag: String): ItemStack {
            var item = when(url){
                url is Material -> create(ItemStack(url as Material), name, lore, enchant)
                url is String -> getHead(url as String, name, lore, false)
                else -> create(url as ItemStack, name, lore, enchant)
            }!!

            var obj = NMS.getItemStack(item);
            var nbt = NMS.getOrCreateTag_(obj);
            NMS.setTag(nbt, tagname, tag);
            NMS.setTag(obj, nbt);
            return NMS.getItemBack(obj);
        }


        /**
         * РАСШИФРОВЫВАЕТ ОПИСАНИЕ ПРЕДМЕТА
         * **/

        fun getTag(item: ItemStack, tag: String): String? {
            var obj = NMS.getItemStack(item)

            if(NMS.getTag_(obj) == null){
                return null
            }

            return NMS.getTagTag(obj, tag)
        }


        /**
         * СТАВИТ БРОНЮ
         * **/

        fun setArmor(item: ItemStack, armor: Double): ItemStack{
            var modifier = AttributeModifier(UUID.randomUUID(), "geretic.armor", armor, AttributeModifier.Operation.ADD_NUMBER);
            var meta = item.itemMeta;
            meta!!.addAttributeModifier(Attribute.GENERIC_ARMOR, modifier);
            item.setItemMeta(meta);
            return item;
        }


        /**
         * КОЖАННАЯ БРОНЯ
         **/

        fun getColorArmor(color: Color, slot: Int): ItemStack {

            var item = SItem.create(
                when(slot){
                    1 -> Material.LEATHER_HELMET
                    2 -> Material.LEATHER_CHESTPLATE
                    3 -> Material.LEATHER_LEGGINGS
                    4 -> Material.LEATHER_BOOTS
                    else -> Material.LEATHER_CHESTPLATE
                }, " ", arrayListOf(), false);
            var meta = item.getItemMeta() as LeatherArmorMeta;
            meta.setColor(color);
            item.setItemMeta(meta);

            return item;

        }

    }

}





