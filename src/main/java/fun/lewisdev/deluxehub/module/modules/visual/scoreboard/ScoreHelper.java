package fun.lewisdev.deluxehub.module.modules.visual.scoreboard;

import fun.lewisdev.deluxehub.utility.PlaceholderUtil;
import fun.lewisdev.deluxehub.utility.TextUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.List;

import static fun.lewisdev.deluxehub.utility.color.patterns.HexUtils.colorize;
import static fun.lewisdev.deluxehub.utility.color.patterns.HexUtils.translateHexColorCodes;

/**
 * @author crisdev333
 */
public class ScoreHelper {

    private Scoreboard scoreboard;
    private Objective objective;
    private Player player;

    public ScoreHelper(Player player) {
        this.player = player;
        scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
        objective = scoreboard.registerNewObjective("sidebar", "dummy");
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);

        // Create Teams, limited to ChatColor values
        for (int i = 0; i < ChatColor.values().length; i++) {
            Team team = scoreboard.registerNewTeam("SLOT_" + (i + 1)); // Slot starts from 1
            team.addEntry(genEntry(i));
        }
        player.setScoreboard(scoreboard);
    }

    public void setTitle(String title) {
        title = setPlaceholders(title);
        objective.setDisplayName(title.length() > 200 ? title.substring(0, 200) : title);
    }

    public void setSlot(int slot, String text) {
        Team team = scoreboard.getTeam("SLOT_" + slot);
        String entry = genEntry(slot - 1); // Adjust for zero-based index

        if (team != null && !scoreboard.getEntries().contains(entry)) {
            objective.getScore(entry).setScore(slot);
        }

        text = setPlaceholders(text);
        String pre = getFirstSplit(text);
        String suf = getFirstSplit(ChatColor.getLastColors(pre) + getSecondSplit(text));
        team.setPrefix(pre);
        team.setSuffix(suf);
    }

    public void removeSlot(int slot) {
        String entry = genEntry(slot - 1); // Adjust for zero-based index
        if (scoreboard.getEntries().contains(entry)) {
            scoreboard.resetScores(entry);
        }
    }

    public String setPlaceholders(String text) {
        return colorize(translateHexColorCodes(PlaceholderUtil.setPlaceholders(text, this.player)));
    }

    public void setSlotsFromList(List<String> list) {
        while (list.size() > 199) {
            list.remove(list.size() - 1);
        }

        int slot = list.size();

        if (slot < 199) {
            for (int i = (slot + 1); i <= 199; i++) {
                removeSlot(i);
            }
        }

        for (String line : list) {
            setSlot(slot, line);
            slot--;
        }
    }

    private String genEntry(int slot) {
        if (slot < 0 || slot >= ChatColor.values().length) {
            return ""; // Return an empty string for out-of-bounds indices
        }
        return ChatColor.values()[slot].toString();
    }

    private String getFirstSplit(String s) {
        return s.length() > 200 ? s.substring(0, 200) : s;
    }

    private String getSecondSplit(String s) {
        if (s.length() > 245) {
            s = s.substring(0, 245);
        }
        return s.length() > 200 ? s.substring(200) : "";
    }
}
