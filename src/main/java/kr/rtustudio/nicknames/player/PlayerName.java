package kr.rtustudio.nicknames.player;

import kr.rtustudio.framework.bukkit.api.configuration.internal.translation.TranslationConfiguration;
import kr.rtustudio.framework.bukkit.api.format.ComponentFormatter;
import kr.rtustudio.framework.bukkit.api.player.PlayerChat;
import kr.rtustudio.nicknames.NickNames;
import kr.rtustudio.nicknames.configuration.NameConfig;
import kr.rtustudio.nicknames.manager.NameManager;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@Getter
@RequiredArgsConstructor
public class PlayerName {

    private final NickNames plugin;

    private final PlayerChat chat;
    private final NameManager nnm;
    private final NameConfig nameConfig;

    private final Player player;
    private final TranslationConfiguration message;
    private String name;

    public PlayerName(NickNames plugin, Player player) {
        this.plugin = plugin;
        this.nnm = plugin.getNameManager();
        this.message = plugin.getConfiguration().getMessage();
        this.chat = PlayerChat.of(plugin);
        this.nameConfig = plugin.getConfiguration(NameConfig.class);
        this.player = player;
        this.name = nnm.getName(player.getUniqueId());
        if (name != null && name.isEmpty()) return;
        this.player.setDisplayName(this.name);
    }

    public void setName(String name) {
        this.name = name;
        player.setDisplayName(name);
        nnm.setName(player.getUniqueId(), name);
    }

    public void changeName(Player player, String name) {
        changeName(null, player, name);
    }

    public void changeName(CommandSender operator, Player player, String name) {
        final String previous = this.name;

        CommandSender sender = operator != null ? operator : player;
        if (name.equals("잘못된닉네임")) {
            chat.announce(sender, ComponentFormatter.mini(message.get(sender, "nickname.change.unavailable")));
            return;
        }
        switch (validateName(name)) {
            case CONTAINS ->
                    chat.announce(sender, ComponentFormatter.mini(message.get(sender, "nickname.change.contains")));
            case LENGTH ->
                    chat.announce(sender, ComponentFormatter.mini(message.get(sender, "nickname.change.length").replace("{max_length}", String.valueOf(nameConfig.getMaxLength()))));
            case DUPLICATED ->
                    chat.announce(sender, ComponentFormatter.mini(message.get(sender, "nickname.change.duplicated")));
            case SUCCESS -> {
                setName(name);
                chat.announce(sender, ComponentFormatter.mini(message.get(sender, operator != null ? "nickname.change.other" : "nickname.change.success").replace("{previous}", previous).replace("{name}", name)));
            }
        }
    }

    private NameCheckResult validateName(String name) {
        if (!name.matches(nameConfig.getAllowedRegex())) return NameCheckResult.CONTAINS;
        if (name.length() > nameConfig.getMaxLength()) return NameCheckResult.LENGTH;
        if (nnm.getNamesFromDB().contains(name)) return NameCheckResult.DUPLICATED;
        return NameCheckResult.SUCCESS;
    }

    private enum NameCheckResult {
        CONTAINS,
        LENGTH,
        DUPLICATED,
        SUCCESS
    }
}
