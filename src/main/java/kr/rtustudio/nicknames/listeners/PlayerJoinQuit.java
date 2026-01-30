package kr.rtustudio.nicknames.listeners;

import kr.rtustudio.framework.bukkit.api.listener.RSListener;
import kr.rtustudio.nicknames.NickNames;
import kr.rtustudio.nicknames.player.PlayerNameManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;

@SuppressWarnings("unused")
public class PlayerJoinQuit extends RSListener<NickNames> {

    private final PlayerNameManager pnm;

    public PlayerJoinQuit(NickNames plugin) {
        super(plugin);
        this.pnm = plugin.getPlayerNameManager();
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        pnm.addPlayer(e.getPlayer());
    }

}
