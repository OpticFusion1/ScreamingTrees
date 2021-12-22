package optic_fusion1.screamingtrees;

import org.bukkit.util.Vector;
import org.bukkit.entity.Player;
import org.bukkit.entity.Item;
import org.bukkit.scheduler.BukkitRunnable;

public class ThrowItemRunnable extends BukkitRunnable {

  private Item item;
  private Player player;

  public ThrowItemRunnable(Item argItem, Player argPlayer) {
    item = argItem;
    player = argPlayer;
  }

  @Override
  public void run() {
    Vector vec1 = item.getLocation().toVector();
    Vector vec2 = player.getEyeLocation().toVector();
    item.setVelocity(vec2.subtract(vec1).normalize().multiply(1));
  }
}
