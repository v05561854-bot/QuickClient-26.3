// Original binary: sg/ec/RnKjCRVmDzPXHf5l
package sg.ec;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.Camera;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

// $VF: Compiled from s
public class ElytraTarget extends ClientModule {
   private static final String field11488 = "target";
   private static final double field11489 = 0;
   private static final double field11490 = 0;
   private static final double field11491 = 0;
   private static final double field11492 = 0;
   private static final double field11493 = 0;
   private static final double field11494 = 0;
   private static final double field11495 = 0;
   private static final double field11496 = 0;
   private static final double field11497 = 0;
   private static final double field11498 = 0;
   private static final long field11499 = 0;
   private static final long field11500 = 0;
   private static final long field11501 = 0;
   private static final long field11502 = 0;
   public final NumberSetting<Float> field11503;
   public final ToggleSetting field11504;
   public final NumberSetting<Float> field11505;
   public final ToggleSetting field11506;
   public final ModeSetting field11507;
   public final NumberSetting<Integer> field11508;
   public final NumberSetting<Float> field11509;
   public final ModeSetting field11510;
   private final ToggleSetting field11511;
   private final ModeSetting field11512;
   private final ToggleSetting autoFirework;
   private final ElytraTargetRenderer field11513;
   private ElytraTarget.Nested159 field11514;
   public Vec3 field11515;
   private boolean field11516;
   private boolean field11517;
   private int field11518;
   private final ElapsedTimer field11519;
   private final ElapsedTimer field11520;
   private final ElapsedTimer field11521;
   private final ElapsedTimer field11522;
   private final List<Vec3> field11523;
   public final ToggleSetting field11610;
   public final NumberSetting<Float> field11611;
   public final NumberSetting<Integer> field11612;
   private final ElapsedTimer field11613;
   private Vec3 field11614;
   private final ToggleSetting field11633;
   private final NumberSetting<Float> field11634;
   private final NumberSetting<Float> field11635;
   private final NumberSetting<Float> field11636;
   private final NumberSetting<Float> field11637;
   private final NumberSetting<Float> field11638;
   private final NumberSetting<Float> field11639;
   private final ToggleSetting field11643;
   private final NumberSetting<Float> field11644;
   private final NumberSetting<Float> field11645;
   private int field11646 = Integer.MIN_VALUE;
   private static final String field11524 = "Дистанция обнаружения";
   private static final float field11525 = 5.0F;
   private static final float field11526 = 100.0F;
   private static final float field11527 = 5.0F;
   private static final float field11528 = 30.0F;
   private static final String field11529 = "Элитра лаг";
   private static final String field11530 = "Дистанция залагивания";
   private static final float field11531 = 3.0F;
   private static final float field11532 = 15.0F;
   private static final float field11533 = 0.5F;
   private static final float field11534 = 7.0F;
   private static final String field11535 = "Элитра перегон";
   private static final String field11536 = "Режима предикта";
   private static final String field11537 = "От ротации";
   private static final String field11538 = "От скорости";
   private static final String field11539 = "От скорости";
   private static final String field11540 = "Предиктить от бпс";
   private static final String field11541 = "Сила предикта";
   private static final float field11542 = 10.0F;
   private static final float field11543 = 0.5F;
   private static final float field11544 = 3.0F;
   private static final String field11545 = "Мод перегона";
   private static final String field11546 = "Легитный";
   private static final String field11547 = "Полный рейдж";
   private static final String field11548 = "Полу рейдж";
   private static final String field11549 = "Легитный";
   private static final String field11550 = "Рендерить предикт";
   private static final String field11551 = "Как отображать";
   private static final String field11552 = "Квадратик";
   private static final String field11553 = "Моделька";
   private static final String field11554 = "Моделька";
   private static final String field11555 = "Elytra Target";
   private static final String field11556 = "Моделька";
   private static final double field11557 = 15.0;
   private static final double field11558 = 0.6;
   private static final double field11559 = 4.0;
   private static final double field11560 = 2.0;
   private static final double field11561 = 2.5;
   private static final long field11562 = 300L;
   private static final double field11563 = 7.0;
   private static final double field11564 = 5.0;
   private static final double field11565 = 2.5;
   private static final double field11566 = 5.0;
   private static final double field11567 = 5.0;
   private static final double field11568 = 4.0;
   private static final double field11569 = 0.5;
   private static final double field11570 = 0.3;
   private static final double field11571 = 6.0;
   private static final double field11572 = 6.0;
   private static final double field11573 = 0.2;
   private static final double field11574 = 5.0;
   private static final double field11575 = 1.0E-4;
   private static final double field11576 = 180.0;
   private static final double field11577 = 0.5;
   private static final double field11578 = -1.5;
   private static final double field11579 = 0.3;
   private static final double field11580 = 1.5;
   private static final double field11581 = 2.5;
   private static final double field11582 = 0.001;
   private static final double field11583 = 0.001;
   private static final double field11584 = 0.001;
   private static final double field11585 = 0.001;
   private static final double field11586 = 0.001;
   private static final double field11587 = 0.3;
   private static final double field11588 = 0.02;
   private static final double field11589 = 0.001;
   private static final double field11590 = 0.001;
   private static final double field11591 = 2.0;
   private static final double field11592 = 4.0;
   private static final double field11593 = 1.5;
   private static final double field11594 = 2.0;
   private static final double field11595 = -0.5;
   private static final double field11596 = -0.5;
   private static final double field11597 = -0.5;
   private static final double field11598 = -1.0;
   private static final double field11599 = 0.7;
   private static final double field11600 = 0.001;
   private static final double field11601 = 0.4;
   private static final double field11602 = 0.001;
   private static final double field11603 = 0.4;
   private static final String field11604 = "glass";
   private static final double field11605 = 250.0;
   private static final long field11606 = 250L;
   private static final long field11607 = 1000L;
   static final String field11608 = "target";
   private static final String field11609 = "target";
   private static final String field11615 = "Авто фейерверк";
   private static final String field11616 = "Мин. скорость";
   private static final String field11617 = "Задержка (мс)";
   private static final float field11618 = 5.0F;
   private static final float field11619 = 60.0F;
   private static final float field11620 = 20.0F;
   private static final int field11621 = 100;
   private static final int field11622 = 2000;
   private static final int field11623 = 50;
   private static final int field11624 = 400;
   private static final double field11625 = 20.0;
   private static final String field11626 = "Предикт перегона";
   private static final String field11627 = "Макс. упреждение (тики)";
   private static final String field11628 = "Вес дистанции";
   private static final String field11629 = "Вес поворота";
   private static final String field11630 = "Вес клиренса";
   private static final String field11631 = "Штраф за высоту";
   private static final String field11632 = "Дистанция отхода";
   private static final String field11640 = "Синхронизация с аурой";
   private static final String field11641 = "Упреждение атаки (тики)";
   private static final String field11642 = "Запас дистанции";

   private void method05281() {
      if (this.field11519.method04698(field11605)) {
         this.method05292();
      }
   }

   private Vec3 method05282(Vec3 var1) {
      Vec3 var2 = new Vec3(-var1.z, 0.0, var1.x);
      return var2.lengthSqr() < field11586 ? new Vec3(1.0, 0.0, 0.0) : var2.normalize();
   }

   @Subscribe
   private void method05283(Render3DEvent var1) {
      LivingEntity var2 = AttackAura.method00456();
      AttackAura var3 = ModuleManager.INSTANCE.method08219();
      boolean var4 = this.field11511.method05999() && var2 != null && var3.method03937() && var3.field00787;
      if (this.field11512.method05600(field11556)) {
         this.field11513.method10080(var1, var4 ? var2 : null, var4 ? var3.field00788 : null, ColorUtils.Nested138.method04636());
      } else {
         this.field11513.method10074();
         if (var4) {
            PoseStack var5 = var1.method00996();
            var5.pushPose();
            VertexConsumer var6 = var1.method00985().method07407(EffectRenderPipelines.method05552());
            Camera var7 = var1.method00994();
            Vec3 var8 = var7.position();
            WorldLineRenderer.method06533(var6, var5.last().pose(), var8, -1, var3.field00788, 1.0F);
            var5.popPose();
         }
      }
   }

   private void method05284() {
      this.field11515 = null;
      this.field11514 = ElytraTarget.Nested159.LEAVING;
      this.field11516 = false;
      this.field11517 = false;
      this.field11613.method04694();
      this.field11614 = null;
      this.method05295();
   }

   private boolean method05285(Vec3 var1) {
      if (mc.player != null && mc.level != null) {
         double var2 = var1.x - mc.player.getX();
         double var4 = var1.y - mc.player.getEyeY();
         double var6 = var1.z - mc.player.getZ();
         AABB var8 = mc.player.getBoundingBox().move(var2, var4, var6).inflate(field11587);
         return mc.level.noCollision(mc.player, var8) && !mc.level.containsAnyLiquid(var8) ? !this.method05297(var8) : false;
      } else {
         return false;
      }
   }

   private Vec3 method05286(Vec3 var1, Vec3 var2, double var3) {
      if (var2.lengthSqr() < field11600) {
         return null;
      }

      Vec3 var5 = var2.normalize();
      Vec3 var6 = null;
      int var7 = method05306(var3, field11601);

      for (int var8 = 1; var8 <= var7; var8++) {
         Vec3 var9 = var1.add(var5.scale(var3 * var8 / var7));
         if (!this.method05285(var9)) {
            break;
         }

         var6 = var9;
      }

      return var6;
   }

   @Override
   public void method00245() {
      this.method05284();
      this.field11513.method10074();
      super.method00245();
   }

   private void method05287(Vec3 var1, Vec3 var2, double var3) {
      double var5 = var3 * field11577;
      double var7 = GuiHitTestUtils.method05610(field11578, var3 * field11579, field11580);
      this.field11523.add(var1.scale(var3).add(0.0, var7, 0.0));
      this.field11523.add(var1.scale(var3).add(var2.scale(var5)).add(0.0, var7, 0.0));
      this.field11523.add(var1.scale(var3).add(var2.scale(-var5)).add(0.0, var7, 0.0));
      this.field11523.add(var2.scale(var3).add(0.0, var7, 0.0));
      this.field11523.add(var2.scale(-var3).add(0.0, var7, 0.0));
   }

   @Subscribe
   private void method05288(PlayerUpdateEvent var1) {
      if (mc.player != null && mc.level != null) {
         LivingEntity var2 = AttackAura.method00456();
         this.method05319(var2);
         if (this.field11504.method05999()
            && mc.player.isFallFlying()
            && var2 != null
            && !(MovementUtils.method05729(var2) >= field11557)
            && !var2.isFallFlying()) {
            Vec3 var3 = mc.player.getEyePosition();
            Vec3 var4 = var2.getEyePosition();
            double var5 = Math.hypot(var4.x - var3.x, var4.z - var3.z);
            double var7 = var3.distanceTo(var4);
            if (this.method05307()) {
               this.method05295();
               this.field11522.method04694();
               this.field11514 = ElytraTarget.Nested159.LEAVING;
               this.field11515 = this.method05315(var3, this.method05311(var2, var4), var2);
               this.field11517 = false;
               this.field11520.method04694();
               this.method05281();
               this.field11516 = true;
            } else {
               this.field11516 = false;
               if (this.field11514 == ElytraTarget.Nested159.ATTACK) {
                  this.method05304(var3, var4, var5, var2);
               } else {
                  this.method05309(var3, var4, var2, var5);
               }

               this.method05298(var7);
            }

            this.method05310(false);
         } else {
            this.method05284();
         }
      }
   }

   public ElytraTarget() {
      super(ModuleCategory.Combat, ModuleSubCategory.Utility);
      this.field11503 = new NumberSetting<Float>(field11524)
         .method10835(field11525)
         .method10838(field11526)
         .method10834(field11527)
         .method03017(field11528)
         .method03023(this);
      this.field11504 = new ToggleSetting(field11529).method03014(false).method03023(this);
      this.field11505 = new NumberSetting<Float>(field11530)
         .method10835(field11531)
         .method10838(field11532)
         .method10834(field11533)
         .method03017(field11534)
         .method03025(this.field11504::method05999)
         .method03023(this);
      this.autoFirework = new ToggleSetting("Auto Firework")
         .method03017(false)
         .method03018("Route this module's rockets through the Auto Firework Rocket scheduler and its settings.")
         .method03023(this);
      this.field11506 = new ToggleSetting(field11535).method03014(false).method03023(this);
      this.field11507 = new ModeSetting(field11536)
         .method05597(field11537, field11538)
         .method03017(field11539)
         .method03025(this.field11506::method03022)
         .method03023(this);
      this.field11508 = new NumberSetting<Integer>(field11540).method10835(5).method10838(30).method10834(1).method03017(15).method03023(this);
      this.field11509 = new NumberSetting<Float>(field11541)
         .method10835(0.0F)
         .method10838(field11542)
         .method10834(field11543)
         .method03017(field11544)
         .method03025(this.field11506::method03022)
         .method03023(this);
      this.field11510 = new ModeSetting(field11545)
         .method05597(field11546, field11547, field11548)
         .method03017(field11549)
         .method03025(this.field11506::method03022)
         .method03023(this);
      this.field11511 = new ToggleSetting(field11550).method03017(false).method03025(this.field11506::method03022).method03023(this);
      this.field11512 = new ModeSetting(field11551)
         .method05597(field11552, field11553)
         .method03017(field11554)
         .method03025(() -> this.field11506.method05999() && this.field11511.method05999())
         .method03023(this);
      this.field11513 = new ElytraTargetRenderer();
      this.field11610 = new ToggleSetting(field11615).method03014(true).method03023(this);
      this.field11611 = new NumberSetting<Float>(field11616)
         .method10835(field11618)
         .method10838(field11619)
         .method10834(1.0F)
         .method03017(field11620)
         .method03023(this);
      this.field11612 = new NumberSetting<Integer>(field11617)
         .method10835(field11621)
         .method10838(field11622)
         .method10834(field11623)
         .method03017(field11624)
         .method03023(this);
      this.field11633 = new ToggleSetting(field11626).method03014(true).method03023(this);
      this.field11634 = new NumberSetting<Float>(field11627)
         .method10835(0.0F)
         .method10838(20.0F)
         .method10834(0.5F)
         .method03017(8.0F)
         .method03025(this.field11633::method05999)
         .method03023(this);
      this.field11635 = new NumberSetting<Float>(field11628)
         .method10835(0.0F)
         .method10838(2.0F)
         .method10834(0.05F)
         .method03017(0.2F)
         .method03023(this);
      this.field11636 = new NumberSetting<Float>(field11629)
         .method10835(0.0F)
         .method10838(3.0F)
         .method10834(0.05F)
         .method03017(1.0F)
         .method03023(this);
      this.field11637 = new NumberSetting<Float>(field11630)
         .method10835(0.0F)
         .method10838(10.0F)
         .method10834(0.5F)
         .method03017(2.0F)
         .method03023(this);
      this.field11638 = new NumberSetting<Float>(field11631)
         .method10835(0.0F)
         .method10838(20.0F)
         .method10834(0.5F)
         .method03017(6.0F)
         .method03023(this);
      this.field11639 = new NumberSetting<Float>(field11632)
         .method10835(3.0F)
         .method10838(15.0F)
         .method10834(0.5F)
         .method03017(7.0F)
         .method03023(this);
      this.field11643 = new ToggleSetting(field11640).method03014(true).method03023(this);
      this.field11644 = new NumberSetting<Float>(field11641)
         .method10835(0.0F)
         .method10838(10.0F)
         .method10834(1.0F)
         .method03017(3.0F)
         .method03025(this.field11643::method05999)
         .method03023(this);
      this.field11645 = new NumberSetting<Float>(field11642)
         .method10835(0.0F)
         .method10838(3.0F)
         .method10834(0.1F)
         .method03017(0.8F)
         .method03025(this.field11643::method05999)
         .method03023(this);
      this.field11514 = ElytraTarget.Nested159.LEAVING;
      this.field11515 = null;
      this.field11516 = false;
      this.field11517 = false;
      this.field11518 = 1;
      this.field11519 = new ElapsedTimer();
      this.field11520 = new ElapsedTimer();
      this.field11521 = new ElapsedTimer();
      this.field11522 = new ElapsedTimer();
      this.field11523 = new ArrayList<>();
      this.field11613 = new ElapsedTimer();
      this.field11614 = null;
      this.method03935(field11555);
   }

   private static double method05289(double var0, double var2, double var4) {
      double var6 = Math.max(0.0, var2);
      double var8 = Math.max(0.0, var4) * field11592;
      return var0 * field11593 + var6 * field11594 - var8;
   }

   private boolean method05290(BlockState var1) {
      if (var1.isAir()) {
         return false;
      }

      if (var1.is(BlockTags.LEAVES)) {
         return true;
      }

      String var2 = BuiltInRegistries.BLOCK.getKey(var1.getBlock()).getPath();
      return var2.contains(field11604);
   }

   private boolean method05291(Vec3 var1, Vec3 var2) {
      Vec3 var3 = var2.subtract(var1);
      double var4 = var3.length();
      if (var4 < field11602) {
         return true;
      }

      int var6 = method05306(var4, field11603);

      for (int var7 = 1; var7 <= var6; var7++) {
         double var8 = (double)var7 / var6;
         Vec3 var10 = var1.add(var3.scale(var8));
         if (!this.method05285(var10)) {
            return false;
         }
      }

      return true;
   }

   private void method05292() {
      AutoFireworkRocket autoFireworkRocket = ModuleManager.INSTANCE.getAutoFireworkRocket();
      if (this.autoFirework.method05999() && autoFireworkRocket != null) {
         autoFireworkRocket.requestBoost(true);
      } else {
         ElytraHelper elytraHelper = ModuleManager.INSTANCE.method08187();
         if (!elytraHelper.useRocketSilently()) {
            elytraHelper.method06054();
         }
      }
   }

   private double method05293(Vec3 var1, Vec3 var2, float var3, float var4) {
      double var5 = var2.x - var1.x;
      double var7 = var2.y - var1.y;
      double var9 = var2.z - var1.z;
      double var11 = Math.sqrt(var5 * var5 + var9 * var9);
      if (var11 < field11575) {
         return Math.abs(var4) + field11576;
      }

      float var13 = (float)Math.toDegrees(Math.atan2(-var5, var9));
      float var14 = (float)Math.toDegrees(-Math.atan2(var7, var11));
      double var15 = Math.abs(Mth.wrapDegrees(var13 - var3));
      double var17 = Math.abs(var14 - var4);
      return var15 + var17;
   }

   private boolean method05294(Vec3 var1, Vec3 var2, Vec3 var3) {
      if (var3 == null) {
         return false;
      }

      double var4 = Math.hypot(var3.x - var2.x, var3.z - var2.z);
      return var4 < field11581 ? false : this.method05285(var3) && GuiHitTestUtils.method05608(var3) && this.method05291(var1, var3);
   }

   private void method05295() {
      PacketBufferManager.INSTNCE.method03606(field11609);
   }

   private List<Vec3> method05296(Vec3 var1, Vec3 var2) {
      ArrayList var3 = new ArrayList();
      var3.add(var1);
      var3.add(var1.add(var2).normalize());
      var3.add(var1.add(var2.reverse()).normalize());
      var3.add(var2);
      var3.add(var2.reverse());
      var3.add(var1.add(0.0, field11595, 0.0).normalize());
      var3.add(var2.add(0.0, field11596, 0.0).normalize());
      var3.add(var2.reverse().add(0.0, field11597, 0.0).normalize());
      var3.add(new Vec3(0.0, field11598, 0.0));
      var3.add(var1.add(0.0, field11599, 0.0).normalize());
      var3.add(new Vec3(0.0, 1.0, 0.0));
      return var3;
   }

   private boolean method05297(AABB var1) {
      BlockPos var2 = BlockPos.containing(var1.minX, var1.minY, var1.minZ);
      BlockPos var3 = BlockPos.containing(var1.maxX, var1.maxY, var1.maxZ);

      for (BlockPos var5 : BlockPos.betweenClosed(var2, var3)) {
         BlockState var6 = mc.level.getBlockState(var5);
         if (this.method05290(var6)) {
            return true;
         }
      }

      return false;
   }

   private void method05298(double var1) {
      if (var1 > this.field11505.method03022().floatValue() && this.field11521.method04699(field11606)) {
         this.method05295();
         this.method05308();
         this.field11521.method04694();
         this.field11522.method04694();
      }

      if (this.field11522.method04699(field11607)) {
         this.method05308();
         this.method05295();
      }
   }

   private Vec3 method05299(LivingEntity var1, Vec3 var2, Vec3 var3) {
      Vec3 var4 = var1.getDeltaMovement();
      Vec3 var5 = new Vec3(var4.x, 0.0, var4.z);
      if (var5.lengthSqr() < field11582) {
         Vec3 var6 = MovementUtils.method05718(var1);
         var5 = new Vec3(var6.x, 0.0, var6.z);
      }

      if (var5.lengthSqr() < field11583) {
         Vec3 var7 = var1.getLookAngle();
         var5 = new Vec3(var7.x, 0.0, var7.z);
      }

      if (var5.lengthSqr() < field11584) {
         Vec3 var8 = var3.subtract(var2);
         var5 = new Vec3(var8.x, 0.0, var8.z);
      }

      return var5.lengthSqr() < field11585 ? new Vec3(0.0, 0.0, 1.0) : var5.normalize().reverse();
   }

   @Subscribe
   private void method05301(AttackEntityEvent var1) {
      if (var1.method09431() != AttackEntityEvent.Nested264.POST) {
         LivingEntity var2 = AttackAura.method00456();
         if (mc.player != null && var2 != null) {
            this.method05314(mc.player.getEyePosition(), var2.getEyePosition(), var2);
         } else {
            this.field11518 = -this.field11518;
         }

         this.field11514 = ElytraTarget.Nested159.LEAVING;
         this.field11517 = true;
      }
   }

   private void method05302(Vec3 var1, Vec3 var2, LivingEntity var3) {
      if (!this.field11516 || this.field11515 == null || this.field11520.method04699(field11562)) {
         double var4 = Math.max(this.field11639.method03022().doubleValue(), this.field11505.method03022().doubleValue());
         Vec3 var6 = this.method05305(var1, this.method05311(var3, var2), var3, var4);
         if (var6 != null) {
            this.field11515 = var6;
         }

         this.field11520.method04694();
      }
   }

   private Vec3 method05303(Vec3 var1, Vec3 var2, LivingEntity var3) {
      Vec3 var4 = this.method05299(var3, var1, var2);
      Vec3 var5 = this.method05282(var4);
      Vec3 var6 = this.method05311(var3, var2);
      double var7 = Math.max(this.field11639.method03022().doubleValue(), this.field11505.method03022().doubleValue());
      this.field11523.clear();
      this.method05287(var4, var5, var7);
      this.method05287(var4, var5, var7 + field11565);
      this.method05287(var4, var5, var7 + field11566);
      float var9 = mc.player.getYRot();
      float var10 = mc.player.getXRot();
      Vec3 var11 = null;
      Vec3 var12 = null;
      double var13 = Double.NEGATIVE_INFINITY;
      double var15 = Double.NEGATIVE_INFINITY;

      for (Vec3 var18 : this.field11523) {
         double var19 = Math.hypot(var18.x, var18.z);
         if (!(var19 < field11567)) {
            Vec3 var21 = var6.add(var18);
            if (!(var21.distanceTo(var1) < field11568) && this.method05285(var21) && GuiHitTestUtils.method05608(var21) && this.method05291(var1, var21)) {
               double var22 = var18.y / Math.max(field11569, var19);
               double var24 = Math.max(0.0, Math.abs(var22) - field11570) * this.field11638.method03022().doubleValue();
               double var26 = Math.max(0.0, (var18.x * var4.x + var18.z * var4.z) / var19);
               double var28 = this.method05293(var1, var21, var9, var10);
               double var30 = this.method05312(var21);
               double var32 = -var28 * this.field11636.method03022().doubleValue()
                  + var26 * field11572
                  - var18.length() * this.field11635.method03022().doubleValue()
                  + var30 * this.field11637.method03022().doubleValue()
                  - var24;
               double var34 = var18.x * var5.x + var18.z * var5.z;
               if (var34 * this.field11518 > 0.0) {
                  if (var32 > var13) {
                     var13 = var32;
                     var11 = var18;
                  }
               } else if (var32 > var15) {
                  var15 = var32;
                  var12 = var18;
               }
            }
         }
      }

      Vec3 var36 = var11 != null ? var11 : var12;
      return var36 == null ? this.method05305(var1, var6, var3, var7 + field11574) : var6.add(var36);
   }

   private void method05304(Vec3 var1, Vec3 var2, double var3, LivingEntity var4) {
      AttackAura var5 = ModuleManager.INSTANCE.method08219();
      Vec3 var6 = this.method05311(var4, var2);
      if (this.method05316(var5)) {
         int var7 = var5.method00465();
         double var8 = Math.hypot(mc.player.getDeltaMovement().x, mc.player.getDeltaMovement().z);
         double var10 = this.method05318()
            + Math.max(0.0, this.field11644.method03022().doubleValue()) * Math.max(var8, 0.1);
         if (!var5.method00437() || var7 > this.field11644.method03022().intValue() || var3 > var10) {
            this.field11514 = ElytraTarget.Nested159.LEAVING;
            return;
         }

         Vec3 var12 = var5.field00787 && var5.field00788 != null && var5.field00788.lengthSqr() > field11582 ? var5.field00788 : var6;
         Vec3 var13 = this.method05320(var1, var12, this.method05318());
         if (this.method05285(var13) && GuiHitTestUtils.method05608(var13) && this.method05291(var1, var13)) {
            this.field11515 = var13;
         } else {
            Vec3 var14 = var13.subtract(var1);
            double var15 = Math.min(var14.length(), this.field11505.method03022().doubleValue() + field11559);
            Vec3 var17 = this.method05286(var1, var14, var15);
            if (var17 != null && var17.distanceTo(var1) >= field11560) {
               this.field11515 = var17;
            } else {
               this.field11514 = ElytraTarget.Nested159.LEAVING;
            }
         }

         return;
      }

      boolean var18 = GuiHitTestUtils.method05608(var6) && this.method05291(var1, var6);
      if (var18) {
         this.field11515 = var6;
         if (var3 <= this.field11505.method03022().floatValue() * field11558) {
            this.field11514 = ElytraTarget.Nested159.LEAVING;
         }
      } else {
         Vec3 var19 = var6.subtract(var1);
         double var20 = Math.min(var19.length(), this.field11505.method03022().floatValue() + field11559);
         Vec3 var22 = this.method05286(var1, var19, var20);
         if (var22 != null && var22.distanceTo(var1) >= field11560) {
            this.field11515 = var22;
         } else {
            this.field11514 = ElytraTarget.Nested159.LEAVING;
         }
      }
   }

   private Vec3 method05305(Vec3 var1, Vec3 var2, LivingEntity var3, double var4) {
      Vec3 var6 = this.method05299(var3, var1, var2);
      Vec3 var7 = this.method05282(var6);
      Vec3 var8 = null;
      double var9 = Double.NEGATIVE_INFINITY;

      for (Vec3 var12 : this.method05296(var6, var7)) {
         Vec3 var13 = this.method05286(var1, var12, var4);
         if (var13 != null) {
            double var14 = var13.distanceTo(var1);
            if (!(var14 < 1.0)) {
               Vec3 var16 = var12.normalize();
               double var17 = method05289(var14, var16.dot(var6), var16.y);
               if (var17 > var9) {
                  var9 = var17;
                  var8 = var13;
               }
            }
         }
      }

      if (var8 != null) {
         return var8;
      }

      Vec3 var19 = new Vec3(var6.x, 0.0, var6.z);
      if (var19.lengthSqr() < field11589) {
         var19 = new Vec3(var2.x - var1.x, 0.0, var2.z - var1.z);
      }

      if (var19.lengthSqr() < field11590) {
         var19 = new Vec3(1.0, 0.0, 0.0);
      }

      return var1.add(var19.normalize().scale(field11591));
   }

   private Vec3 method05311(LivingEntity var1, Vec3 var2) {
      AttackAura var3 = ModuleManager.INSTANCE.method08219();
      if (var3 != null
         && var3.method03937()
         && AttackAura.method00456() == var1
         && var3.field00788 != null
         && var3.field00788.lengthSqr() > field11582) {
         return var3.field00788;
      }

      if (!this.field11633.method05999()) {
         return var2;
      }

      Vec3 var4 = var1.getDeltaMovement();
      if (var4.lengthSqr() < field11582) {
         var4 = new Vec3(var1.getX() - var1.xo, var1.getY() - var1.yo, var1.getZ() - var1.zo);
      }

      if (var4.lengthSqr() < field11582) {
         return var2;
      }

      Vec3 var5 = var4;
      double var6 = 1.0;
      if (this.field11506.method05999()) {
         var6 = Mth.clamp(this.field11509.method03022().doubleValue() / 3.0, 0.0, 1.5);
         double var8 = Math.hypot(var4.x, var4.z) * field11625;
         var6 *= Mth.clamp(var8 / Math.max(this.field11508.method03022().doubleValue(), 1.0), 0.0, 1.5);
         if (this.field11507.method05600(field11537)) {
            Vec3 var10 = var1.getLookAngle();
            if (var10.lengthSqr() > field11582) {
               var5 = var10.normalize().scale(Math.max(var4.length(), 0.15));
            }
         }
      }

      double var12 = mc.player.getEyePosition().distanceTo(var2);
      double var14 = mc.player.getDeltaMovement().length();
      double var16 = Mth.clamp(var12 / Math.max(var14, 0.1), 0.0, this.field11634.method03022().doubleValue());
      var16 *= var6;
      return var16 <= field11575 ? var2 : var2.add(var5.scale(var16));
   }

   private double method05312(Vec3 var1) {
      if (mc.player == null || mc.level == null) {
         return 0.0;
      }

      AABB var2 = mc.player
         .getBoundingBox()
         .move(var1.x - mc.player.getX(), var1.y - mc.player.getEyeY(), var1.z - mc.player.getZ())
         .inflate(field11587);
      if (!mc.level.noCollision(mc.player, var2)) {
         return 0.0;
      }

      double var3 = 1.0;
      if (mc.level.containsAnyLiquid(var2)) {
         var3 -= 0.5;
      }

      if (this.method05297(var2)) {
         var3 -= 0.75;
      }

      return Math.max(0.0, var3);
   }

   private double method05313() {
      AttackAura var1 = ModuleManager.INSTANCE.method08219();
      double var2 = var1 == null
         ? this.field11505.method03022().doubleValue() + field11559
         : var1.method00403() + var1.method00414();
      if (mc.player != null && mc.player.isFallFlying() && this.method03937()) {
         var2 += this.field11503.method03022().doubleValue();
      }

      return Math.max(2.0, var2);
   }

   private double method05318() {
      return Math.max(2.0, this.method05313() - this.field11645.method03022().doubleValue());
   }

   private boolean method05316(AttackAura var1) {
      return this.field11643.method05999() && var1 != null && var1.method03937();
   }

   private boolean method05319(LivingEntity var1) {
      int var2 = var1 == null ? Integer.MIN_VALUE : var1.getId();
      if (var2 == this.field11646) {
         return false;
      }

      this.field11646 = var2;
      this.method05284();
      this.field11518 = 1;
      this.field11519.method04694();
      this.field11520.method04694();
      this.field11521.method04694();
      this.field11522.method04694();
      this.field11613.method04694();
      return true;
   }

   public void method05322(LivingEntity var1) {
      this.method05319(var1);
   }

   private Vec3 method05320(Vec3 var1, Vec3 var2, double var3) {
      Vec3 var5 = var1.subtract(var2);
      double var6 = var5.length();
      if (var6 < field11600) {
         return var2;
      }

      return var2.add(var5.scale(Math.min(var6, Math.max(0.0, var3)) / var6));
   }

   private void method05314(Vec3 var1, Vec3 var2, LivingEntity var3) {
      Vec3 var4 = this.method05299(var3, var1, var2);
      Vec3 var5 = this.method05282(var4);
      Vec3 var6 = this.method05311(var3, var2);
      double var7 = Math.max(this.field11639.method03022().doubleValue(), this.field11505.method03022().doubleValue());
      double[] var9 = new double[2];

      for (int var10 = 0; var10 < 2; var10++) {
         double var11 = var10 == 0 ? 1.0 : -1.0;
         Vec3 var13 = var5.scale(var11);

         for (int var14 = 0; var14 < 3; var14++) {
            double var15 = var7 + var14 * field11565;
            Vec3 var17 = var6.add(var13.scale(var15));
            if (this.method05294(var1, var2, var17)) {
               var9[var10] = var9[var10] + this.method05312(var17) * 2.0 + 1.0 - this.method05293(var1, var17, mc.player.getYRot(), mc.player.getXRot()) * 0.01;
            } else {
               var9[var10] -= 2.0;
            }
         }
      }

      if (Math.abs(var9[0] - var9[1]) > field11575) {
         this.field11518 = var9[0] >= var9[1] ? 1 : -1;
      } else {
         this.field11518 = -this.field11518;
      }
   }

   private Vec3 method05315(Vec3 var1, Vec3 var2, LivingEntity var3) {
      Vec3 var4 = mc.player.getDeltaMovement();
      Vec3 var5 = new Vec3(var4.x, 0.0, var4.z);
      if (var5.lengthSqr() < field11582) {
         var5 = new Vec3(var1.x - var2.x, 0.0, var1.z - var2.z);
      }

      if (var5.lengthSqr() < field11582) {
         var5 = new Vec3(1.0, 0.0, 0.0);
      }

      var5 = var5.normalize();
      Vec3 var6 = this.method05282(var5);
      double var7 = Math.max(this.field11639.method03022().doubleValue(), this.field11505.method03022().doubleValue());
      Vec3[] var9 = new Vec3[]{
         var5.add(0.0, 0.8, 0.0).normalize(),
         var6,
         var6.reverse(),
         var5.add(var6).normalize(),
         var5.add(var6.reverse()).normalize(),
         new Vec3(0.0, 1.0, 0.0),
         var5.add(0.0, -0.4, 0.0).normalize()
      };
      Vec3 var10 = null;
      double var11 = Double.NEGATIVE_INFINITY;

      for (Vec3 var13 : var9) {
         Vec3 var14 = this.method05286(var1, var13, var7);
         if (var14 != null && !(var14.distanceTo(var1) < field11561)) {
            double var15 = this.method05312(var14);
            if (!(var15 <= 0.0)) {
               double var17 = this.method05293(var1, var14, mc.player.getYRot(), mc.player.getXRot());
               double var19 = var15 * 4.0 - var17 * 0.05 + var14.distanceTo(var2) * 0.02;
               if (var19 > var11) {
                  var11 = var19;
                  var10 = var14;
               }
            }
         }
      }

      return var10 != null ? var10 : this.method05305(var1, var2, var3, var7);
   }

   private static int method05306(double var0, double var2) {
      return Math.max(2, (int)Math.ceil(var0 / var2));
   }

   private boolean method05307() {
      if (mc.player == null || mc.level == null) {
         return false;
      }

      if (mc.player.horizontalCollision) {
         return true;
      }

      AABB var1 = mc.player.getBoundingBox().inflate(field11588);
      return !mc.level.noCollision(mc.player, var1);
   }

   private void method05308() {
      PacketBufferManager.INSTNCE.method03604(field11608);
   }

   private void method05309(Vec3 var1, Vec3 var2, LivingEntity var3, double var4) {
      AttackAura var6 = ModuleManager.INSTANCE.method08219();
      boolean var7 = this.field11517
         || this.field11515 == null
         || var1.distanceTo(this.field11515) < field11561
         || !this.method05294(var1, var2, this.field11515);
      if (var7) {
         this.field11515 = this.method05303(var1, var2, var3);
         this.field11517 = false;
         this.method05310(true);
      }

      if (this.method05316(var6)) {
         double var8 = Math.hypot(mc.player.getDeltaMovement().x, mc.player.getDeltaMovement().z);
         double var10 = this.method05318()
            + Math.max(0.0, this.field11644.method03022().doubleValue()) * Math.max(var8, 0.1);
         if (var6.method00437()
            && var6.method00465() <= this.field11644.method03022().intValue()
            && var4 <= var10) {
            this.field11514 = ElytraTarget.Nested159.ATTACK;
            this.method05281();
         }
      } else if (var4 > this.field11505.method03022().floatValue()) {
         this.field11514 = ElytraTarget.Nested159.ATTACK;
         this.method05281();
      }
   }

   private void method05310(boolean var1) {
      if (mc.player == null || mc.level == null) {
         return;
      }

      if (!this.field11610.method05999() || !mc.player.isFallFlying() || this.field11515 == null) {
         return;
      }

      if (InventoryUtils.method06136(Items.FIREWORK_ROCKET) == null) {
         return;
      }

      // Speed is measured in blocks per second; this also covers the LEAVING state while already fast enough.
      double var2 = Math.hypot(mc.player.getDeltaMovement().x, mc.player.getDeltaMovement().z) * field11625;
      double var4 = this.field11611.method03022().doubleValue();
      if (var2 >= var4) {
         return;
      }

      if (var1) {
         if (this.field11614 != null && this.field11614.distanceTo(this.field11515) < 1.0) {
            return;
         }
      } else if (!this.field11613.method04699((long)this.field11612.method03022().intValue())) {
         return;
      }

      ElytraHelper elytraHelper = ModuleManager.INSTANCE.method08187();
      // Packet-level rocket use: no visible hotbar change, no rotation packet and nothing held by the lag buffer.
      if (!elytraHelper.useRocketSilently()) {
         elytraHelper.method06054();
      }

      this.field11613.method04694();
      if (var1) {
         this.field11614 = this.field11515;
      }
   }

   // $VF: Compiled from s
   public enum Nested159 {
      ATTACK,
      LEAVING;
   }
}
