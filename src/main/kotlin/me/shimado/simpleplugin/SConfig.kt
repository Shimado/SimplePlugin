package me.shimado.simpleplugin

import org.bukkit.configuration.file.YamlConfiguration
import java.io.BufferedReader
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.io.Reader

class SConfig {

    var config: YamlConfiguration? = null;
    var messages: YamlConfiguration? = null;

    var config_file: File? = null;
    var messages_file: File? = null;

    constructor(config: YamlConfiguration, messages: YamlConfiguration){
        this.config = config
        this.messages = messages
    }

    fun update_func(old_version: String, new_version: String, data_config: Map<String, Object>, data_messages: Map<String, Object>) {

        var version = config?.get("Version");

        //old_version -> new_version

        if (version != null && version.toString().equals(old_version)) {
            save_file_data(config_file!!, data_config, true, new_version);
            save_file_data(messages_file!!, data_messages, false, new_version);
        }

    }

    /**
     * ЗАПИСЬ В ФАЙЛ
     **/

    fun save_file_data(file: File, data: Map<String, Object>, config_enable: Boolean, new_version: String) {

        var lines = arrayListOf<String>();

        var reader: Reader = InputStreamReader(FileInputStream(file), "UTF-8")
        BufferedReader(reader).lines().forEach {
            if (config_enable && it != null && it.length > 0 && it.split(":")[0].equals("Version")) {
                lines.add("Version: $new_version");
            } else {
                lines.add(it);
            }
        }

        if (data.size > 0) {

            lines.add(" ")

            for((key, value) in data){
                lines.add("$key: $value")
            }

        }

        reader.close();

        var writer = OutputStreamWriter(FileOutputStream(file), "UTF-8");

        for (l in lines) {
            writer.write("$l\r\n");
        }

        writer.close();

        if (config_enable) {
            config = YamlConfiguration.loadConfiguration(config_file!!);
        } else {
            messages = YamlConfiguration.loadConfiguration(messages_file!!);
        }

    }

}