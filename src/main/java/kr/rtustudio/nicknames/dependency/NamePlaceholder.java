package kr.rtustudio.nicknames.integration;

import kr.rtustudio.framework.bukkit.api.integration.wrapper.PlaceholderArgs;
import kr.rtustudio.framework.bukkit.api.integration.wrapper.PlaceholderWrapper;
import kr.rtustudio.nicknames.NickNames;
import kr.rtustudio.nicknames.player.PlayerName;
import kr.rtustudio.nicknames.player.PlayerNameManager;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

public class NamePlaceholder extends PlaceholderWrapper<NickNames> {

    private final PlayerNameManager pnm;

    public NamePlaceholder(NickNames plugin) {
        super(plugin);
        this.pnm = plugin.getPlayerNameManager();
    }

    @Override
    public String onRequest(OfflinePlayer offlinePlayer, PlaceholderArgs args) {
        if (args.isEmpty()) return null;

        if (args.equals(0, "display")) {
            PlayerName pn = pnm.getPlayer(offlinePlayer.getUniqueId());
            if (pn != null) {
                String name = pn.getName();
                if (name != null && !name.isEmpty()) return name;
            }
            if (offlinePlayer instanceof Player player) {
                if (player.getDisplayName().isEmpty()) return player.getName();
                return player.getDisplayName();
            }
            return offlinePlayer.getName();
        }

        PlayerName pn = pnm.getPlayer(offlinePlayer.getUniqueId());
        return pn == null ? null : pn.getName();
    }
}
