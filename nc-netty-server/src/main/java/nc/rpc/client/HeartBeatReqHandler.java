package nc.rpc.client;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;
import nc.rpc.core.vo.MessageType;
import nc.rpc.core.vo.NettyRequestHeader;
import nc.rpc.core.vo.NettyRequestMessage;

import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class HeartBeatReqHandler extends ChannelInboundHandlerAdapter {

    private static final Log LOG = LogFactory.getLog(HeartBeatReqHandler.class);
    private volatile  ScheduledFuture<?> heartBeat;

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        NettyRequestMessage message = (NettyRequestMessage) msg;
        /*是不是握手认证成功的应答*/
        if(message.getMyHeader()!=null
                &&message.getMyHeader().getType()== MessageType.LOGIN_RESP.value()){
            /*Netty已经提供了定时机制，定时发出心跳请求*/
            heartBeat = ctx.executor().scheduleAtFixedRate(
                    new  HeartBeatTask(ctx),0,
                    5000,TimeUnit.MILLISECONDS);
            ReferenceCountUtil.release(msg);
            /*是不是心跳的应答*/
        }else if(message.getMyHeader()!=null
                &&message.getMyHeader().getType()==MessageType.HEARTBEAT_RESP.value()){
            //LOG.info("收到服务器心跳应答");
            ReferenceCountUtil.release(msg);
        }else{
            ctx.fireChannelRead(msg);
        }
    }

    /*心跳请求的任务类*/
    private class HeartBeatTask implements Runnable {
        private final ChannelHandlerContext ctx;

        public HeartBeatTask(ChannelHandlerContext ctx) {
            this.ctx = ctx;
        }

        @Override
        public void run() {
            NettyRequestMessage heartBeat = buildHeatBeat();
            ctx.writeAndFlush(heartBeat);
        }

        private NettyRequestMessage buildHeatBeat() {
            NettyRequestMessage message = new NettyRequestMessage();
            NettyRequestHeader myHeader = new NettyRequestHeader();
            myHeader.setType(MessageType.HEARTBEAT_REQ.value());
            message.setMyHeader(myHeader);
            return message;
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
            throws Exception {
        cause.printStackTrace();
        if (heartBeat != null) {
            heartBeat.cancel(true);
            heartBeat = null;
        }
        ctx.fireExceptionCaught(cause);
    }
}
