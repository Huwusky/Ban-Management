package me.confuser.banmanager.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;

import me.confuser.banmanager.BanManager;
import me.confuser.banmanager.data.PlayerMuteData;
import me.confuser.banmanager.events.PlayerMuteEvent;
import me.confuser.banmanager.util.DateUtils;
import me.confuser.bukkitutil.Message;
import me.confuser.bukkitutil.listeners.Listeners;

public class MuteListener extends Listeners<BanManager> {

	@EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
	public void notifyOnBan(PlayerMuteEvent event) {
		PlayerMuteData mute = event.getMute();

		String broadcastPermission;
		Message message;

		if (mute.getExpires() == 0) {
			broadcastPermission = "bm.notify.mute";
			message = Message.get("playerMuted");
		} else {
			broadcastPermission = "bm.notify.tempmute";
			message = Message.get("playerTempMuted");
			message.set("expires", DateUtils.getDifferenceFormat(mute.getExpires()));
		}

		message.set("player", mute.getPlayer().getName()).set("actor", mute.getActor().getName()).set("reason", mute.getReason());

		plugin.getServer().broadcast(message.toString(), broadcastPermission);

		// Check if the sender is online and does not have the
		// broadcastPermission
		Player player;
		if ((player = plugin.getServer().getPlayer(mute.getActor().getUUID())) == null) {
			return;
		}

		if (!player.hasPermission(broadcastPermission)) {
			message.sendTo(player);
		}
	}
}
