package optic_fusion1.screamingtrees;

import java.util.HashMap;
import java.util.List;
import org.bukkit.inventory.ItemStack;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.Sound;
import de.slikey.effectlib.Effect;
import de.slikey.effectlib.effect.BleedEffect;
import org.bukkit.plugin.Plugin;
import org.bukkit.block.Block;
import java.util.Random;
import org.bukkit.Location;
import org.bukkit.entity.Item;
import java.util.Map;
import de.slikey.effectlib.EffectManager;

public class Tree {

  private EffectManager em;
  public static Map<Item, Tree> itemsBeingThrown = new HashMap<>();
  private Location treeLocation;
  private ScreamingTrees plugin;
  private Random rand;

  public Tree(ScreamingTrees plugin, Block argTreeBlock) {
    em = new EffectManager((Plugin) plugin);
    treeLocation = argTreeBlock.getLocation();
    this.plugin = plugin;
    rand = plugin.getRandom();
  }

  public void bleed() {
    boolean allowed = plugin.getBleedAllowed();
    int chance = plugin.getBleedChance();
    if (allowed && rand.nextInt(100) < chance) {
      Effect effect = new BleedEffect(em);
      effect.setLocation(treeLocation);
      effect.iterations = 1;
      effect.start();
      em.disposeOnTermination();
    }
  }

  public void scream() {
    boolean allowed = plugin.getScreamAllowed();
    int chance = plugin.getScreamChance();
    if (allowed && rand.nextInt(100) < chance) {
      treeLocation.getWorld().playSound(treeLocation, Sound.ENTITY_GHAST_SCREAM, 1.0f, 1.0f);
    }
  }

  public void speak(Player player) {
    boolean allowed = plugin.getSpeakAllowed();
    int chance = plugin.getSpeakChance();
    if (allowed && rand.nextInt(100) < chance) {
      player.sendMessage(getRandomSpeakMessage());
    }
  }

  public void throwItem(Player player) {
    boolean allowed = plugin.getThrowAllowed();
    int chance = plugin.getThrowChance();
    if (allowed && rand.nextInt(100) < chance) {
      Location throwLoc = treeLocation;
      throwLoc.add(0.0, 3.0, 0.0);
      Item item = player.getWorld().dropItemNaturally(throwLoc, new ItemStack(Material.APPLE));
      Tree.itemsBeingThrown.put(item, this);
      new ThrowItemRunnable(item, player).runTaskTimer(plugin, 0L, 1L);
    }
  }

  public boolean isTreeInWorld() {
    return plugin.getAllWorlds() || plugin.getWorlds().stream().anyMatch(world -> treeLocation.getWorld().getName().equalsIgnoreCase(world));
  }

  public boolean isTreeInBiome() {
    return plugin.getAllBiomes() || plugin.getBiomes().stream().anyMatch(biome -> treeLocation.getBlock().getBiome().toString().equalsIgnoreCase(biome));
  }

  private String getRandomSpeakMessage() {
    List<String> messages = plugin.getSpeakMessages();
    return "<Tree> " + messages.get(rand.nextInt(messages.size()));
  }

}
