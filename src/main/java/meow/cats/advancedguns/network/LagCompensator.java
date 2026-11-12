package meow.cats.advancedguns.network;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.AABB;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.UUID;

public class LagCompensator {
    private static final Map<UUID, LinkedList<Snapshot>> HISTORY = new HashMap<>();

    public record Snapshot(long tick, AABB box) {}

    public static void recordServerTick(ServerLevel level) {
        long currentTick = level.getGameTime();
        for (Entity entity : level.getAllEntities()) {
            if (entity instanceof LivingEntity living && living.isAlive()) {
                LinkedList<Snapshot> list = HISTORY.computeIfAbsent(living.getUUID(), k -> new LinkedList<>());
                list.addLast(new Snapshot(currentTick, living.getBoundingBox()));
                while (!list.isEmpty() && currentTick - list.getFirst().tick() > 20) {
                    list.removeFirst();
                }
            }
        }
        HISTORY.keySet().removeIf(uuid -> level.getEntity(uuid) == null);
    }

    public static AABB getCompensatedBox(Entity entity, long targetTick) {
        LinkedList<Snapshot> list = HISTORY.get(entity.getUUID());
        if (list == null || list.isEmpty()) {
            return entity.getBoundingBox();
        }
        Snapshot best = list.getLast();
        long minDiff = Long.MAX_VALUE;
        for (Snapshot snap : list) {
            long diff = Math.abs(snap.tick() - targetTick);
            if (diff <= minDiff) {
                minDiff = diff;
                best = snap;
            }
        }
        return best.box();
    }
}
