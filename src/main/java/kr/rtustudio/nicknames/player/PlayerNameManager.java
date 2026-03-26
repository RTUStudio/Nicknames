package kr.rtustudio.nicknames.player;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import kr.rtustudio.nicknames.NickNames;
import lombok.extern.java.Log;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Log
public class PlayerNameManager {

    private final NickNames plugin;

    private final Map<UUID, PlayerName> map = new Object2ObjectOpenHashMap<>();

    public PlayerNameManager(NickNames plugin) {
        this.plugin = plugin;
    }

    public List<PlayerName> getPlayers() {
        return new ObjectArrayList<>(map.values());
    }

    public boolean contains(UUID uuid) {
        return map.containsKey(uuid);
    }

    public void addPlayer(Player player) {
        PlayerName playerName = new PlayerName(plugin, player);
        map.put(player.getUniqueId(), playerName);
    }

    public void removePlayer(Player player) {
        map.remove(player.getUniqueId());
    }

    public PlayerName getPlayer(UUID uuid) {
        return map.get(uuid);
    }

    public PlayerName getPlayer(String name) {
        for (PlayerName playerName : map.values()) {
            if (playerName.getName().equals(name)) return playerName;
        }
        return null;
    }

}
