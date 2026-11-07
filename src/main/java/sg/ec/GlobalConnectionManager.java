// Original binary: sg/ec/IAGMUtg9AkptMrSV
package sg.ec;

import java.util.List;

/**
 * Offline stub of the client's former global backend connection.
 *
 * <p>QuickClient used to hold a websocket session to a remote service for IRC, parties, friends,
 * cloud configs, the config market, forum auth and online player state. That socket, its pinned TLS
 * factory, its reconnect/autosave timers and the endpoint itself are gone, so the client opens no
 * connection to anything besides the Minecraft server the player joins.
 *
 * <p>Every public member keeps its original signature so the remaining call sites (party and IRC
 * commands, the schedules HUD, packet handlers) still compile and simply observe an offline client.
 */
public class GlobalConnectionManager implements MinecraftAccess {
   public static String field08653 = "QUICK";
   public static volatile List<AnarchyServerInfo> field08661 = List.of();
   public static volatile List<FunTimeServerData> field08662 = List.of();
   public static volatile long field08663 = 0L;

   public static boolean method03845() {
      return false;
   }

   public static void method03846(List<AnarchyServerInfo> var0, List<FunTimeServerData> var1) {
      field08661 = var0 != null ? List.copyOf(var0) : List.of();
      field08662 = var1 != null ? List.copyOf(var1) : List.of();
      field08663 = System.currentTimeMillis();
   }

   public static void method03849() {
   }

   public static void method03850() {
   }

   public static void method03852(GlobalPacket<GlobalPacketHandler, ?> var0) {
   }

   public static void method03853() {
   }

   public static void method03855() {
   }

   public static void method03856() {
   }

   public static void method03857() {
   }

   public static GlobalPlayer method03858() {
      return null;
   }

   public static void method03859() {
   }

   public static void method03860() {
   }
}
