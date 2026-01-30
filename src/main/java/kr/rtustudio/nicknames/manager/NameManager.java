package kr.rtustudio.nicknames.manager;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import kr.rtustudio.framework.bukkit.api.platform.JSON;
import kr.rtustudio.framework.bukkit.api.storage.Storage;
import kr.rtustudio.nicknames.NickNames;
import kr.rtustudio.nicknames.data.Nickname;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class NameManager {

    private final NickNames plugin;

    private final HashMap<UUID, Nickname> cache = new HashMap<>();

    public NameManager(NickNames plugin) {
        this.plugin = plugin;
        Storage storage = plugin.getStorage();
        storage.get("Nickname", JSON.of()).thenAccept(result -> {
            for (JsonObject object : result) {
                String uuid = object.get("uuid").getAsString();
                String name = object.get("name").getAsString();
                try {
                    UUID parsedUuid = UUID.fromString(uuid);
                    cache.put(parsedUuid, new Nickname(parsedUuid, name));
                } catch (Exception ignored) {
                }
            }
        });
    }

    public void setName(UUID uuid, String name) {
        Storage storage = plugin.getStorage();
        storage.get("Nickname", JSON.of("uuid", uuid.toString())).thenAccept(result -> {
            cache.put(uuid, new Nickname(uuid, name));
            if (result == null || result.isEmpty()) {
                storage.add("Nickname", JSON.of("uuid", uuid.toString()).append("name", name));
            } else storage.set("Nickname", JSON.of("uuid", uuid.toString()), JSON.of("name", name));
        });
    }

    @Nullable
    public String getName(UUID uuid) {
        Nickname nickname = getNickname(uuid);
        return nickname == null ? "" : nickname.name();
    }

    @Nullable
    public Nickname getNickname(UUID uuid) {
        if (cache.containsKey(uuid)) return cache.get(uuid);
        Storage storage = plugin.getStorage();
        CompletableFuture<List<JsonObject>> result = storage.get("Nickname", JSON.of("uuid", uuid.toString()));
        if (result == null) return null;
        List<JsonObject> list = result.join();
        if (list == null || list.isEmpty()) return null;
        JsonElement name = list.getFirst().get("name");
        if (name == null) return null;
        Nickname nickname = new Nickname(uuid, name.getAsString());
        cache.put(uuid, nickname);
        return nickname;
    }

    public List<String> getNamesFromDB() {
        return cache.values().stream().map(Nickname::name).toList();
    }

}
