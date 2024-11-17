package nc.rpc.core.kryo;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import nc.rpc.core.vo.NettyRequestHeader;
import nc.rpc.core.vo.NettyRequestMessage;

import java.util.HashMap;
import java.util.Map;

public class TestKryoCodeC {

    public NettyRequestMessage getMessage() {
		NettyRequestMessage myMessage = new NettyRequestMessage();
		NettyRequestHeader myHeader = new NettyRequestHeader();
		myHeader.setLength(123);
		myHeader.setSessionID(99999);
		myHeader.setType((byte) 1);
		myHeader.setPriority((byte) 7);
		Map<String, Object> attachment = new HashMap<String, Object>();
		for (int i = 0; i < 10; i++) {
			attachment.put("ciyt --> " + i, "lilinfeng " + i);
		}
		myHeader.setAttachment(attachment);
		myMessage.setMyHeader(myHeader);
		myMessage.setBody("abcdefg-----------------------AAAAAA");
		return myMessage;
    }

    /**
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
		TestKryoCodeC testC = new TestKryoCodeC();

		for (int i = 0; i < 5; i++) {
			ByteBuf sendBuf = Unpooled.buffer();
            NettyRequestMessage message = testC.getMessage();
            System.out.println("Encode:"+message + "[body ] "
                    + message.getBody());
            KryoSerializer.serialize(message, sendBuf);
			NettyRequestMessage decodeMsg = (NettyRequestMessage) KryoSerializer.deserialize(sendBuf);
			System.out.println("Decode:"+decodeMsg + "<body > "
					+ decodeMsg.getBody());
			System.out
				.println("-------------------------------------------------");
		}

    }

}
