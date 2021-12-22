package optic_fusion1.screamingtrees;

import java.nio.channels.ReadableByteChannel;
import java.io.FileOutputStream;
import java.nio.channels.Channels;
import java.io.File;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.apache.commons.lang.StringUtils;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import javax.net.ssl.HttpsURLConnection;
import org.bukkit.plugin.java.JavaPlugin;

public class Updater {

  private String oldVersion;
  private JavaPlugin plugin;
  private String RESOURCE_ID;
  private UpdateResult result;
  private String version;

  public Updater(JavaPlugin plugin, int resourceId, boolean disabled) {
    super();
    RESOURCE_ID = resourceId + "";
    this.plugin = plugin;
    oldVersion = plugin.getDescription().getVersion();
    if (disabled || resourceId == -1) {
      result = UpdateResult.DISABLED;
      return;
    }
    run();
  }

  public UpdateResult getResult() {
    return result;
  }

  public String getVersion() {
    return version;
  }

  public String getOldVersion() {
    return oldVersion;
  }

  private void run() {
    try {
      HttpsURLConnection connection = (HttpsURLConnection) new URL("https://api.spigotmc.org/legacy/update.php?resource=" + RESOURCE_ID).openConnection();
      int timed_out = 2000;
      connection.setConnectTimeout(timed_out);
      connection.setReadTimeout(timed_out);
      version = new BufferedReader(new InputStreamReader(connection.getInputStream())).readLine();
      connection.disconnect();
      versionCheck();
    } catch (IOException e) {
      result = UpdateResult.FAIL_SPIGOT;
    }
  }

  public boolean shouldUpdate(String localVersion, String remoteVersion) {
    return versionCompare(localVersion, remoteVersion) == 2;
  }

  private void versionCheck() {
    if (shouldUpdate(oldVersion, version)) {
      result = UpdateResult.UPDATE_AVAILABLE;
    } else {
      result = UpdateResult.NO_UPDATE;
    }
  }

  public int versionCompare(String v1, String v2) {
    int v1Len = StringUtils.countMatches(v1, ".");
    int v2Len = StringUtils.countMatches(v2, ".");
    if (v1Len != v2Len) {
      int count = Math.abs(v1Len - v2Len);
      if (v1Len > v2Len) {
        for (int i = 1; i <= count; ++i) {
          v2 += ".0";
        }
      } else {
        for (int i = 1; i <= count; ++i) {
          v1 += ".0";
        }
      }
    }
    if (v1.equals(v2)) {
      return 0;
    }
    String[] v1Str = StringUtils.split(v1, ".");
    String[] v2Str = StringUtils.split(v2, ".");
    int j = 0;
    while (j < v1Str.length) {
      String str1 = "";
      String str2 = "";
      for (char c : v1Str[j].toCharArray()) {
        if (Character.isLetter(c)) {
          int u = c - 'a' + 1;
          if (u < 10) {
            str1 += String.valueOf("0" + u);
          } else {
            str1 += String.valueOf(u);
          }
        } else {
          str1 += String.valueOf(c);
        }
      }
      for (char c : v2Str[j].toCharArray()) {
        if (Character.isLetter(c)) {
          int u = c - 'a' + 1;
          if (u < 10) {
            str2 += String.valueOf("0" + u);
          } else {
            str2 += String.valueOf(u);
          }
        } else {
          str2 += String.valueOf(c);
        }
      }
      v1Str[j] = "1" + str1;
      v2Str[j] = "1" + str2;
      int num1 = Integer.parseInt(v1Str[j]);
      int num2 = Integer.parseInt(v2Str[j]);
      if (num1 != num2) {
        if (num1 > num2) {
          return 1;
        }
        return 2;
      } else {
        ++j;
      }
    }
    return -1;
  }

  public void downloadUpdate() {
    if (result == UpdateResult.UPDATE_AVAILABLE) {
      download(plugin, Integer.valueOf(RESOURCE_ID));
      plugin.getLogger().info("Downloaded jar automatically, restart to update. Note: Updates take 30-40 minutes to load");
    }
  }

  public boolean download(Plugin plugin, int resourceId) {
    try {
      download(new URL("https://api.spiget.org/v2/resources/" + resourceId + "/download"), new File(Bukkit.getServer().getUpdateFolderFile(), plugin.getDescription().getName() + ".jar"));
      return true;
    } catch (IOException e) {
      plugin.getLogger().warning("Unable to download jar");
      e.printStackTrace();
      return false;
    }
  }

  private void download(URL url, File target) throws IOException {
    target.getParentFile().mkdirs();
    target.createNewFile();
    try ( ReadableByteChannel rbc = Channels.newChannel(url.openStream());  FileOutputStream fos = new FileOutputStream(target)) {
      fos.getChannel().transferFrom(rbc, 0L, Long.MAX_VALUE);
    }
  }

  public enum UpdateResult {
    BAD_RESOURCEID,
    DISABLED,
    FAIL_NOVERSION,
    FAIL_SPIGOT,
    NO_UPDATE,
    UPDATE_AVAILABLE;
  }
}
