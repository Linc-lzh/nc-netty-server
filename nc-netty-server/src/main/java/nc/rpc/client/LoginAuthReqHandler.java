package nc.rpc.client;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import nc.rpc.core.vo.MessageType;
import nc.rpc.core.vo.NettyRequestHeader;
import nc.rpc.core.vo.NettyRequestMessage;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
public class LoginAuthReqHandler extends ChannelInboundHandlerAdapter {

    private static final Log LOG = LogFactory.getLog(LoginAuthReqHandler.class);

    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        /*发出认证请求*/
        ctx.writeAndFlush(buildLoginReq());
    }

    public void channelRead(ChannelHandlerContext ctx, Object msg)
            throws Exception {
        NettyRequestMessage message = (NettyRequestMessage) msg;
        /*是不是握手成功的应答*/
        if(message.getMyHeader()!=null
                &&message.getMyHeader().getType()== MessageType.LOGIN_RESP.value()){
            byte loginResult = (byte) message.getBody();
            if (loginResult != (byte) 0) {
                // 握手失败，关闭连接
                ctx.close();
            } else {
                LOG.info("Login is ok : " + message);
                ctx.fireChannelRead(msg);
            }
        }else{
            ctx.fireChannelRead(msg);
        }
    }

    private NettyRequestMessage buildLoginReq() {
        NettyRequestMessage message = new NettyRequestMessage();
        NettyRequestHeader myHeader = new NettyRequestHeader();
        myHeader.setType(MessageType.LOGIN_REQ.value());
        message.setMyHeader(myHeader);
        return message;
    }

    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
            throws Exception {
        ctx.fireExceptionCaught(cause);
    }
}
