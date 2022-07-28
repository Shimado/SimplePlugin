package me.shimado.simpleplugin

import org.bukkit.Color
import org.bukkit.Location
import org.bukkit.Particle
import org.bukkit.Particle.DustOptions
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import java.io.IOException
import java.util.*

class SFunc {

    /**
     * ВОЗВРАЩАЕТ СЛУЧАНОЕ ЧИСЛО
     * @return min => x > max
     */

    fun random_int(min: Int, max: Int): Int {
        return min + Math.floor(Math.random() * (max - min)).toInt()
    }

    fun random_double(min: Double, max: Double): Double {
        return min + Math.floor(Math.random() * (max - min)).toInt()
    }

    /**
     * ДЕЛАЕТ КРАСИВОЕ ЧИСЛО
     */
    fun getIntNumber(number: Double): String? {
        val chars = number.toString().replace(",", ".").split("\\.".toRegex()).dropLastWhile { it.isEmpty() }
            .toTypedArray()
        return if (chars.size == 3 && !chars[2].startsWith("0")) {
            number.toString()
        } else {
            chars[0]
        }
    }

    /**
     * АНИМАЦИЯ ЧАСТИЦ
     */

    fun color_dust(loc: Location, a: Double, b: Double, c: Double, color: Color) {
        loc.world!!.spawnParticle(Particle.REDSTONE, loc.x, loc.y, loc.z, 0, a, b, c, DustOptions(color!!, 2f))
    }

    /**
     * СПАВНИТ В РАДИУСЕ
     */

    fun randomInRadius(loc: Location, radius: Double): Location {
        return loc.clone().add(Math.random() * if (random_int(0, 2) == 1) radius else -radius,
                               Math.random() * if (random_int(0, 2) == 1) radius else -radius,
                               Math.random() * if (random_int(0, 2) == 1) radius else -radius)
    }


    fun getValue(instance: Any, name: String): Any? {
        var result: Any? = null
        try {
            val field = instance.javaClass.getDeclaredField(name)
            field.isAccessible = true
            result = field[instance]
            field.isAccessible = false
        } catch (e: IOException) {
            return result;
        }
        return result;
    }

}