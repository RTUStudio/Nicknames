package kr.rtustudio.nicknames.provider;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;

import kr.rtustudio.nicknames.NickNames;
import kr.rtustudio.nicknames.player.PlayerName;
import kr.rtustudio.nicknames.player.PlayerNameManager;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
public class NameProvider implements kr.rtustudio.framework.bukkit.api.core.provider.name.NameProvider {

    private final NickNames plugin;
    private final PlayerNameManager pnm;

    public NameProvider(NickNames plugin) {
        this.plugin = plugin;
        this.pnm = plugin.getPlayerNameManager();
    }

    @Override
    public NickNames getPlugin() {
        return plugin;
    }

    public @NotNull List<String> names(Scope scope) {
        List<String> result = new ObjectArrayList<>();
        for (PlayerName player : pnm.getPlayers()) {
            if (scope == Scope.LOCAL) {
                if (player.getPlayer() == null) continue;
            }
            if (!player.getName().isEmpty()) {
                result.add("@" + player.getName());
            }
            result.add(player.getPlayer().getName());
        }
        return result;
    }

    public String getName(UUID uniqueId) {
        PlayerName pn = pnm.getPlayer(uniqueId);
        return pn == null ? null : pn.getName();
    }

    public UUID getUniqueId(String name) {
        PlayerName pn;
        if (name.startsWith("@")) {
            pn = pnm.getPlayer(name.substring(1));
        } else {
            Player player = Bukkit.getPlayer(name);
            if (player == null) return null;
            pn = pnm.getPlayer(player.getUniqueId());
        }
        return pn == null ? null : pn.getPlayer().getUniqueId();
    }
}