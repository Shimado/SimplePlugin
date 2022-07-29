package me.shimado.simpleplugin

import com.mojang.authlib.GameProfile
import org.bukkit.Location
import org.bukkit.block.Block
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import java.lang.reflect.Constructor
import java.lang.reflect.Field
import java.lang.reflect.InvocationTargetException
import java.util.regex.Matcher
import java.util.regex.Pattern

class NMS {

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

///**
// * ВОЗВРАЩАЕТ БАККИТ СУЩЕСТВО
// **/
//
//public static Object getEntityBack(Object entity) {
//    try {
//        if (MainFunGames.version_name.equals("v1_17_R1") || MainFunGames.version_name.contains("v1_18") || MainFunGames.version_name.equals("v1_19_R1")) {
//            return getNMS_1_18("net.minecraft.world.entity.Entity").getMethod("getBukkitEntity").invoke(entity);
//        } else {
//            return getNMSclass("Entity").getMethod("getBukkitEntity").invoke(entity);
//        }
//    } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
//        e.printStackTrace();
//    }
//    return null;
//}
//
///**
// * ENTITYFIREWORK
// **/
//
//public static Object create_entity(Location loc, ItemStack firework) {
//    try {
//        if (MainFunGames.version_name.contains("v1_18") || MainFunGames.version_name.equals("v1_17_R1") || MainFunGames.version_name.equals("v1_19_R1")) {
//            Constructor<?> constructor = getNMS_1_18("net.minecraft.world.entity.projectile.EntityFireworks").getConstructor(
//                getNMS_1_18("net.minecraft.world.level.World"),
//                double.class,
//                        double.class,
//            double.class,
//            getNMS_1_18("net.minecraft.world.item.ItemStack"));
//            return constructor.newInstance(getWorld(loc), loc.getX(), loc.getY(), loc.getZ(), getItemStack(firework));
//        } else {
//            Constructor<?> constructor = getNMSclass("EntityFireworks").getConstructor(
//                getNMSclass("World"),
//                double.class,
//                        double.class,
//            double.class,
//            getNMSclass("ItemStack"));
//            return constructor.newInstance(getWorld(loc), loc.getX(), loc.getY(), loc.getZ(), getItemStack(firework));
//        }
//    } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
//        e.printStackTrace();
//    }
//    return null;
//}
//
///**
// * ENTITYFIREWORK
// **/
//
//public static void set_lifecycle(Object firework) {
//    try {
//        if (MainFunGames.version_name.contains("v1_18") || MainFunGames.version_name.equals("v1_17_R1") || MainFunGames.version_name.equals("v1_19_R1")) {
//            getNMS_1_18("net.minecraft.world.entity.projectile.EntityFireworks").getDeclaredField("f").setInt(firework, 0);
//        } else {
//            getNMSclass("EntityFireworks").getDeclaredField("expectedLifespan").setInt(firework, 0);
//        }
//    } catch (IllegalAccessException e) {
//        e.printStackTrace();
//    } catch (NoSuchFieldException e) {
//        e.printStackTrace();
//    }
//}
//
///**
// * ОТПРАВЛЯЕТ ПАКЕТ
// **/
//
//public static void sendPacket(Player player, Object packet) {
//    try {
//        if (MainFunGames.version_name.contains("v1_18") || MainFunGames.version_name.equals("v1_19_R1")) {
//            Object entityplayer = Class.forName("org.bukkit.craftbukkit." + MainFunGames.version_name + ".entity.CraftPlayer").getMethod("getHandle").invoke(player);
//            Object playerConnection = entityplayer.getClass().getField("b").get(entityplayer);
//            getNMS_1_18("net.minecraft.server.network.PlayerConnection").getMethod("a", getNMS_1_18("net.minecraft.network.protocol.Packet")).invoke(playerConnection, packet);
//        } else if (MainFunGames.version_name.equals("v1_17_R1")) {
//            Object entityplayer = Class.forName("org.bukkit.craftbukkit." + MainFunGames.version_name + ".entity.CraftPlayer").getMethod("getHandle").invoke(player);
//            Object playerConnection = entityplayer.getClass().getField("b").get(entityplayer);
//            getNMS_1_18("net.minecraft.server.network.PlayerConnection").getMethod("sendPacket", getNMS_1_18("net.minecraft.network.protocol.Packet")).invoke(playerConnection, packet);
//        } else {
//            Object entityplayer = Class.forName("org.bukkit.craftbukkit." + MainFunGames.version_name + ".entity.CraftPlayer").getMethod("getHandle").invoke(player);
//            Object playerConnection = entityplayer.getClass().getField("playerConnection").get(entityplayer);
//            getNMSclass("PlayerConnection").getMethod("sendPacket", getNMSclass("Packet")).invoke(playerConnection, packet);
//        }
//    } catch (Exception e) {
//        e.printStackTrace();
//    }
//}
//
///**
// * ПАКЕТ НА УНИЧТОЖЕНИЕ ENTITY
// **/
//
//public static Object PacketDestroy(int id) {
//    try {
//        Constructor<?> constructor;
//        if (MainFunGames.version_name.contains("v1_18") || MainFunGames.version_name.equals("v1_17_R1") || MainFunGames.version_name.equals("v1_19_R1")) {
//            constructor = getNMS_1_18("net.minecraft.network.protocol.game.PacketPlayOutEntityDestroy").getConstructor(int[].class);
//        } else {
//            constructor = getNMSclass("PacketPlayOutEntityDestroy").getConstructor(int[].class);
//        }
//        return constructor.newInstance(new int[]{id});
//    } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
//        e.printStackTrace();
//    }
//    return null;
//}
//
///**
// * ПАКЕТ НА СТАТУС
// **/
//
//public static Object PacketStatus(Object object, byte num) {
//    try {
//        Constructor<?> constructor;
//        if (MainFunGames.version_name.contains("v1_18") || MainFunGames.version_name.equals("v1_17_R1") || MainFunGames.version_name.equals("v1_19_R1")) {
//            constructor = getNMS_1_18("net.minecraft.network.protocol.game.PacketPlayOutEntityStatus")
//                .getConstructor(getNMS_1_18("net.minecraft.world.entity.Entity"), byte.class);
//        } else {
//            constructor = getNMSclass("PacketPlayOutEntityStatus").getConstructor(getNMSclass("Entity"), byte.class);
//        }
//        return constructor.newInstance(object, num);
//    } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
//        e.printStackTrace();
//    }
//    return null;
//}
//
///**
// * СПАВНИТ СОЗДАННЫЙ ENTITY
// **/
//
//public static Object PacketSpawn(Object object) {
//    try {
//        if (MainFunGames.version_name.contains("v1_18") || MainFunGames.version_name.equals("v1_17_R1") || MainFunGames.version_name.equals("v1_19_R1")) {
//            Constructor<?> constructor = getNMS_1_18("net.minecraft.network.protocol.game.PacketPlayOutSpawnEntity").getConstructor(getNMS_1_18("net.minecraft.world.entity.Entity"));
//            return constructor.newInstance(object);
//        } else if (MainFunGames.version_name.equals("v1_12_R1") || MainFunGames.version_name.contains("v1_13")) {
//            Constructor<?> constructor1 = getNMSclass("PacketPlayOutSpawnEntity").getConstructor(getNMSclass("Entity"), int.class);
//            return constructor1.newInstance(object, 76);
//        } else {
//            Constructor<?> constructor2 = getNMSclass("PacketPlayOutSpawnEntity").getConstructor(getNMSclass("Entity"));
//            return constructor2.newInstance(object);
//        }
//    } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
//        e.printStackTrace();
//    }
//    return null;
//}
//
///**
// * ПАКЕТ НА META ENTITY
// **/
//
//public static Object PacketPlayOutEntityMetadata(Object entity, Object watcher) {
//    try {
//        Constructor<?> constructor;
//        if (MainFunGames.version_name.contains("v1_18") || MainFunGames.version_name.equals("v1_17_R1") || MainFunGames.version_name.equals("v1_19_R1")) {
//            constructor = getNMS_1_18("net.minecraft.network.protocol.game.PacketPlayOutEntityMetadata").getConstructor(int.class, getNMS_1_18("net.minecraft.network.syncher.DataWatcher"), boolean.class);
//        } else {
//            constructor = getNMSclass("PacketPlayOutEntityMetadata").getConstructor(int.class, getNMSclass("DataWatcher"), boolean.class);
//        }
//        return constructor.newInstance(getEntityId(entity), watcher, false);
//    } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
//        e.printStackTrace();
//    }
//    return null;
//}
//
///**
// * ПОЛУЧЕНИЕ ПРЕДМЕТА, ЧТОБЫ ЗАТЕМ ИЗМЕНЯТЬ ТЭГ
// **/
//
//public static Object getItemStack(Object item) {
//    try {
//        if (MainFunGames.version_name.equals("v1_18_R2") || MainFunGames.version_name.equals("v1_19_R1")) {
//            return getNMS_1_18("org.bukkit.craftbukkit." + MainFunGames.version_name + ".inventory.CraftItemStack").getMethod("asNMSCopy", ItemStack.class).invoke(null, item);
//        } else {
//            return getCraftBukkitclass("CraftItemStack").getMethod("asNMSCopy", ItemStack.class).invoke(null, item);
//        }
//    } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
//        e.printStackTrace();
//    }
//    return null;
//}
//
///**
// * ВОЗВРАЩАЕТ ТЭГ ПРЕДМЕТА
// **/
//
//public static Object getTag_(Object item) {
//    try {
//        if(MainFunGames.version_name.equals("v1_19_R1")) {
//            return getNMS_1_18("net.minecraft.world.item.ItemStack").getMethod("u").invoke(item);
//        }else if (MainFunGames.version_name.equals("v1_18_R2")) {
//            return getNMS_1_18("net.minecraft.world.item.ItemStack").getMethod("t").invoke(item);
//        } else if (MainFunGames.version_name.equals("v1_18_R1")) {
//            return getNMS_1_18("net.minecraft.world.item.ItemStack").getMethod("s").invoke(item);
//        } else if (MainFunGames.version_name.equals("v1_17_R1")) {
//            return getNMS_1_18("net.minecraft.world.item.ItemStack").getMethod("getTag").invoke(item);
//        } else {
//            return getNMSclass("ItemStack").getMethod("getTag").invoke(item);
//        }
//    } catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
//        e.printStackTrace();
//    }
//    return null;
//}
//
///**
// * ПОЛУЧАЕТ ТЭГ, ЗАДАННЫЙ ДЛЯ БУМБОКСА
// **/
//
//public static String getTagTag(Object item, String tagname) {
//    try {
//        Object tag = getTag_(item);
//        if (MainFunGames.version_name.contains("v1_18") || MainFunGames.version_name.equals("v1_19_R1")) {
//            return (String) getNMS_1_18("net.minecraft.nbt.NBTTagCompound").getMethod("l", String.class).invoke(tag, tagname);
//        } else if (MainFunGames.version_name.equals("v1_17_R1")) {
//            return (String) getNMS_1_18("net.minecraft.nbt.NBTTagCompound").getMethod("getString", String.class).invoke(tag, tagname);
//        } else {
//            return (String) getNMSclass("NBTTagCompound").getMethod("getString", String.class).invoke(tag, tagname);
//        }
//    } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
//        e.printStackTrace();
//    }
//    return null;
//}
//
///**
// * ПОЛУЧАЕТ, ЛИБО СОЗДАЕТ ТЭГ
// **/
//
//public static Object getOrCreateTag_(Object item) {
//    try {
//        if(MainFunGames.version_name.equals("v1_19_R1")){
//            return getNMS_1_18("net.minecraft.world.item.ItemStack").getMethod("v").invoke(item);
//        }else if (MainFunGames.version_name.equals("v1_18_R2")) {
//            return getNMS_1_18("net.minecraft.world.item.ItemStack").getMethod("u").invoke(item);
//        } else if (MainFunGames.version_name.equals("v1_18_R1")) {
//            return getNMS_1_18("net.minecraft.world.item.ItemStack").getMethod("t").invoke(item);
//        } else if (MainFunGames.version_name.equals("v1_17_R1")) {
//            return getNMS_1_18("net.minecraft.world.item.ItemStack").getMethod("getOrCreateTag").invoke(item);
//        } else if (MainFunGames.version_name.equals("v1_12_R1")) {
//            Object object = getNMSclass("ItemStack").getMethod("getTag").invoke(item);
//            try {
//                return object == null ? getNMSclass("NBTTagCompound").getConstructor().newInstance() : object;
//            } catch (InstantiationException e) {
//                e.printStackTrace();
//            }
//        } else {
//            return getNMSclass("ItemStack").getMethod("getOrCreateTag").invoke(item);
//        }
//    } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
//        e.printStackTrace();
//    }
//    return null;
//}
//
///**
// * ЗАДАЕТ ТЭГ ПОД БУМБОКС
// **/
//
//public static void setTag(Object item, String tagname, String tag) {
//    try {
//        if (MainFunGames.version_name.contains("v1_18") || MainFunGames.version_name.equals("v1_19_R1")) {
//            getNMS_1_18("net.minecraft.nbt.NBTTagCompound").getMethod("a", String.class, String.class).invoke(item, tagname, tag);
//        } else if (MainFunGames.version_name.equals("v1_17_R1")) {
//            getNMS_1_18("net.minecraft.nbt.NBTTagCompound").getMethod("setString", String.class, String.class).invoke(item, tagname, tag);
//        } else {
//            getNMSclass("NBTTagCompound").getMethod("setString", String.class, String.class).invoke(item, tagname, tag);
//        }
//    } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
//        e.printStackTrace();
//    }
//}
//
///**
// * СТАВИТ ТЭГ НА ПРЕДМЕТ
// **/
//
//public static void setTag(Object item, Object comp) {
//    try {
//        if (MainFunGames.version_name.contains("v1_18") || MainFunGames.version_name.equals("v1_19_R1")) {
//            getNMS_1_18("net.minecraft.world.item.ItemStack").getMethod("c", getNMS_1_18("net.minecraft.nbt.NBTTagCompound")).invoke(item, comp);
//        } else if (MainFunGames.version_name.equals("v1_17_R1")) {
//            getNMS_1_18("net.minecraft.world.item.ItemStack").getMethod("setTag", getNMS_1_18("net.minecraft.nbt.NBTTagCompound")).invoke(item, comp);
//        } else {
//            getNMSclass("ItemStack").getMethod("setTag", getNMSclass("NBTTagCompound")).invoke(item, comp);
//        }
//    } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
//        e.printStackTrace();
//    }
//}
//
///**
// * ВОЗВРАЩАЕТ ПРЕДМЕТ С ИЗМЕНЕННЫМ ТЭГОМ
// **/
//
//public static ItemStack getItemBack(Object item) {
//    try {
//        if (MainFunGames.version_name.equals("v1_18_R2") || MainFunGames.version_name.equals("v1_19_R1")) {
//            return (ItemStack) (getNMS_1_18("org.bukkit.craftbukkit." + MainFunGames.version_name + ".inventory.CraftItemStack").getMethod("asCraftMirror", getNMS_1_18("net.minecraft.world.item.ItemStack")).invoke(null, item));
//        } else if (MainFunGames.version_name.equals("v1_18_R1") || MainFunGames.version_name.equals("v1_17_R1")) {
//            return (ItemStack) (getCraftBukkitclass("CraftItemStack").getMethod("asCraftMirror", getNMS_1_18("net.minecraft.world.item.ItemStack")).invoke(null, item));
//        } else {
//            return (ItemStack) (getCraftBukkitclass("CraftItemStack").getMethod("asCraftMirror", getNMSclass("ItemStack")).invoke(null, item));
//        }
//    } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
//        e.printStackTrace();
//    }
//    return null;
//}
//
//
///**
// * DATAWATCHER АРМОРСТЕНДА
// **/
//
//public static Object getDataWatcher(Object stand) {
//    try {
//        if (MainFunGames.version_name.equals("v1_18_R2") || MainFunGames.version_name.equals("v1_19_R1")) {
//            Field field = getNMS_1_18("net.minecraft.world.entity.Entity").getDeclaredField("Y");
//            field.setAccessible(true);
//            return field.get(stand);
//        } else if (MainFunGames.version_name.equals("v1_18_R1")) {
//            Field field = getNMS_1_18("net.minecraft.world.entity.Entity").getDeclaredField("Z");
//            field.setAccessible(true);
//            return field.get(stand);
//        } else if (MainFunGames.version_name.equals("v1_17_R1")) {
//            Field field = getNMS_1_18("net.minecraft.world.entity.Entity").getDeclaredField("Y");
//            field.setAccessible(true);
//            return field.get(stand);
//        } else {
//            return getNMSclass("Entity").getMethod("getDataWatcher").invoke(stand);
//        }
//    } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException | NoSuchFieldException e) {
//        e.printStackTrace();
//    }
//    return null;
//}
//
///**
// * TileEntitySkull
// **/
//
//public static void getTileEntitySkull(Block block, GameProfile profile) {
//    Object a = getWorld(block.getLocation());
//    try {
//        if (MainFunGames.version_name.contains("v1_18") || MainFunGames.version_name.equals("v1_19_R1")) {
//            Object b = getNMS_1_18("net.minecraft.world.level.World").getMethod("getBlockEntity", getNMS_1_18("net.minecraft.core.BlockPosition"), boolean.class).invoke(a, getBlockPosition(block), true);
//            getNMS_1_18("net.minecraft.world.level.block.entity.TileEntitySkull").getMethod("a", GameProfile.class).invoke(b, profile);
//        } else if (MainFunGames.version_name.equals("v1_17_R1")) {
//            Object b = getNMS_1_18("net.minecraft.world.level.World").getMethod("getTileEntity", getNMS_1_18("net.minecraft.core.BlockPosition")).invoke(a, getBlockPosition(block));
//            getNMS_1_18("net.minecraft.world.level.block.entity.TileEntitySkull").getMethod("setGameProfile", GameProfile.class).invoke(b, profile);
//        } else {
//            Object b = getNMSclass("WorldServer").getMethod("getTileEntity", getNMSclass("BlockPosition")).invoke(a, getBlockPosition(block));
//            getNMSclass("TileEntitySkull").getMethod("setGameProfile", GameProfile.class).invoke(b, profile);
//        }
//    } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
//        e.printStackTrace();
//    }
//}
//
///**
// * BlockPosition
// **/
//
//public static Object getBlockPosition(Block block) {
//    try {
//        Constructor<?> constructor;
//        if (MainFunGames.version_name.contains("v1_18") || MainFunGames.version_name.equals("v1_17_R1") || MainFunGames.version_name.equals("v1_19_R1")) {
//            constructor = getNMS_1_18("net.minecraft.core.BlockPosition").getConstructor(int.class, int.class, int.class);
//        } else {
//            constructor = getNMSclass("BlockPosition").getConstructor(int.class, int.class, int.class);
//        }
//        return constructor.newInstance(block.getX(), block.getY(), block.getZ());
//    } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException | InstantiationException e) {
//        e.printStackTrace();
//    }
//
//    return null;
//}

}