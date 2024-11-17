package nc.rpc.core.server;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import nc.rpc.core.vo.NettyRequestMessage;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ServerBusiHandler
        extends SimpleChannelInboundHandler<NettyRequestMessage> {
    private static final Log LOG
            = LogFactory.getLog(ServerBusiHandler.class);

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, NettyRequestMessage msg)
            throws Exception {
        LOG.info(msg);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx)
            throws Exception {
        LOG.info(ctx.channel().remoteAddress()+" 主动断开了连接!");
    }

}
