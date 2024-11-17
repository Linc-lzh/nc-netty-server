package nc.rpc;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import nc.rpc.core.server.ServerInit;
import nc.rpc.core.vo.NettyConstant;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class NettyServer {
    private static final Log LOG = LogFactory.getLog(NettyServer.class);

    public void bind() throws Exception {
        // 配置服务端的NIO线程组
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        ServerBootstrap b = new ServerBootstrap();
        b.group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .option(ChannelOption.SO_BACKLOG, 1024)
                .childHandler(new ServerInit());

        // 绑定端口，同步等待成功
        b.bind(NettyConstant.SERVER_PORT).sync();
        LOG.info("Netty server start : "
                + (NettyConstant.SERVER_IP + " : "
                + NettyConstant.SERVER_PORT));
    }

    public static void main(String[] args) throws Exception {
        new NettyServer().bind();
    }
}
