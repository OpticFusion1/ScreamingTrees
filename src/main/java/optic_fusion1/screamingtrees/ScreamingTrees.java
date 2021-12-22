package optic_fusion1.screamingtrees;

import java.util.List;
import org.bukkit.configuration.file.FileConfiguration;
import java.util.logging.Logger;
import java.util.logging.Level;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import org.bukkit.plugin.java.JavaPlugin;

public class ScreamingTrees extends JavaPlugin {

  private static final Random RANDOM = ThreadLocalRandom.current();

  @Override
  public void onEnable() {
    new MetricsLite(this, 8685);
    checkForUpdate();
    Bukkit.getPluginManager().registerEvents(new ScreamingTreesListener(this), this);
    getConfig().options().copyDefaults(true);
    saveConfig();
  }

  private void checkForUpdate() {
    Logger logger = getLogger();
    FileConfiguration pluginConfig = getConfig();
    Updater updater = new Updater(this, 83235, false);
    Updater.UpdateResult result = updater.getResult();
    if (result == Updater.UpdateResult.UPDATE_AVAILABLE) {
      if (!pluginConfig.isSet("auto-update") || !pluginConfig.getBoolean("auto-update")) {
        logger.info("===== UPDATE AVAILABLE ====");
        logger.info("https://www.spigotmc.org/resources/screamingtrees.83235/");
        logger.log(Level.INFO, "Installed Version: {0} New Version:{1}", new Object[]{updater.getOldVersion(), updater.getVersion()});
        logger.info("===== UPDATE AVAILABLE ====");
      } else if (pluginConfig.getBoolean("auto-update")) {
        updater.downloadUpdate();
      }
    }
  }

  public Random getRandom() {
    return RANDOM;
  }

  public boolean getBleedAllowed() {
    return getConfig().getBoolean("TreeBleed.Allowed");
  }

  public int getBleedChance() {
    return getConfig().getInt("TreeBleed.Chance");
  }

  public boolean getScreamAllowed() {
    return getConfig().getBoolean("TreeScream.Allowed");
  }

  public int getScreamChance() {
    return getConfig().getInt("TreeScream.Chance");
  }

  public boolean getSpeakAllowed() {
    return getConfig().getBoolean("TreeSpeak.Allowed");
  }

  public int getSpeakChance() {
    return getConfig().getInt("TreeSpeak.Chance");
  }

  public List<String> getSpeakMessages() {
    return getConfig().getStringList("TreeSpeak.Messages");
  }

  public boolean getThrowAllowed() {
    return getConfig().getBoolean("TreeThrow.Allowed");
  }

  public int getThrowChance() {
    return getConfig().getInt("TreeThrow.Chance");
  }

  public int getThrowDamage() {
    return getConfig().getInt("TreeThrow.Damage");
  }

  public boolean getAllWorlds() {
    return getConfig().getBoolean("Worlds.AllWorlds");
  }

  public List<String> getWorlds() {
    return getConfig().getStringList("Worlds.Worlds");
  }

  public boolean getAllBiomes() {
    return getConfig().getBoolean("Biomes.AllBiomes");
  }

  public List<String> getBiomes() {
    return getConfig().getStringList("Biomes.Biomes");
  }

}
