package kr.rtustudio.nicknames.handler;

import kr.rtustudio.framework.bukkit.api.listener.RSListener;
import kr.rtustudio.nicknames.NickNames;
import kr.rtustudio.nicknames.player.PlayerNameManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

@SuppressWarnings("unused")
public class PlayerJoinQuit extends RSListener<NickNames> {

    private final PlayerNameManager pnm;

    public PlayerJoinQuit(NickNames plugin) {
        super(plugin);
        this.pnm = plugin.getPlayerNameManager();
    }

    @EventHandler
    private void onJoin(PlayerJoinEvent e) {
        pnm.addPlayer(e.getPlayer());
    }

    @EventHandler
    private void onQuit(PlayerQuitEvent e) {
        pnm.removePlayer(e.getPlayer());
    }

}
