package me.shimado.simpleplugin;

import org.bukkit.configuration.file.YamlConfiguration;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SConfig {

    private static void update_func(String old_version, String new_version, Map<String, Object> data_config, Map<String, Object> data_messages) {

        Object version = config.get("Version");

        //old_version -> new_version

        if (version != null && (String.valueOf((Double) version)).equals(old_version)) {
            save_file_data(config_file, data_config, true, new_version);
            save_file_data(current_language, data_messages, false, new_version);
        }

    }

    /**
     * ЗАПИСЬ В ФАЙЛ
     **/

    private static void save_file_data(File file, Map<String, Object> data, boolean config_enable, String new_version) {

        try {
            List<String> lines = new ArrayList<>();
            Reader reader = new InputStreamReader(new FileInputStream(file), "UTF-8");

            new BufferedReader(reader).lines().forEach(l -> {
                if (config_enable && l != null && l.length() > 0 && l.split(":")[0].equals("Version")) {
                    lines.add("Version: " + new_version);
                } else {
                    lines.add(l);
                }
            });

            if (data.size() > 0) {

                lines.add(" ");

                for (Map.Entry<String, Object> a : data.entrySet()) {
                    lines.add(a.getKey() + ": " + a.getValue());
                }
            }

            reader.close();

            Writer writer = new OutputStreamWriter(new FileOutputStream(file), "UTF-8");

            for (String l : lines) {
                writer.write(l + "\r\n");
            }

            writer.close();

            if (config_enable) {
                config = YamlConfiguration.loadConfiguration(config_file);
            } else {
                messages = YamlConfiguration.loadConfiguration(current_language);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
