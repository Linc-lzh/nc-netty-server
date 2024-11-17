package nc.rpc.core.kryo;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import nc.rpc.core.vo.NettyRequestMessage;

public class KryoEncoder  extends MessageToByteEncoder<NettyRequestMessage> {

    @Override
    protected void encode(ChannelHandlerContext ctx, NettyRequestMessage message,
                          ByteBuf out) throws Exception {
        KryoSerializer.serialize(message, out);
        ctx.flush();
    }
}
