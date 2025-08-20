package com.phoenixpickaxes.commands;

import com.phoenixpickaxes.managers.SkinManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.List;

public class PhoenixTabCompleter implements TabCompleter {

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> suggestions = new ArrayList<>();

        if (args.length == 1) {
            // Primera palabra → sugerir subcomandos
            suggestions.add("reload");
            suggestions.add("setskin");
        } else if (args.length == 2 && args[0].equalsIgnoreCase("setskin")) {
            // Segunda palabra → sugerir skins desde SkinManager
            suggestions.addAll(SkinManager.getAllSkinIds());
        }

        return suggestions;
    }
}
