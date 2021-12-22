package optic_fusion1.screamingtrees;

import org.bukkit.entity.Item;
import org.bukkit.util.Vector;
import org.bukkit.EntityEffect;
import org.bukkit.entity.Entity;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.Material;
import java.util.EnumSet;
import org.bukkit.event.Listener;

public class ScreamingTreesListener implements Listener {

  private static final EnumSet<Material> LOGS = EnumSet.of(Material.ACACIA_LOG, Material.BIRCH_LOG, Material.DARK_OAK_LOG,
          Material.JUNGLE_LOG, Material.OAK_LOG, Material.SPRUCE_LOG);
  private ScreamingTrees plugin;

  public ScreamingTreesListener(ScreamingTrees plugin) {
    this.plugin = plugin;
  }

  @EventHandler
  public void onPlayerBreakLog(BlockBreakEvent event) {
    Player player = event.getPlayer();
    if (LOGS.contains(event.getBlock().getType())) {
      Block log = event.getBlock();
      Tree tree = new Tree(plugin, log);
      if (tree.isTreeInWorld() && tree.isTreeInBiome()) {
        tree.bleed();
        tree.scream();
        tree.speak(player);
      }
    }
  }

  @EventHandler
  public void onPlayerHitLog(BlockDamageEvent event) {
    Player player = event.getPlayer();
    if (LOGS.contains(event.getBlock().getType())) {
      Block log = event.getBlock();
      Tree tree = new Tree(plugin, log);
      if (tree.isTreeInWorld() && tree.isTreeInBiome()) {
        tree.throwItem(player);
      }
    }
  }

  @EventHandler
  public void onPlayerHitByItem(EntityPickupItemEvent event) {
    if (!(event.getEntity() instanceof Player)) {
      return;
    }
    Player player = (Player) event.getEntity();
    Item item = event.getItem();
    if (Tree.itemsBeingThrown.containsKey(item)) {
      int throwDamage = plugin.getThrowDamage();
      event.setCancelled(true);
      player.damage(throwDamage, (Entity) item);
      item.remove();
      player.playEffect(EntityEffect.HURT);
      double max = 0.4;
      double x = max * (2.0 * Math.random() - 1.0);
      double z = max * (2.0 * Math.random() - 1.0);
      double y = max * (0.5 * Math.random() + 0.5);
      player.setVelocity(player.getVelocity().add(new Vector(x, y, z)));
      Tree.itemsBeingThrown.remove(item);
    }
  }
}
