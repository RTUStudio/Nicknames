package kr.rtustudio.nicknames.manager;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import kr.rtustudio.nicknames.NickNames;
import kr.rtustudio.nicknames.data.Nickname;
import kr.rtustudio.storage.JSON;
import kr.rtustudio.storage.Storage;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class NameManager {

    private final Storage storage;
    private final Map<UUID, Nickname> cache = new Object2ObjectOpenHashMap<>();

    public NameManager(NickNames plugin) {
        this.storage = plugin.getStorage("Nickname");
        storage.get(JSON.of().get()).thenAccept(result -> {
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
        storage.get(JSON.of("uuid", uuid.toString()).get()).thenAccept(result -> {
            cache.put(uuid, new Nickname(uuid, name));
            if (result == null || result.isEmpty()) {
                storage.add(JSON.of("uuid", uuid.toString()).append("name", name).get());
            } else {
                storage.set(JSON.of("uuid", uuid.toString()).get(), JSON.of("name", name).get());
            }
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
        CompletableFuture<List<JsonObject>> result = storage.get(JSON.of("uuid", uuid.toString()).get());
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
