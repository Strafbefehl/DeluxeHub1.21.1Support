package fun.lewisdev.deluxehub.hook.hooks.head;

import fun.lewisdev.deluxehub.DeluxeHubPlugin;
import fun.lewisdev.deluxehub.hook.PluginHook;
import fun.lewisdev.deluxehub.utility.universal.XMaterial;
import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.profile.PlayerProfile;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BaseHead implements PluginHook, HeadHook {

    private Map<String, ItemStack> cache;

    @Override
    public void onEnable(DeluxeHubPlugin plugin) {
        cache = new HashMap<>();
    }

    @Override
    public ItemStack getHead(String data) {
        if (cache.containsKey(data)) return cache.get(data);

		String decodedBase = new String(Base64.getDecoder().decode(data), StandardCharsets.UTF_8);
		Pattern pattern = Pattern.compile("\"url\"\\s*:\\s*\"([^\"]+)\"");
		Matcher matcher = pattern.matcher(decodedBase);

		ItemStack head = XMaterial.PLAYER_HEAD.parseItem();
		SkullMeta meta = (SkullMeta) head.getItemMeta();
		if(matcher.find()){
			PlayerProfile playerProfile = Bukkit.createPlayerProfile(UUID.randomUUID(), "");
			try{
				URL url = URI.create(matcher.group(1)).toURL();
				playerProfile.getTextures().setSkin(url);
				meta.setOwnerProfile(playerProfile);
				head.setItemMeta(meta);
			}catch(IllegalArgumentException | SecurityException | MalformedURLException e){
				e.printStackTrace();
			}
		}
		cache.put(data, head);
		return head;
    }
}
