package nc.rpc.core.vo;

public final class NettyRequestMessage {

    private NettyRequestHeader myHeader;

    private Object body;

    public final NettyRequestHeader getMyHeader() {
    	return myHeader;
    }

    public final void setMyHeader(NettyRequestHeader myHeader) {
    	this.myHeader = myHeader;
    }

    public final Object getBody() {
    	return body;
    }

    public final void setBody(Object body) {
    	this.body = body;
    }

    @Override
    public String toString() {
    	return "MyMessage [myHeader=" + myHeader + "][body="+body+"]";
    }
}
