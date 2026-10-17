package yuuria.stackupper.network;

import gnu.trove.map.TObjectIntMap;
import gnu.trove.map.hash.TObjectIntHashMap;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import yuuria.stackupper.StackUpper;
import yuuria.stackupper.StackUpperHelpers;

public class SyncItemData implements IMessage {
    private TObjectIntMap<Item> map;
    private int[] confVal;

    public SyncItemData() {}

    public SyncItemData(int[] confValue, TObjectIntMap<Item> map)
    {
        this.confVal = confValue;
        this.map = map;
    }

    public SyncItemData(TObjectIntMap<Item> map)
    {
        this.confVal = StackUpperHelpers.generateConfigInt();
        this.map = map;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        int len = buf.readInt();
        int[] cfg = new int[len];
        for (int i = 0; i < len; i++) {
            cfg[i] = buf.readInt();
        }
        this.confVal = cfg;

        int size = buf.readInt();
        TObjectIntMap<Item> m = new TObjectIntHashMap<>(size);
        for (int i = 0; i < size; i++)
        {
            Item item = Item.getItemById(buf.readInt());
            int value = buf.readInt();
            if (item != null) m.put(item, value);
        }

        this.map = m;
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(confVal.length);
        for (int v : confVal) {
            buf.writeInt(v);
        }
        buf.writeInt(map.size());
        map.forEachEntry((item, val) -> {
            buf.writeInt(Item.getIdFromItem(item));
            buf.writeInt(val);
            return true;
        });
    }

    public static class Handler implements IMessageHandler<SyncItemData, IMessage> {
        @Override
        public IMessage onMessage(SyncItemData msg, MessageContext ctx) {
            if (ctx.side == Side.CLIENT) {
                StackUpper.LOGGER.info("recieved msg from server");
                Minecraft.getMinecraft().addScheduledTask(() -> {
                    StackUpper.LOGGER.info("{}\n{}", msg.confVal, msg.map);
                    StackUpperHelpers.trueConf = msg.confVal;
                    StackUpperHelpers.appliedStackMap = msg.map;
                });
            }
            return null;
        }
    }
}
