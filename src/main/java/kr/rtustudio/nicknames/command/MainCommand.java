package kr.rtustudio.nicknames.command;

import kr.rtustudio.framework.bukkit.api.command.CommandArgs;
import kr.rtustudio.framework.bukkit.api.command.RSCommand;
import kr.rtustudio.nicknames.NickNames;
import kr.rtustudio.nicknames.configuration.NameConfig;

public class MainCommand extends RSCommand<NickNames> {

    public MainCommand(NickNames plugin) {
        super(plugin, "nickname");
        registerCommand(new NickCommand(plugin));
    }

    @Override
    protected void reload(CommandArgs data) {
        plugin.reloadConfiguration(NameConfig.class);
    }
}
