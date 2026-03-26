package kr.rtustudio.nicknames.command;

import kr.rtustudio.framework.bukkit.api.command.CommandArgs;
import kr.rtustudio.framework.bukkit.api.command.RSCommand;
import kr.rtustudio.nicknames.NickNames;
import kr.rtustudio.nicknames.player.PlayerName;
import kr.rtustudio.nicknames.player.PlayerNameManager;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.UUID;

public class NickCommand extends RSCommand<NickNames> {

    private final PlayerNameManager playerNameManager;

    public NickCommand(NickNames plugin) {
        super(plugin, "change");
        this.playerNameManager = plugin.getPlayerNameManager();
    }

    @Override
    protected Result execute(CommandArgs data) {
        if (data.length(2)) {
            if (player() == null) return Result.ONLY_PLAYER;
            if (!hasPermission("nickname.change")) return Result.FAILURE;

            PlayerName playerName = playerNameManager.getPlayer(player().getUniqueId());
            playerName.changeName(player(), data.get(1));
            return Result.SUCCESS;
        }

        if (data.length(3) && hasPermission("nickname.change.other")) {
            UUID targetUuid = provider().getUniqueId(data.get(2));
            if (targetUuid == null) return Result.NOT_FOUND_ONLINE_PLAYER;
            Player target = plugin.getServer().getPlayer(targetUuid);
            PlayerName playerName = playerNameManager.getPlayer(targetUuid);
            playerName.changeName(player(), target, data.get(1));
            return Result.SUCCESS;
        }

        return Result.FAILURE;
    }

    @Override
    protected List<String> tabComplete(CommandArgs data) {
        if (data.length(3) && hasPermission("nickname.change.other")) {
            return provider().names();
        }
        return List.of();
    }
}
