package nc.rpc.core.server;


import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;
import nc.rpc.core.vo.MessageType;
import nc.rpc.core.vo.NettyRequestHeader;
import nc.rpc.core.vo.NettyRequestMessage;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class HeartBeatRespHandler extends ChannelInboundHandlerAdapter {

	private static final Log LOG
			= LogFactory.getLog(HeartBeatRespHandler.class);

    public void channelRead(ChannelHandlerContext ctx, Object msg)
	    throws Exception {
		NettyRequestMessage message = (NettyRequestMessage) msg;
		/*是不是心跳请求*/
		if(message.getMyHeader()!=null
				&&message.getMyHeader().getType()== MessageType.HEARTBEAT_REQ.value()){
			/*心跳应答报文*/
			NettyRequestMessage heartBeatResp = buildHeatBeat();
			ctx.writeAndFlush(heartBeatResp);
			ReferenceCountUtil.release(msg);
		}else{
			ctx.fireChannelRead(msg);
		}
    }

    private NettyRequestMessage buildHeatBeat() {
		NettyRequestMessage message = new NettyRequestMessage();
		NettyRequestHeader myHeader = new NettyRequestHeader();
		myHeader.setType(MessageType.HEARTBEAT_RESP.value());
		message.setMyHeader(myHeader);
		return message;
    }

}
