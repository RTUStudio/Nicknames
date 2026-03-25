package kr.rtustudio.nicknames;

import kr.rtustudio.configurate.model.ConfigPath;
import kr.rtustudio.framework.bukkit.api.RSPlugin;
import kr.rtustudio.framework.bukkit.api.core.provider.name.NameProvider;
import kr.rtustudio.nicknames.command.MainCommand;
import kr.rtustudio.nicknames.configuration.NameConfig;
import kr.rtustudio.nicknames.handler.PlayerJoinQuit;
import kr.rtustudio.nicknames.integration.NamePlaceholder;
import kr.rtustudio.nicknames.manager.NameManager;
import kr.rtustudio.nicknames.player.PlayerNameManager;
import lombok.Getter;
import org.bukkit.permissions.PermissionDefault;

public class NickNames extends RSPlugin {

    @Getter
    private static NickNames instance;

    @Getter
    private NameManager nameManager;

    @Getter
    private PlayerNameManager playerNameManager;

    @Getter
    private kr.rtustudio.nicknames.provider.NameProvider nameProvider;

    @Override
    protected void load() {
        instance = this;
    }

    @Override
    protected void enable() {
        registerStorage("Nickname");

        registerConfiguration(NameConfig.class, ConfigPath.of("Name"));

        nameManager = new NameManager(this);
        playerNameManager = new PlayerNameManager(this);

        nameProvider = new kr.rtustudio.nicknames.provider.NameProvider(this);
        setProvider(NameProvider.class, nameProvider);

        registerPermission("nickname.change", PermissionDefault.TRUE);
        registerPermission("nickname.change.other", PermissionDefault.OP);

        registerCommand(new MainCommand(this), true);

        registerEvent(new PlayerJoinQuit(this));

        registerIntegration(new NamePlaceholder(this));
    }
}
