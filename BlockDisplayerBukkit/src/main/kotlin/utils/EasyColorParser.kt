package utils

import net.md_5.bungee.api.ChatColor
import java.util.regex.Pattern

val String.rainbow : String get() {
    val text = substring(20)

    var r = Integer.parseInt(substring(9, 11), 16)
    var g = Integer.parseInt(substring(12, 14), 16)
    var b = Integer.parseInt(substring(15, 17), 16)
    val amount = Integer.parseInt(substring(18, 20))

    fun getHex(): String {
        var str = "#"
        if (r < 16) {
            str += "0"
        }
        str += Integer.toHexString(if (r > 255) 255 else if (r < 0) 0 else r)
        if (g < 16) {
            str += "0"
        }
        str += Integer.toHexString(if (g > 255) 255 else if (g < 0) 0 else g)
        if (b < 16) {
            str += "0"
        }
        str += Integer.toHexString(if (b > 255) 255 else if (b < 0) 0 else b)
        return str
    }
    fun change(amount: Int) {
        if (r == 255 && g != 255 && b == 0) {
            g += amount
            if (g > 255) {
                g = 255
            }
        } else if (r != 0 && g == 255 && b == 0) {
            r -= amount
            if (r < 0) {
                r = 0
            }
        } else if (r == 0 && g == 255 && b != 255) {
            b += amount
            if (b > 255) {
                b = 255
            }
        } else if (r == 0 && g != 0 && b == 255) {
            g -= amount
            if (g < 0) {
                g = 0
            }
        } else if (r != 255 && g == 0 && b == 255) {
            r += amount
            if (r > 255) {
                r = 255
            }
        } else if (r == 255 && g == 0 && b != 0) {
            b -= amount
            if (b < 0) {
                b = 0
            }
        }
    }

    val str = StringBuffer()
    for (i in text.indices) {
        if (text[i] != ' ') {
            str.append(getHex())
            change(amount)
        }
        str.append(text[i])
    }
    return str.toString().t
}

val String.t : String get() {
    var s = this
    if (s.startsWith("&rainbow-")) {
        if (Pattern.compile("[0-9A-F]{2}-[0-9A-F]{2}-[0-9A-F]{2}-[0-9]{2}").matcher(s.substring(9, 20)).matches()) {
            return this.rainbow
        }
    }

    val rgb = Pattern.compile("#[0-9a-f]{6}").matcher(this)

    while (rgb.find()) {
        try {
            s = s.replaceFirst(rgb.group(), ChatColor.of(rgb.group()).toString())
        } catch (_: Exception) {}
    }

    val color = Pattern.compile("%[a-zA-Z_]*%").matcher(this)
    while (color.find()) {
        try {
            s = s.replaceFirst(
                color.group(),
                ChatColor.of(color.group().replace("%", "")).toString()
            )
        } catch (_: Exception) {}
    }
    return s
}