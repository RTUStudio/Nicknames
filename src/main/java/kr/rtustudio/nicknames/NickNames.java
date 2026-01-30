package kr.rtustudio.nicknames;

import kr.rtustudio.framework.bukkit.api.RSPlugin;
import kr.rtustudio.nicknames.command.MainCommand;
import kr.rtustudio.nicknames.configuration.NameConfig;
import kr.rtustudio.nicknames.dependency.NamePlaceholder;
import kr.rtustudio.nicknames.listeners.PlayerJoinQuit;
import kr.rtustudio.nicknames.manager.NameManager;
import kr.rtustudio.nicknames.player.PlayerNameManager;
import kr.rtustudio.nicknames.provider.NameProvider;
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
    private NameProvider nameProvider;

    @Override
    public void load() {
        instance = this;
    }

    @Override
    public void enable() {
        initStorage("Nickname");

        registerConfiguration(NameConfig.class, "Name");

        nameManager = new NameManager(this);
        playerNameManager = new PlayerNameManager(this);

        nameProvider = new NameProvider(this);
        getFramework().getProviders().setName(nameProvider);

        registerPermission(getPlugin().getName() + ".nickname.change", PermissionDefault.TRUE);
        registerPermission(getPlugin().getName() + ".nickname.change.other", PermissionDefault.OP);

        registerCommand(new MainCommand(this), true);

        registerEvent(new PlayerJoinQuit(this));

        registerIntegration(new NamePlaceholder(this));
    }
}
