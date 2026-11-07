package sg.ec;

import net.minecraft.core.component.DataComponents;
import net.minecraft.network.protocol.game.ServerboundSetCarriedItemPacket;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.Fireworks;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AutoFireworkRocket extends ClientModule {
   private static final Logger LOGGER = LoggerFactory.getLogger("QuickClient");
   private static final String HAND_CLIENT = "Client Default";
   private static final String HAND_HOTBAR = "Hotbar";
   private static final String HAND_OFFHAND = "Offhand Swap";

   private final NumberSetting<Integer> boostInterval;
   private final NumberSetting<Float> speedThreshold;
   private final NumberSetting<Integer> reserveRockets;
   private final NumberSetting<Integer> minFlightDuration;
   private final ModeSetting useHand;
   private final ToggleSetting skipWhileBlinking;
   private final NumberSetting<Integer> boostTimeout;
   private final NumberSetting<Integer> backoffDelay;
   private final KeyBindSetting boostKey;
   private final ToggleSetting debugLog;
   private final ElapsedTimer intervalTimer = new ElapsedTimer();
   private long backoffUntil;
   private int pendingSinceTick = -1;
   private boolean boostKeyPressed;

   public AutoFireworkRocket() {
      super(ModuleCategory.Movement, ModuleSubCategory.Main);
      this.boostInterval = new NumberSetting<Integer>("Boost Interval (ms)")
         .method10835(100)
         .method10838(5000)
         .method10834(50)
         .method03017(500)
         .method03023(this);
      this.speedThreshold = new NumberSetting<Float>("Speed Threshold (bps)")
         .method10835(0.0F)
         .method10838(60.0F)
         .method10834(0.5F)
         .method03017(25.0F)
         .method03023(this);
      this.reserveRockets = new NumberSetting<Integer>("Reserve Rockets")
         .method10835(0)
         .method10838(64)
         .method10834(1)
         .method03017(0)
         .method03023(this);
      this.minFlightDuration = new NumberSetting<Integer>("Min Flight Duration")
         .method10835(1)
         .method10838(3)
         .method10834(1)
         .method03017(1)
         .method03023(this);
      this.useHand = new ModeSetting("Use Hand")
         .method05597(HAND_CLIENT, HAND_HOTBAR, HAND_OFFHAND)
         .method03017(HAND_CLIENT)
         .method03023(this);
      this.skipWhileBlinking = new ToggleSetting("Skip While Blinking").method03017(true).method03023(this);
      this.boostTimeout = new NumberSetting<Integer>("Boost Timeout (ticks)")
         .method10835(1)
         .method10838(40)
         .method10834(1)
         .method03017(10)
         .method03023(this);
      this.backoffDelay = new NumberSetting<Integer>("Backoff (ms)")
         .method10835(0)
         .method10838(5000)
         .method10834(100)
         .method03017(1000)
         .method03023(this);
      this.boostKey = new KeyBindSetting("Boost Key").method03017(new KeyBinding()).method03023(this);
      this.debugLog = new ToggleSetting("Debug Log").method03017(false).method03023(this);
      this.method03935("Auto Firework Rocket");
      this.method03931("Keeps the elytra boosted while gliding. Elytra Target can route its own rockets through this scheduler.");
   }

   @Override
   public void method00276() {
      this.reset();
      super.method00276();
   }

   @Override
   public void method00245() {
      this.reset();
      super.method00245();
   }

   private void reset() {
      this.pendingSinceTick = -1;
      this.backoffUntil = 0L;
      this.boostKeyPressed = false;
      this.intervalTimer.method04694();
   }

   @Subscribe
   private void onKeyInput(KeyInputEvent var1) {
      this.handleKey(var1.method05887(), var1.method05884());
   }

   @Subscribe
   private void onMouseButton(MouseButtonEvent var1) {
      this.handleKey(var1.method06914(), var1.method06919());
   }

   private void handleKey(int key, InputAction action) {
      if (action != InputAction.RELEASE && key >= 0 && this.boostKey.method03022() != null && this.boostKey.method03022().method05871() == key) {
         this.boostKeyPressed = true;
      }
   }

   @Subscribe
   private void onFireworkBoost(FireworkBoostEvent var1) {
      if (var1.method04089() == mc.player) {
         this.pendingSinceTick = -1;
      }
   }

   @Subscribe
   private void onPlayerUpdate(PlayerUpdateEvent var1) {
       if (mc.player == null || mc.level == null) {
          this.pendingSinceTick = -1;
          return;
       }

       // When Elytra Target is active it routes its own rockets through requestBoost(true);
       // skip the autonomous loop to avoid interrupting it.
       ElytraTarget elytraTarget = ModuleManager.INSTANCE.method08186();
       if (elytraTarget != null && elytraTarget.method03937()) {
          return;
       }

      if (this.pendingSinceTick >= 0 && mc.player.tickCount - this.pendingSinceTick > this.boostTimeout.method03022()) {
         this.pendingSinceTick = -1;
         this.backoffUntil = System.currentTimeMillis() + this.backoffDelay.method03022();
         if (this.debugLog.method05999()) {
            LOGGER.info("[AutoFireworkRocket] no boost observed, backing off for {} ms", this.backoffDelay.method03022());
         }
      }

      if (mc.gui != null && mc.gui.screen() != null) {
         return;
      }

      boolean forced = this.boostKeyPressed;
      this.boostKeyPressed = false;
      this.requestBoost(forced);
   }

   // immediate requests skip the interval/speed/blink gates so Elytra Target's lag-rocket combo still fires inside its own blink window
   public boolean requestBoost(boolean immediate) {
      if (mc.player == null || mc.level == null || mc.gameMode == null || mc.player.connection == null) {
         return false;
      }

      if (!mc.player.isFallFlying() || mc.player.isUsingItem() || this.pendingSinceTick >= 0 || System.currentTimeMillis() < this.backoffUntil) {
         return false;
      }

      if (InventoryActionManager.INSTANCE.method10476()) {
         return false;
      }

      if (!immediate) {
         if (!this.intervalTimer.method04699(this.boostInterval.method03022())) {
            return false;
         }

         if (this.speedThreshold.method03022() > 0.0F && MovementUtils.method05729(mc.player) >= this.speedThreshold.method03022()) {
            return false;
         }

         if (this.skipWhileBlinking.method05999() && PacketBufferManager.INSTNCE.method03607()) {
            return false;
         }
      }

      Slot slot = this.findRocketSlot();
      if (slot == null) {
         return false;
      }

      ItemStack stack = slot.getItem();
      if (stack.isEmpty() || mc.player.getCooldowns().isOnCooldown(stack)) {
         return false;
      }

      if (InventoryUtils.method06099(Items.FIREWORK_ROCKET) - stack.getCount() < this.reserveRockets.method03022()) {
         return false;
      }

      this.useRocket(slot);
      this.pendingSinceTick = mc.player.tickCount;
      this.intervalTimer.method04694();
      if (this.debugLog.method05999()) {
         LOGGER.info("[AutoFireworkRocket] fired rocket via {}, {} left", this.useHand.method03022(), InventoryUtils.method06099(Items.FIREWORK_ROCKET));
      }

      return true;
   }

   private void useRocket(Slot slot) {
      String hand = this.useHand.method03022();
      boolean offhand = HAND_OFFHAND.equals(hand) || HAND_CLIENT.equals(hand) && ModuleManager.INSTANCE.method08187().isLegitRocketUse();
      if (offhand) {
         DelayedInventoryActionManager.INSTANCE.method09011(InteractionHand.OFF_HAND, slot);
      } else {
         this.useFromHotbar(slot);
      }
   }

   private void useFromHotbar(Slot slot) {
      int hotbarIndex = -1;

      for (int index = 0; index < 9; index++) {
         if (mc.player.getInventory().getItem(index) == slot.getItem()) {
            hotbarIndex = index;
            break;
         }
      }

      if (hotbarIndex == -1) {
         InventoryUtils.method06097(slot.getItem(), Float.MAX_VALUE, Float.MAX_VALUE);
         return;
      }

      int selected = mc.player.getInventory().getSelectedSlot();
      if (hotbarIndex != selected) {
         mc.player.connection.send(new ServerboundSetCarriedItemPacket(hotbarIndex));
      }

      InventoryUtils.method06142(InteractionHand.MAIN_HAND, Float.MAX_VALUE, Float.MAX_VALUE, false);
      if (hotbarIndex != selected) {
         mc.player.connection.send(new ServerboundSetCarriedItemPacket(selected));
      }
   }

   private Slot findRocketSlot() {
      Slot fallback = null;

      for (Slot slot : mc.player.containerMenu.slots) {
         ItemStack stack = slot.getItem();
         if (stack.getItem() == Items.FIREWORK_ROCKET && this.isUsableRocket(stack)) {
            if (slot.container == mc.player.getInventory() && slot.index < 9) {
               return slot;
            }

            if (fallback == null) {
               fallback = slot;
            }
         }
      }

      return fallback;
   }

   private boolean isUsableRocket(ItemStack stack) {
      Fireworks fireworks = stack.get(DataComponents.FIREWORKS);
      return fireworks != null && fireworks.flightDuration() >= this.minFlightDuration.method03022();
   }
}