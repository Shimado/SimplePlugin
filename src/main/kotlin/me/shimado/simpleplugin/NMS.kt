package me.shimado.simpleplugin

import com.mojang.authlib.GameProfile
import org.bukkit.Location
import org.bukkit.block.Block
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import java.lang.reflect.Field

class NMS {

    companion object{

        /**
         * ПОЛУЧАЕТ НАЗВАНИЕ КЛАССА НЕПОСРЕДСТВЕННО NMS
         */

        fun getNMSclass(param_name: String): Class<*>? {
            return when(param_name.replace("(?:v1_)", "").replace(".{3}$", "").toInt()){
                in 12..16 -> Class.forName("net.minecraft.server." + SimplePlugin.version + "." + param_name);
                else -> Class.forName(param_name);
            }
        }

        /**
         * ДЛЯ 1.12.2 - 1.18.1 - ПОЛУЧАЕТ НАЗВАНИЕ КЛАССА БАККИТ.
         */

        fun getCraftBukkitclass(param_name: String): Class<*>?  = Class.forName("org.bukkit.craftbukkit." + SimplePlugin.version + ".inventory." + param_name)


        /**
         * ВОЗВРАЩАЕТ ID ENTITY
         */

        fun getEntityId(entity: Any?): Int {
            var field: Field = when(SimplePlugin.version){
                "v1_19_R1", "v1_18_R2", "v1_18_R1" -> getNMSclass("net.minecraft.world.entity.Entity")!!.getDeclaredField("at")
                "v1_17_R1" -> getNMSclass("net.minecraft.world.entity.Entity")!!.getDeclaredField("as")
                else -> getNMSclass("Entity")!!.getDeclaredField("id")
            }
            field.isAccessible = true
            return field[entity] as Int
        }

        /**
         * ВОЗВРАЩАЕТ МИР, ГДЕ ИГРОК
         */

        fun getWorld(loc: Location): Any? = loc.world!!.javaClass.getMethod("getHandle").invoke(loc.world)

        /**
         * ДОБАВЛЯЕТ В МИР СУЩЕСТВО
         */

        fun addEntity(loc: Location, entity: Any?) = when(SimplePlugin.version){
            "v1_19_R1", "v1_18_R2" -> getNMSclass("net.minecraft.server.level.WorldServer")!!.getMethod("b", getNMSclass("net.minecraft.world.entity.Entity")).invoke(getWorld(loc), entity)
            "v1_18_R1" -> getNMSclass("net.minecraft.server.level.WorldServer")!!.getMethod("addFreshEntity", getNMSclass("net.minecraft.world.entity.Entity")).invoke(getWorld(loc), entity)
            "v1_17_R1" -> getNMSclass("net.minecraft.server.level.WorldServer")!!.getMethod("addEntity", getNMSclass("net.minecraft.world.entity.Entity")).invoke(getWorld(loc), entity)
            else -> getNMSclass("WorldServer")!!.getMethod("addEntity", getNMSclass("Entity")).invoke(getWorld(loc), entity)
        }

        /**
         * ВОЗВРАЩАЕТ БАККИТ СУЩЕСТВО
         **/

        fun getEntityBack(entity: Any): Any = when(SimplePlugin.version) {
            "v1_19_R1", "v1_18_R2", "v1_18_R1", "v1_17_R1" -> getNMSclass("net.minecraft.world.entity.Entity")!!.getMethod("getBukkitEntity").invoke(entity)
            else -> getNMSclass("Entity")!!.getMethod("getBukkitEntity").invoke(entity)
        }

        /**
         * ENTITYFIREWORK
         **/

        fun create_entity(loc: Location, firework: ItemStack): Any = when(SimplePlugin.version) {
            "v1_19_R1", "v1_18_R2", "v1_18_R1", "v1_17_R1" -> getNMSclass("net.minecraft.world.entity.projectile.EntityFireworks")!!
                .getConstructor(getNMSclass("net.minecraft.world.level.World"), Double.javaClass, Double.javaClass, Double.javaClass, getNMSclass("net.minecraft.world.item.ItemStack"))
                .newInstance(getWorld(loc), loc.getX(), loc.getY(), loc.getZ(), getItemStack(firework))
            else -> getNMSclass("EntityFireworks")!!
                .getConstructor(getNMSclass("World"), Double.javaClass, Double.javaClass, Double.javaClass, getNMSclass("ItemStack"))
                .newInstance(getWorld(loc), loc.getX(), loc.getY(), loc.getZ(), getItemStack(firework))
        }


        /**
         * ENTITYFIREWORK
         **/

        fun set_lifecycle(firework: Any) = when(SimplePlugin.version) {
            "v1_19_R1", "v1_18_R2", "v1_18_R1", "v1_17_R1" -> getNMSclass("net.minecraft.world.entity.projectile.EntityFireworks")!!.getDeclaredField("f").setInt(firework, 0)
            else -> getNMSclass("EntityFireworks")!!.getDeclaredField("expectedLifespan").setInt(firework, 0);
        }


        /**
         * ОТПРАВЛЯЕТ ПАКЕТ
         **/

        fun sendPacket(player: Player, packet: Any) {
            when(SimplePlugin.version) {
                "v1_19_R1", "v1_18_R2", "v1_18_R1" -> {
                    var entityplayer = Class.forName("org.bukkit.craftbukkit." + SimplePlugin.version + ".entity.CraftPlayer").getMethod("getHandle").invoke(player)
                    var playerConnection = entityplayer.javaClass.getField("b").get(entityplayer)
                    getNMSclass("net.minecraft.server.network.PlayerConnection")!!.getMethod("a", getNMSclass("net.minecraft.network.protocol.Packet")).invoke(playerConnection, packet)
                }
                "v1_17_R1" -> {
                    var entityplayer = Class.forName("org.bukkit.craftbukkit." + SimplePlugin.version + ".entity.CraftPlayer").getMethod("getHandle").invoke(player);
                    var playerConnection = entityplayer.javaClass.getField("b").get(entityplayer);
                    getNMSclass("net.minecraft.server.network.PlayerConnection")!!.getMethod("sendPacket", getNMSclass("net.minecraft.network.protocol.Packet")).invoke(playerConnection, packet);
                }
                else -> {
                    var entityplayer = Class.forName("org.bukkit.craftbukkit." + SimplePlugin.version + ".entity.CraftPlayer").getMethod("getHandle").invoke(player);
                    var playerConnection = entityplayer.javaClass.getField("playerConnection").get(entityplayer);
                    getNMSclass("PlayerConnection")!!.getMethod("sendPacket", getNMSclass("Packet")).invoke(playerConnection, packet);
                }
            }
        }

        /**
         * ПАКЕТ НА УНИЧТОЖЕНИЕ ENTITY
         **/

        fun PacketDestroy(id: Int): Any = when(SimplePlugin.version) {
            "v1_19_R1", "v1_18_R2", "v1_18_R1", "v1_17_R1" -> getNMSclass("net.minecraft.network.protocol.game.PacketPlayOutEntityDestroy")!!.getConstructor(IntArray::class.java).newInstance(intArrayOf(id))
            else -> getNMSclass("PacketPlayOutEntityDestroy")!!.getConstructor(IntArray::class.java).newInstance(intArrayOf(id))
        }


        /**
         * ПАКЕТ НА СТАТУС
         **/

        fun PacketStatus(obj: Any, num: Byte): Any = when(SimplePlugin.version) {
            "v1_19_R1", "v1_18_R2", "v1_18_R1", "v1_17_R1" -> getNMSclass("net.minecraft.network.protocol.game.PacketPlayOutEntityStatus")!!.getConstructor(getNMSclass("net.minecraft.world.entity.Entity"), Byte.javaClass).newInstance(obj, num)
            else -> getNMSclass("PacketPlayOutEntityStatus")!!.getConstructor(getNMSclass("Entity"), Byte.javaClass).newInstance(obj, num)
        }


        /**
         * СПАВНИТ СОЗДАННЫЙ ENTITY
         **/

        fun PacketSpawn(obj: Any): Any = when(SimplePlugin.version){
            "v1_19_R1", "v1_18_R2", "v1_18_R1", "v1_17_R1" -> getNMSclass("net.minecraft.network.protocol.game.PacketPlayOutSpawnEntity")!!.getConstructor(getNMSclass("net.minecraft.world.entity.Entity")).newInstance(obj)
            "v1_12_R1", "v1_13_R1", "v1_13_R2" -> getNMSclass("PacketPlayOutSpawnEntity")!!.getConstructor(getNMSclass("Entity"), Int.javaClass).newInstance(obj, 76)
            else -> getNMSclass("PacketPlayOutSpawnEntity")!!.getConstructor(getNMSclass("Entity")).newInstance(obj)
        }


        /**
         * ПАКЕТ НА META ENTITY
         **/

        fun PacketPlayOutEntityMetadata(entity: Any, watcher: Any): Any = when(SimplePlugin.version) {
            "v1_19_R1", "v1_18_R2", "v1_18_R1", "v1_17_R1" -> getNMSclass("net.minecraft.network.protocol.game.PacketPlayOutEntityMetadata")!!.getConstructor(Int.javaClass, getNMSclass("net.minecraft.network.syncher.DataWatcher"), Boolean.javaClass).newInstance(getEntityId(entity), watcher, false)
            else -> getNMSclass("PacketPlayOutEntityMetadata")!!.getConstructor(Int.javaClass, getNMSclass("DataWatcher"), Boolean.javaClass).newInstance(getEntityId(entity), watcher, false)
        }


        /**
         * ПОЛУЧЕНИЕ ПРЕДМЕТА, ЧТОБЫ ЗАТЕМ ИЗМЕНЯТЬ ТЭГ
         **/

        fun getItemStack(item: Any): Any = when(SimplePlugin.version) {
            "v1_19_R1", "v1_18_R2", "v1_18_R1" -> getNMSclass("org.bukkit.craftbukkit." + SimplePlugin.version + ".inventory.CraftItemStack")!!.getMethod("asNMSCopy", ItemStack::class.java).invoke(null, item)
            else -> getCraftBukkitclass("CraftItemStack")!!.getMethod("asNMSCopy", ItemStack::class.java).invoke(null, item)
        }


        /**
         * ВОЗВРАЩАЕТ ТЭГ ПРЕДМЕТА
         **/

        fun getTag_(item: Any): Any = when(SimplePlugin.version) {
            "v1_19_R1" -> getNMSclass("net.minecraft.world.item.ItemStack")!!.getMethod("u").invoke(item)
            "v1_18_R2" -> getNMSclass("net.minecraft.world.item.ItemStack")!!.getMethod("t").invoke(item)
            "v1_18_R1" -> getNMSclass("net.minecraft.world.item.ItemStack")!!.getMethod("s").invoke(item)
            "v1_17_R1" -> getNMSclass("net.minecraft.world.item.ItemStack")!!.getMethod("getTag").invoke(item)
            else -> getNMSclass("ItemStack")!!.getMethod("getTag").invoke(item)
        }

        /**
         * ПОЛУЧАЕТ ТЭГ, ЗАДАННЫЙ ДЛЯ БУМБОКСА
         **/

        fun getTagTag(item: Any, tagname: String): String {
            var tag = getTag_(item)
            return when(SimplePlugin.version) {
                "v1_19_R1", "v1_18_R2", "v1_18_R1" -> getNMSclass("net.minecraft.nbt.NBTTagCompound")!!.getMethod("l", String.javaClass).invoke(tag, tagname).toString()
                "v1_17_R1" -> getNMSclass("net.minecraft.nbt.NBTTagCompound")!!.getMethod("getString", String.javaClass).invoke(tag, tagname).toString()
                else -> getNMSclass("NBTTagCompound")!!.getMethod("getString", String.javaClass).invoke(tag, tagname).toString()
            }
        }

        /**
         * ПОЛУЧАЕТ, ЛИБО СОЗДАЕТ ТЭГ
         **/

        fun getOrCreateTag_(item: Any): Any = when(SimplePlugin.version) {
            "v1_19_R1" -> getNMSclass("net.minecraft.world.item.ItemStack")!!.getMethod("v").invoke(item)
            "v1_18_R2" -> getNMSclass("net.minecraft.world.item.ItemStack")!!.getMethod("u").invoke(item)
            "v1_18_R1" -> getNMSclass("net.minecraft.world.item.ItemStack")!!.getMethod("t").invoke(item)
            "v1_17_R1" -> getNMSclass("net.minecraft.world.item.ItemStack")!!.getMethod("getOrCreateTag").invoke(item)
            "v1_12_R1" -> {
                var obj = getNMSclass("ItemStack")!!.getMethod("getTag").invoke(item)
                if (obj == null) getNMSclass("NBTTagCompound")!!.getConstructor().newInstance() else obj;
            }
            else -> getNMSclass("ItemStack")!!.getMethod("getOrCreateTag").invoke(item)
        }

        /**
         * ЗАДАЕТ ТЭГ ПОД БУМБОКС
         **/

        fun setTag(item: Any, tagname: String, tag: String) = when(SimplePlugin.version) {
            "v1_19_R1", "v1_18_R2", "v1_18_R1" -> getNMSclass("net.minecraft.nbt.NBTTagCompound")!!.getMethod("a", String.javaClass, String.javaClass).invoke(item, tagname, tag)
            "v1_17_R1" -> getNMSclass("net.minecraft.nbt.NBTTagCompound")!!.getMethod("setString", String.javaClass, String.javaClass).invoke(item, tagname, tag)
            else -> getNMSclass("NBTTagCompound")!!.getMethod("setString", String.javaClass, String.javaClass).invoke(item, tagname, tag)
        }


        /**
         * СТАВИТ ТЭГ НА ПРЕДМЕТ
         **/

        fun setTag(item: Any, comp: Any) = when(SimplePlugin.version) {
            "v1_19_R1", "v1_18_R2", "v1_18_R1" -> getNMSclass("net.minecraft.world.item.ItemStack")!!.getMethod("c", getNMSclass("net.minecraft.nbt.NBTTagCompound")).invoke(item, comp)
            "v1_17_R1" -> getNMSclass("net.minecraft.world.item.ItemStack")!!.getMethod("setTag", getNMSclass("net.minecraft.nbt.NBTTagCompound")).invoke(item, comp)
            else -> getNMSclass("ItemStack")!!.getMethod("setTag", getNMSclass("NBTTagCompound")).invoke(item, comp)
        }


        /**
         * ВОЗВРАЩАЕТ ПРЕДМЕТ С ИЗМЕНЕННЫМ ТЭГОМ
         **/

        fun getItemBack(item: Any): ItemStack = when(SimplePlugin.version) {
            "v1_19_R1", "v1_18_R2" -> getNMSclass("org.bukkit.craftbukkit." + SimplePlugin.version + ".inventory.CraftItemStack")!!.getMethod("asCraftMirror", getNMSclass("net.minecraft.world.item.ItemStack")).invoke(null, item) as ItemStack
            "v1_18_R1", "v1_17_R1" -> getCraftBukkitclass("CraftItemStack")!!.getMethod("asCraftMirror", getNMSclass("net.minecraft.world.item.ItemStack")).invoke(null, item) as ItemStack
            else -> getCraftBukkitclass("CraftItemStack")!!.getMethod("asCraftMirror", getNMSclass("ItemStack")).invoke(null, item) as ItemStack
        }

        /**
         * DATAWATCHER АРМОРСТЕНДА
         **/

        fun getDataWatcher(stand: Any): Any {
            var field: Field? = when(SimplePlugin.version) {
                "v1_19_R1", "v1_18_R2", "v1_17_R1" -> getNMSclass("net.minecraft.world.entity.Entity")!!.getDeclaredField("Y")
                "v1_18_R1" -> getNMSclass("net.minecraft.world.entity.Entity")!!.getDeclaredField("Z")
                else -> null
            }
            field
            if(field != null){
                field.isAccessible = true
                return field.get(stand)
            }else{
                return getNMSclass("Entity")!!.getMethod("getDataWatcher").invoke(stand)
            }
        }

        /**
         * TileEntitySkull
         **/

        fun getTileEntitySkull(block: Block, profile: GameProfile) {
            var a = getWorld(block.getLocation())
            when (SimplePlugin.version) {
                "v1_19_R1", "v1_18_R2", "v1_18_R1" -> {
                    var b = getNMSclass("net.minecraft.world.level.World")!!.getMethod("getBlockEntity", getNMSclass("net.minecraft.core.BlockPosition"), Boolean.javaClass).invoke(a, getBlockPosition(block), true)
                    getNMSclass("net.minecraft.world.level.block.entity.TileEntitySkull")!!.getMethod("a", GameProfile::class.java).invoke(b, profile)
                }
                "v1_17_R1" -> {
                    var b = getNMSclass("net.minecraft.world.level.World")!!.getMethod("getTileEntity", getNMSclass("net.minecraft.core.BlockPosition")).invoke(a, getBlockPosition(block))
                    getNMSclass("net.minecraft.world.level.block.entity.TileEntitySkull")!!.getMethod("setGameProfile", GameProfile::class.java).invoke(b, profile);
                }
                else -> {
                    var b = getNMSclass("WorldServer")!!.getMethod("getTileEntity", getNMSclass("BlockPosition")).invoke(a, getBlockPosition(block));
                    getNMSclass("TileEntitySkull")!!.getMethod("setGameProfile", GameProfile::class.java).invoke(b, profile)
                }
            }
        }

        /**
         * BlockPosition
         **/

        fun getBlockPosition(block: Block): Any = when(SimplePlugin.version) {
            "v1_19_R1", "v1_18_R2", "v1_18_R1", "v1_17_R1" -> getNMSclass("net.minecraft.core.BlockPosition")!!.getConstructor(Int.javaClass, Int.javaClass, Int.javaClass).newInstance(block.getX(), block.getY(), block.getZ())
            else -> getNMSclass("BlockPosition")!!.getConstructor(Int.javaClass, Int.javaClass, Int.javaClass).newInstance(block.getX(), block.getY(), block.getZ())
        }

    }

}